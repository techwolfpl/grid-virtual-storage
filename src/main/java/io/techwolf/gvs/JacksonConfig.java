package io.techwolf.gvs;

import static com.fasterxml.jackson.core.StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigDecimal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilder objectMapperBuilder() {
    final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToEnable(WRITE_BIGDECIMAL_AS_PLAIN);
    builder.serializerByType(BigDecimal.class, new ToStringSerializer());
    return builder;
  }
}