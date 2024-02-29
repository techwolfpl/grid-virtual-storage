package io.techwolf.gvs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class GvsServiceApplication {


  public static final String VERSION = "0.0.9";

  ObjectMapper mapper;


  public static void main(String[] args) {
    SpringApplication.run(GvsServiceApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    return mapper;
  }

  @Bean("settings")
  @SneakyThrows
  public Settings settings(@Value("${ha.options:/data/options.json}") String configLocan) {
    final String json = FileUtils.readFileToString(ResourceUtils.getFile(configLocan), "UTF-8");
    return objectMapper().readValue(json, Settings.class);
  }
}
