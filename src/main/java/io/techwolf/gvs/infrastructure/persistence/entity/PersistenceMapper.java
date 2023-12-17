package io.techwolf.gvs.infrastructure.persistence.entity;

import io.techwolf.gvs.domain.EnergyGrid;
import io.techwolf.gvs.domain.EnergyStorage;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PersistenceMapper {
  EnergyGridEntity map(EnergyGrid e);
  EnergyGrid map(EnergyGridEntity e);
  EnergyStorageEntity map(EnergyStorage e);
  EnergyStorage map(EnergyStorageEntity e);
}
