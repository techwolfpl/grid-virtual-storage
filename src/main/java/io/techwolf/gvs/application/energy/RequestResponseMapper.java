package io.techwolf.gvs.application.energy;

import io.techwolf.gvs.domain.EnergyGrid;
import io.techwolf.gvs.domain.EnergyStorage;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RequestResponseMapper {

  EnergyGrid map(EnergyGridDto e);

  EnergyGridDto map(EnergyGrid e);

  EnergyStorageDto map(EnergyStorage e);

  EnergyStorage map(EnergyStorageDto e);
}
