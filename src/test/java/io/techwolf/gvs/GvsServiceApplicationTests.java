package io.techwolf.gvs;

import io.techwolf.gvs.infrastructure.persistence.entity.EnergyStorageEntity;
import io.techwolf.gvs.infrastructure.persistence.entity.EnergyStorageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GvsServiceApplicationTests {

  @Test
  public void shouldSaveEnergyStorage() {
    when().request("GET", "/api/grid/main/battery/used").then().statusCode(200);
  }
}
