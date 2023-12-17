package io.techwolf.gvs.application.energy;

import io.techwolf.gvs.domain.EnergyGridQuery;
import io.techwolf.gvs.domain.EnergyStorage;
import io.techwolf.gvs.domain.RegisterGridReadingsUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EnergyController {
  private EnergyGridQuery energyGridQuery;
  private RegisterGridReadingsUseCase registerGridReadingsUseCase;

  @GetMapping("/api/grid/main/battery/energy")
  public Double getBatteryEnergy() {
    return energyGridQuery
      .getEnergyGrid()
      .getStorageForDischarge(LocalDate.now())
      .map(EnergyStorage::getEnergy)
      .orElse(BigDecimal.ZERO)
      .doubleValue();
  }

  @GetMapping("/api/grid/main/battery/used")
  public Double getBatteryUsed() {
    var grid = energyGridQuery.getEnergyGrid();
    return grid.getEnergyUsedFromStorage().doubleValue();
  }

  @GetMapping("/api/grid/main/battery/charged")
  public Double getBatteryCharged() {
    var grid = energyGridQuery.getEnergyGrid();
    return grid.getEnergyChargedToStorage().doubleValue();
  }

  @GetMapping("/api/grid/main/forward")
  public Double getGridForward() {
    var grid = energyGridQuery.getEnergyGrid();
    return grid.getForwardEnergy().doubleValue();
  }

  @GetMapping("/api/grid/main/reverse")
  public Double getGridReverse() {
    var grid = energyGridQuery.getEnergyGrid();
    return grid.getReverseEnergy().doubleValue();
  }

  @PostMapping("/api/grid/main/readings")
  public void registerGridReadings(@RequestBody ReadingsDto readings) {
    registerGridReadingsUseCase.register(
      BigDecimal.valueOf(readings.getGridForwardEnergy()),
      BigDecimal.valueOf(readings.getGridReverseEnergy())
    );
  }
}
