package io.techwolf.gvs.infrastructure.persistence.entity;

import io.techwolf.gvs.domain.EnergyGrid;
import io.techwolf.gvs.domain.grid.port.EnergyGridPersistencePort;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class EnergyGridPersistenceAdapter implements EnergyGridPersistencePort {

  private EnergyGridRepository repository;

  @Override
  @Transactional
  public void persist(EnergyGrid energyGrid) {
    var gridOpt = repository.findById(energyGrid.getId());
    final var entity = gridOpt.orElse(new EnergyGridEntity());
    Map<UUID, EnergyStorageEntity> storageMap = entity
        .getStorages()
        .stream()
        .collect(Collectors.toMap(EnergyStorageEntity::getId, e -> e));

    var mappedStorages = energyGrid
        .getStorages()
        .stream()
        .map(
            s ->
                storageMap
                    .getOrDefault(
                        s.getId(),
                        new EnergyStorageEntity(
                            s.getId(),
                            s.getName(),
                            entity,
                            s.getChargedFrom(),
                            s.getChargedTo(),
                            s.getDischargedFrom(),
                            s.getDischargedTo()
                        )
                    )
                    .update(s)
        )
        .collect(Collectors.toList());

    entity.setName(energyGrid.getName());
    entity.setForwardEnergy(energyGrid.getForwardEnergy());
    entity.setReverseEnergy(energyGrid.getReverseEnergy());
    entity.setRealForwardEnergy(energyGrid.getRealForwardEnergy());
    entity.setRealReverseEnergy(energyGrid.getRealReverseEnergy());
    entity.setEnergyChargedToStorage(energyGrid.getEnergyChargedToStorage());
    entity.setEnergyUsedFromStorage(energyGrid.getEnergyUsedFromStorage());
    entity.setReportingEnabled(energyGrid.getReportingEnabled());
    entity.setCurrentPeriodBalanceSum(energyGrid.getCurrentPeriodBalanceSum());
    entity.setCurrentPeriodEnd(energyGrid.getCurrentPeriodEnd());
    entity.setStorages(mappedStorages);

    repository.save(entity);
  }
}
