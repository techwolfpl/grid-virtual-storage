package io.techwolf.gvs.infrastructure.persistence.entity;

import io.techwolf.gvs.domain.EnergyGrid;
import io.techwolf.gvs.domain.grid.port.EnergyGridProviderPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnergyGridProviderAdapter implements EnergyGridProviderPort {
  EnergyGridRepository energyGridRepository;
  PersistenceMapper mapper;

  @Override
  public EnergyGrid getEnergyGrid() {
    var grid = energyGridRepository.findAll().stream().findFirst();
    if (grid.isPresent()) {
      return grid.map(g -> mapper.map(g)).get();
    }
    //TODO: for now this is kind of get or create, until grid management would be implemented
    return mapper.map(
      energyGridRepository.save(
        new EnergyGridEntity(
          UUID.randomUUID(),
          "main",
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          BigDecimal.ZERO,
          List.of()
        )
      )
    );
  }
}
