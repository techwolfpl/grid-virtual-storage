package io.techwolf.gvs.domain;

import io.techwolf.gvs.domain.grid.port.EnergyGridProviderPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetEnergyGridService implements EnergyGridQuery {
  private EnergyGridProviderPort providerPort;

  @Override
  public EnergyGrid getEnergyGrid() {
    return providerPort.getEnergyGrid();
  }
}
