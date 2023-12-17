package io.techwolf.gvs.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class EnergyGrid {
  private UUID id;
  private String name;
  private BigDecimal forwardEnergy;
  private BigDecimal reverseEnergy;
  private BigDecimal realForwardEnergy;
  private BigDecimal realReverseEnergy;
  private BigDecimal energyChargedToStorage;
  private BigDecimal energyUsedFromStorage;
  private List<EnergyStorage> storages;

  public void updateReadings(BigDecimal forwardEnergy, BigDecimal reverseEnergy) {
    var today = LocalDate.now();
    var forwardEnergyGain = forwardEnergy.subtract(this.realForwardEnergy);
    var reverseEnergyGain = reverseEnergy.subtract(this.realReverseEnergy);
    this.realForwardEnergy = forwardEnergy;
    this.realReverseEnergy = reverseEnergy;

    var looses = getStorageForCharge(today).charge(reverseEnergyGain);
    this.reverseEnergy = this.reverseEnergy.add(looses);

    var toGetFromTheGrid = dischargeStorages(today, forwardEnergyGain);
    this.forwardEnergy = this.forwardEnergy.add(toGetFromTheGrid);

    var addedToStorage = reverseEnergyGain.subtract(looses);
    energyChargedToStorage = energyChargedToStorage.add(addedToStorage);

    var usedFromStorage = forwardEnergyGain.subtract(toGetFromTheGrid);
    energyUsedFromStorage = energyUsedFromStorage.add(usedFromStorage);
  }

  private BigDecimal dischargeStorages(LocalDate today, BigDecimal dischargingEnergy) {
    if (dischargingEnergy.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    } else if (dischargingEnergy.compareTo(BigDecimal.ZERO) == -1) {
      throw new RuntimeException("Discharging with negative energy not possible");
    } else {
      var storageToBeDischarged = getStorageForDischarge(today);
      if (storageToBeDischarged.isEmpty()) {
        return dischargingEnergy;
      } else {
        var leftToDischarge = storageToBeDischarged.get().discharge(dischargingEnergy);
        if (leftToDischarge.compareTo(BigDecimal.ZERO) == 0) {
          return BigDecimal.ZERO;
        } else {
          return this.dischargeStorages(today, leftToDischarge);
        }
      }
    }
  }

  public EnergyStorage getStorageForCharge(LocalDate today) {
    return storages
      .stream()
      .filter(s -> s.getChargedFrom().isBefore(today))
      .filter(s -> today.isBefore(s.getChargedTo()))
      .reduce(
        (a, b) -> {
          throw new RuntimeException("Multiple energy storage found for charge");
        }
      )
      .orElseThrow(
        () -> new IllegalStateException("No energy storage for charge available")
      );
  }

  public Optional<EnergyStorage> getStorageForDischarge(LocalDate today) {
    return storages
      .stream()
      .filter(s -> s.getDischargedFrom().isBefore(today))
      .filter(s -> today.isBefore(s.getDischargedTo()))
      .filter(s -> s.getEnergy().compareTo(BigDecimal.ZERO) == 1)
      .findFirst();
  }
}
