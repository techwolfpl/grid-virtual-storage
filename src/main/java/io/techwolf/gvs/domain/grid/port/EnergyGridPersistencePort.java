package io.techwolf.gvs.domain.grid.port;

import io.techwolf.gvs.domain.EnergyGrid;

public interface EnergyGridPersistencePort {
  void persist(EnergyGrid energyGrid);
}
