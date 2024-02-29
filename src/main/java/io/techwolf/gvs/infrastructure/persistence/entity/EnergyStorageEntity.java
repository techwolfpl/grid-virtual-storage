package io.techwolf.gvs.infrastructure.persistence.entity;

import io.techwolf.gvs.domain.EnergyStorage;
import jakarta.persistence.Column;
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
@EqualsAndHashCode(exclude = "energyGrid")
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
  @Column(scale = 10)
  private BigDecimal energy;
  @Column(scale = 10)
  private BigDecimal chargeEfficiencyFactor;

  @ManyToOne
  @JoinColumn
  private EnergyGridEntity energyGrid;

  public EnergyStorageEntity(
    UUID id,
    String name,
    EnergyGridEntity grid,
    LocalDate chargedFrom,
    LocalDate chargedTo,
    LocalDate dischargedFrom,
    LocalDate dischargedTo
  ) {
    this.id = id;
    this.name = name;
    this.energyGrid = grid;
    this.energy = BigDecimal.ZERO;
    this.chargeEfficiencyFactor = new BigDecimal(0.8);
    this.chargedFrom = chargedFrom;
    this.chargedTo = chargedTo;
    this.dischargedFrom = dischargedFrom;
    this.dischargedTo = dischargedTo;
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
