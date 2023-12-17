package io.techwolf.gvs.infrastructure.persistence.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyGridRepository extends JpaRepository<EnergyGridEntity, UUID> {

}
