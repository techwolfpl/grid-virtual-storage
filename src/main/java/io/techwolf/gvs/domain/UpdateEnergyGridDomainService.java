package io.techwolf.gvs.domain;

import io.techwolf.gvs.domain.grid.port.EnergyGridPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEnergyGridDomainService implements UpdateEnergyGridUseCase {

  private final EnergyGridPersistencePort energyGridPersistencePort;

  @Override
  public void update(EnergyGrid energyGrid) {
    energyGridPersistencePort.persist(energyGrid);
  }
}
