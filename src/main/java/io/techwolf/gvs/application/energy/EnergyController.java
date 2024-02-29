package io.techwolf.gvs.application.energy;

import io.techwolf.gvs.domain.EnergyGridQuery;
import io.techwolf.gvs.domain.EnergyStorage;
import io.techwolf.gvs.domain.RegisterGridReadingsUseCase;
import io.techwolf.gvs.domain.UpdateEnergyGridUseCase;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private UpdateEnergyGridUseCase updateEnergyGridUseCase;
  private RequestResponseMapper mapper;

  @GetMapping("/api/grid/main")
  public EnergyGridDto getGrid() {
    return mapper.map(energyGridQuery.getEnergyGrid());
  }

  @PostMapping("/api/grid/main")
  public void updateGrid(@RequestBody EnergyGridDto energyGrid) {
    updateEnergyGridUseCase.update(mapper.map(energyGrid));
  }

  @GetMapping("/api/grid/main/battery/energy")
  public SingleValueResponse<Float> getBatteryEnergy() {
    return new SingleValueResponse<>(
        energyGridQuery
            .getEnergyGrid()
            .getStorageForDischarge(LocalDate.now())
            .map(EnergyStorage::getEnergy)
            .orElse(BigDecimal.ZERO)
            .setScale(4, RoundingMode.HALF_UP)
            .floatValue()
    );
  }

  @GetMapping("/api/grid/main/battery/used")
  public SingleValueResponse<Float> getBatteryUsed() {
    var grid = energyGridQuery.getEnergyGrid();
    return new SingleValueResponse<>(grid.getEnergyUsedFromStorage().floatValue());
  }

  @GetMapping("/api/grid/main/battery/charged")
  public SingleValueResponse<Float> getBatteryCharged() {
    var grid = energyGridQuery.getEnergyGrid();
    return new SingleValueResponse<>(grid.getEnergyChargedToStorage().floatValue());
  }

  @GetMapping("/api/grid/main/forward")
  public SingleValueResponse<Float> getGridForward() {
    var grid = energyGridQuery.getEnergyGrid();
    return new SingleValueResponse<>(grid.getForwardEnergy().floatValue());
  }

  @GetMapping("/api/grid/main/reverse")
  public SingleValueResponse<Float> getGridReverse() {
    var grid = energyGridQuery.getEnergyGrid();
    return new SingleValueResponse<>(grid.getReverseEnergy().floatValue());
  }

//  @PostMapping("/api/grid/main/readings")
//  public ReadingsResponse registerGridReadings(@RequestBody ReadingsDto readings) {
//    registerGridReadingsUseCase.register(
//        BigDecimal.valueOf(readings.getGridForwardEnergy()),
//        BigDecimal.valueOf(readings.getGridReverseEnergy())
//    );
//    var grid = energyGridQuery.getEnergyGrid();
//    return new ReadingsResponse(
//        grid.getEnergyChargedToStorage().setScale(4, RoundingMode.HALF_UP).floatValue(),
//        grid.getEnergyUsedFromStorage().setScale(4, RoundingMode.HALF_UP).floatValue(),
//        grid.getReverseEnergy().setScale(4, RoundingMode.HALF_UP).floatValue(),
//        grid.getForwardEnergy().setScale(4, RoundingMode.HALF_UP).floatValue()
//    );
//  }
}
