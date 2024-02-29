package io.techwolf.gvs.domain;

import static io.techwolf.gvs.GvsServiceApplication.VERSION;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.techwolf.gvs.domain.grid.port.EnergyGridProviderPort;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegisterDevicesDomainService {

  @Autowired
  EnergyGridProviderPort gridProvider;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  PostMessagePort postMessagePort;

  //temporary because of clsspath resource problem
  private String gridTemplate = "{\n"
      + "  \"~\": \"techwolf-pl/devices/grid-virtual-storage/grids/GRID_NAME\",\n"
      + "  \"device\": {\n"
      + "    \"ids\": \"grid-virtual-storage\",\n"
      + "    \"mf\": \"techwolf.pl\",\n"
      + "    \"name\": \"Grid Virtual Storage\",\n"
      + "    \"sw\": \"VERSION\"\n"
      + "  },\n"
      + "  \"name\": \"Grid Virtual Storage GRID_NAME - DISPLAY_NAME\",\n"
      + "  \"uniq_id\": \"grid_virtual_storage_grid_GRID_NAME_PARAMETER_NAME\",\n"
      + "  \"qos\": 0,\n"
      + "  \"unit_of_meas\": \"kWh\",\n"
      + "  \"stat_t\": \"~/state/PARAMETER_NAME\",\n"
      + "  \"val_tpl\": \"{{ value | round(5,default=none) }}\",\n"
      + "  \"dev_cla\": \"energy\",\n"
      + "  \"state_class\": \"CLASS\",\n"
      + "  \"icon\": \"ICON\"\n"
      + "}";
  private String storageTemplate = "{\n"
      + "  \"~\": \"techwolf-pl/devices/grid-virtual-storage/grids/GRID_NAME\",\n"
      + "  \"device\": {\n"
      + "    \"ids\": \"grid-virtual-storage\",\n"
      + "    \"mf\": \"techwolf.pl\",\n"
      + "    \"name\": \"Grid Virtual Storage\",\n"
      + "    \"sw\": \"VERSION\"\n"
      + "  },\n"
      + "  \"name\": \"Grid GRID_NAME storage STORAGE_NAME - DISPLAY_NAME \",\n"
      + "  \"uniq_id\": \"grid_virtual_storage_grid_GRID_NAME_storage_STORAGE_NAME_PARAMETER_NAME\",\n"
      + "  \"qos\": 0,\n"
      + "  \"unit_of_meas\": \"kWh\",\n"
      + "  \"stat_t\": \"~/state/storages/STORAGE_NAME/PARAMETER_NAME\",\n"
      + "  \"val_tpl\": \"{{ value | round(5,default=none) }}\",\n"
      + "  \"dev_cla\": \"energy\",\n"
      + "  \"state_class\": \"CLASS\",\n"
      + "  \"icon\": \"ICON\"\n"
      + "}";

  @SneakyThrows
  @Transactional
  @Scheduled(initialDelay = 2000, fixedDelay = 10000)
  public void registerDevices() {
    var grid = gridProvider.getEnergyGrid();
    var messages = createMessagesForGrid(grid);
    log.info("Registering devices: ");
    messages.forEach((k, v) -> {
      log.info(k + " - " + v);
      postMessagePort.post(k, v, 2);
      // ugly but works ;/;/;/
    });
  }

  @SneakyThrows
  Map<String, String> createMessagesForGrid(EnergyGrid grid) {
    Map<String, String> messageMap = new HashMap<>();
    {
      var msg = gridTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Total grid forward energy");
      msg = msg.replaceAll("PARAMETER_NAME", "total_grid_forward_energy");
      msg = msg.replaceAll("CLASS", "total_increasing");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:transmission-tower-import");
      messageMap.put(createTopic(msg), msg);
    }

    {
      var msg = gridTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Total grid reverse energy");
      msg = msg.replaceAll("PARAMETER_NAME", "total_grid_reverse_energy");
      msg = msg.replaceAll("CLASS", "total_increasing");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:transmission-tower-export");
      messageMap.put(createTopic(msg), msg);
    }

    {
      var msg = gridTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Total storage charged energy");
      msg = msg.replaceAll("PARAMETER_NAME", "total_storage_charged_energy");
      msg = msg.replaceAll("CLASS", "total_increasing");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:battery-arrow-down");
      messageMap.put(createTopic(msg), msg);
    }

    {
      var msg = gridTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Total storage used energy");
      msg = msg.replaceAll("PARAMETER_NAME", "total_storage_used_energy");
      msg = msg.replaceAll("CLASS", "total_increasing");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:battery-arrow-up");
      messageMap.put(createTopic(msg), msg);
    }

    {
      var msg = gridTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Current period energy balance");
      msg = msg.replaceAll("PARAMETER_NAME", "current_periond_energy_balance");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:battery-clock");
      messageMap.put(createTopic(msg), msg);
    }

    var storages = grid.getStorages();
    storages.forEach(storage -> createMessageForStorage(messageMap, storage, grid));
    return messageMap;
  }

  private void createMessageForStorage(Map<String, String> messageMap, EnergyStorage storage,
      EnergyGrid grid) {

    {
      var msg = storageTemplate;
      msg = msg.replaceAll("GRID_NAME", grid.getName());
      msg = msg.replaceAll("DISPLAY_NAME", "Energy stored");
      msg = msg.replaceAll("STORAGE_NAME", storage.getName());
      msg = msg.replaceAll("PARAMETER_NAME", "energy_stored");
      msg = msg.replaceAll("VERSION", VERSION);
      msg = msg.replaceAll("ICON", "mdi:battery-charging");

      messageMap.put(createTopic(msg), msg);
    }
  }

  @SneakyThrows
  private String createTopic(String msg) {
    return "homeassistant/sensor/techwolf-pl/" + objectMapper.readValue(msg, UniqueEntity.class).getUniq_id()
        + "/config";
  }


}
