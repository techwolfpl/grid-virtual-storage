package io.techwolf.gvs.domain;

import io.techwolf.gvs.domain.grid.port.EnergyGridPersistencePort;
import io.techwolf.gvs.domain.grid.port.EnergyGridProviderPort;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterGridReadingsService implements RegisterGridReadingsUseCase {
  private final EnergyGridProviderPort energyGridProviderPort;
  private final EnergyGridPersistencePort energyGridPersistencePort;

  @Override
  public void register(BigDecimal forwardEnergy, BigDecimal reverseEnergy) {
    var grid = energyGridProviderPort.getEnergyGrid();
    grid.updateReadings(forwardEnergy, reverseEnergy);
    energyGridPersistencePort.persist(grid);
  }
}
