package io.techwolf.gvs.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@EqualsAndHashCode
@Table(name = "ENERGY_GRID")
public class EnergyGridEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private BigDecimal forwardEnergy;
  private BigDecimal reverseEnergy;
  private BigDecimal realForwardEnergy;
  private BigDecimal realReverseEnergy;

  @OneToMany(cascade = CascadeType.ALL)
  private List<EnergyStorageEntity> storages;

  public EnergyGridEntity(UUID id) {
    this.id = id;
  }
}
