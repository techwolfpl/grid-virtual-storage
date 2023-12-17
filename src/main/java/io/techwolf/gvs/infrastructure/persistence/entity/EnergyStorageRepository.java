package io.techwolf.gvs.infrastructure.persistence.entity;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface EnergyStorageRepository extends CrudRepository<EnergyStorageEntity, UUID> {

}
