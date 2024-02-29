package io.techwolf.gvs.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = "storages")
@Table(name = "ENERGY_GRID")
public class EnergyGridEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  @Column(scale = 10)
  private BigDecimal forwardEnergy;
  @Column(scale = 10)
  private BigDecimal reverseEnergy;
  @Column(scale = 10)
  private BigDecimal realForwardEnergy;
  @Column(scale = 10)
  private BigDecimal realReverseEnergy;
  @Column(scale = 10)
  private BigDecimal energyChargedToStorage;
  @Column(scale = 10)
  private BigDecimal energyUsedFromStorage;
  @Column
  private Boolean reportingEnabled;
  @Column
  private BigDecimal currentPeriodBalanceSum;
  @Column
  private LocalDateTime currentPeriodEnd;


  @OneToMany(cascade = CascadeType.ALL, mappedBy = "energyGrid")
  private List<EnergyStorageEntity> storages;

  public EnergyGridEntity(UUID id) {
    this.id = id;
    this.forwardEnergy = BigDecimal.ZERO;
    this.reverseEnergy = BigDecimal.ZERO;
    this.realForwardEnergy = BigDecimal.ZERO;
    this.realReverseEnergy = BigDecimal.ZERO;
    this.energyChargedToStorage = BigDecimal.ZERO;
    this.energyUsedFromStorage = BigDecimal.ZERO;
    this.currentPeriodBalanceSum = BigDecimal.ZERO;
    this.reportingEnabled = false;
    this.currentPeriodEnd = null;
  }
}
