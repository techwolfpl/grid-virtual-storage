package io.techwolf.gvs;

import static io.restassured.RestAssured.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.techwolf.gvs.application.energy.EnergyGridDto;
import io.techwolf.gvs.application.energy.ReadingsDto;
import java.time.LocalDate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GvsServiceApplicationTests {

  @LocalServerPort
  int randomServerPort;

  @Autowired
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.baseURI = "http://localhost:" + randomServerPort;

  }

  @Test
  @SneakyThrows
  @Disabled
  public void testGridUpdate() {
    final Response response = RestAssured.get("/api/grid/main");
    final EnergyGridDto grid = mapper.readValue(response.getBody().asString(), EnergyGridDto.class);

    final LocalDate testDate = LocalDate.now().plusYears(3);
    grid.setRealReverseEnergy(3454.4345f);
    grid.getStorages().get(0).setChargedTo(testDate);

    given()
        .body(mapper.writeValueAsString(grid))
        .contentType(ContentType.JSON)
        .log().body()
        .when()
        .post("/api/grid/main")
        .then()
        .statusCode(200);

    given()
        .when()
        .get("/api/grid/main")
        .then()
        .statusCode(200)
        .log()
        .body();

    final Response response2 = RestAssured.get("/api/grid/main");
    System.out.println(response2.getBody().asString());
    final EnergyGridDto grid3 = mapper.readValue(response2.getBody().asString(), EnergyGridDto.class);

  }

  @Test
  @SneakyThrows
  @Disabled
  public void testEnergyStorage() {
    given()
        .body(mapper.writeValueAsString(new ReadingsDto(0.0f, 100.0f)))
        .contentType(ContentType.JSON)
        .when()
        .post("/api/grid/main/readings")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/grid/main/battery/energy")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .body(mapper.writeValueAsString(new ReadingsDto(0.0f, 200.0f)))
        .contentType(ContentType.JSON)
        .when()
        .post("/api/grid/main/readings")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()

        .contentType(ContentType.JSON)
        .when()
        .get("/api/grid/main/battery/energy")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .body(mapper.writeValueAsString(new ReadingsDto(50f, 200.0f)))
        .contentType(ContentType.JSON)
        .when()
        .post("/api/grid/main/readings")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/grid/main/battery/energy")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .body(mapper.writeValueAsString(new ReadingsDto(50f, 300.0f)))
        .contentType(ContentType.JSON)
        .when()
        .post("/api/grid/main/readings")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()

        .contentType(ContentType.JSON)
        .when()
        .get("/api/grid/main/battery/energy")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()
        .body(mapper.writeValueAsString(new ReadingsDto(100f, 300.0f)))
        .contentType(ContentType.JSON)
        .when()
        .post("/api/grid/main/readings")
        .then()
        .statusCode(200)
        .log()
        .body();

    given()

        .contentType(ContentType.JSON)
        .when()
        .get("/api/grid/main/battery/energy")
        .then()
        .statusCode(200)
        .log()
        .body();
  }
}
