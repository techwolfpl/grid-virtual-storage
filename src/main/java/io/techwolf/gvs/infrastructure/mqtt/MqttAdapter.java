package io.techwolf.gvs.infrastructure.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.techwolf.gvs.Settings;
import io.techwolf.gvs.domain.PostMessagePort;
import io.techwolf.gvs.domain.RegisterGridReadingsUseCase;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttAdapter implements PostMessagePort {

  @Autowired
  RegisterGridReadingsUseCase registerGridReadingsUseCase;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  Settings settings;

  private final Lock lock = new ReentrantLock();

  private MqttAsyncClient mqtt = null;

  public MqttAdapter() throws MqttException {}

  @SneakyThrows
  @Scheduled(fixedDelay = 10000)
  public void initialize() {
    if (mqtt != null && mqtt.isConnected()) {
      return;
    }

    if (mqtt == null) {
      mqtt = new MqttAsyncClient(this.settings.getMqttServerUrl(), UUID.randomUUID().toString());
    }
    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(this.settings.getMqttUsername());
    options.setPassword(this.settings.getMqttPassword().toCharArray());
    mqtt.connect(options);
    var limit = 100;
    while (!mqtt.isConnected() && limit > 0) {
      Thread.sleep(200);
      limit -= 1;
      log.info("Waiting for mqtt");
    }
    if (limit == 0) {
      throw new RuntimeException("MQTT  Connection failed!");
    } else {
      log.info("Mqtt connected");
    }

    mqtt.subscribe(this.settings.getForwardEnergyTopic(), 1,
        (topic, message) -> {
          lock.lock();
          try {
            var updates = registerGridReadingsUseCase.registerForward(
                BigDecimal.valueOf(Double.valueOf(message.toString())));
            updates.forEach((k, v) -> this.post(k, v, 0));
          } finally {
            lock.unlock();
          }
        });

    mqtt.subscribe(this.settings.getReverseEnergyTopic(), 1,
        (topic, message) -> {
          lock.lock();
          try {
            var updates = registerGridReadingsUseCase.registerReverse(
                BigDecimal.valueOf(Double.valueOf(message.toString())));
            updates.forEach((k, v) -> this.post(k, v, 0));
          } finally {
            lock.unlock();
          }
        });

  }

  @Override
  @SneakyThrows
  public void post(String topic, String message, int qos) {
    this.initialize();
    var msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
    msg.setQos(qos);
    mqtt.publish(topic, msg);
  }
}
