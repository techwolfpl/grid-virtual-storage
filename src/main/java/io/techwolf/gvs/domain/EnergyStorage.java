package io.techwolf.gvs.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EnergyStorage {
  private UUID id;
  private String name;
  private LocalDate chargedFrom;
  private LocalDate chargedTo;
  private LocalDate dischargedFrom;
  private LocalDate dischargedTo;
  private BigDecimal energy;
  private BigDecimal chargeEfficiencyFactor;

  /**
   *
   * @param dischargedEnergy
   * @return how much energy was lacking in the storage
   */
  public BigDecimal discharge(BigDecimal dischargedEnergy) {
    if (energy.compareTo(dischargedEnergy) == -1) {
      var lacking = dischargedEnergy.subtract(energy);
      this.energy = BigDecimal.ZERO;
      return lacking;
    } else {
      this.energy.subtract(dischargedEnergy);
      return BigDecimal.ZERO;
    }
  }

  /**
   *
   * @param chargedEnergy
   * @return how much energy wasnt charge because of efficiency factor
   */
  public BigDecimal charge(BigDecimal chargedEnergy) {
    var energyToAdd = chargedEnergy.multiply(chargeEfficiencyFactor);
    this.energy = this.energy.add(energyToAdd);
    return chargedEnergy.subtract(energyToAdd);
  }
}
