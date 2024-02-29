package io.techwolf.gvs.domain;

import io.techwolf.gvs.Settings;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
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
  private Boolean reportingEnabled;
  private BigDecimal currentPeriodBalanceSum;
  private LocalDateTime currentPeriodEnd;

  public void updateReadings(BigDecimal forwardEnergy, BigDecimal reverseEnergy, Settings options,
      boolean forcePeriodFinish) {

    var normalizedReverse =
        reverseEnergy.compareTo(BigDecimal.ZERO) < 0 ? reverseEnergy.multiply(BigDecimal.valueOf(-1L)) : reverseEnergy;

    var forwardEnergyGain = forwardEnergy.subtract(this.realForwardEnergy);
    var reverseEnergyGain = normalizedReverse.subtract(this.realReverseEnergy);
    this.realForwardEnergy = forwardEnergy;
    this.realReverseEnergy = normalizedReverse;
    log.info("Forward gain " + forwardEnergyGain);
    log.info("Reverse gain " + reverseEnergyGain);
    if (options.getPeriodicalBalancing()) {
      var now = LocalDateTime.now();
      periodicalBalancing(forwardEnergyGain, reverseEnergyGain, now, forcePeriodFinish);
    } else {
      flushStorage(forwardEnergyGain, reverseEnergyGain);
    }
  }

  void periodicalBalancing(final BigDecimal forwardEnergyGain, final BigDecimal reverseEnergyGain, LocalDateTime now,
      boolean forcePeriodFinish) {
    if (currentPeriodBalanceSum == null) {
      currentPeriodBalanceSum = BigDecimal.ZERO;
    }
    if (!forcePeriodFinish && currentPeriodEnd != null && now.isBefore(currentPeriodEnd)) {
      // we're in the period, just add do buffer
      currentPeriodBalanceSum = currentPeriodBalanceSum.add(forwardEnergyGain).subtract(reverseEnergyGain);
    } else {
      // new period, we flush current state
      if (currentPeriodBalanceSum.compareTo(BigDecimal.ZERO) > 0) {
        flushStorage(currentPeriodBalanceSum, BigDecimal.ZERO); // we have hourly consumtion, no production
      } else {
        flushStorage(BigDecimal.ZERO,
            currentPeriodBalanceSum.multiply(BigDecimal.valueOf(-1L))); // we have hourly production, no consumption
      }
      // reset period end date
      if (currentPeriodEnd == null) {
        // only if no curren tperion take it from current te
        currentPeriodEnd = now.withMinute(0).withSecond(0).withNano(0).plusHours(1);
      } else {
        // if period already began just add one hour and  so on. Also clean minutes  etc to be sure
        currentPeriodEnd = currentPeriodEnd.withMinute(0).withSecond(0).withNano(0).plusHours(1);
      }
      // initialize new balance sum
      currentPeriodBalanceSum = BigDecimal.ZERO.add(forwardEnergyGain).subtract(reverseEnergyGain);
    }
  }

  void flushStorage(BigDecimal forwardEnergyGain, BigDecimal reverseEnergyGain) {
    var today = LocalDate.now();
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
