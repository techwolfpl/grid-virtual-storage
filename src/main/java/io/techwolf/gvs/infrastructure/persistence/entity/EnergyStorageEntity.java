package io.techwolf.gvs.infrastructure.persistence.entity;

import io.techwolf.gvs.domain.EnergyStorage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Setter
@EqualsAndHashCode
@Table(name = "ENERGY_STORAGE")
public class EnergyStorageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private LocalDate chargedFrom;
  private LocalDate chargedTo;
  private LocalDate dischargedFrom;
  private LocalDate dischargedTo;
  private BigDecimal energy;
  private BigDecimal chargeEfficiencyFactor;

  @ManyToOne
  @JoinColumn
  private EnergyGridEntity energyGrid;

  public EnergyStorageEntity(UUID id, EnergyGridEntity grid) {
    this.id = id;
    this.energyGrid = grid;
  }

  public EnergyStorageEntity update(EnergyStorage e) {
    name = e.getName();
    chargedFrom = e.getChargedFrom();
    chargedTo = e.getChargedTo();
    dischargedFrom = e.getDischargedFrom();
    dischargedTo = e.getDischargedTo();
    energy = e.getEnergy();
    chargeEfficiencyFactor = e.getChargeEfficiencyFactor();
    return this;
  }
}
