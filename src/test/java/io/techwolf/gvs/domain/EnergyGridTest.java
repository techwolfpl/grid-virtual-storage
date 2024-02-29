package io.techwolf.gvs.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnergyGridTest {

  @Test
  public void testPeriodDates() {
    EnergyGrid grid = EnergyGrid.builder()
        .energyChargedToStorage(BigDecimal.ZERO)
        .forwardEnergy(BigDecimal.ZERO)
        .energyUsedFromStorage(BigDecimal.ZERO)
        .realForwardEnergy(BigDecimal.ZERO)
        .realReverseEnergy(BigDecimal.ZERO)
        .reverseEnergy(BigDecimal.ZERO)
        .storages(List.of(EnergyStorage.builder()
            .dischargedFrom(LocalDate.now().minusYears(1))
            .dischargedTo(LocalDate.now().plusYears(1))
            .chargedFrom(LocalDate.now().minusYears(1))
            .chargedTo(LocalDate.now().plusYears(1))
            .energy(BigDecimal.ZERO)
            .chargeEfficiencyFactor(BigDecimal.valueOf(0.8))
            .build()
        ))
        .build();

    grid.setCurrentPeriodEnd(LocalDateTime.of(2024, 1, 17, 22, 00));
    grid.periodicalBalancing(BigDecimal.ONE, BigDecimal.ONE, LocalDateTime.of(2024, 1, 17, 21, 04), false);
    Assertions.assertEquals(BigDecimal.ZERO, grid.getCurrentPeriodBalanceSum());
    grid.periodicalBalancing(BigDecimal.ONE, BigDecimal.ZERO, LocalDateTime.of(2024, 1, 17, 21, 04), false);
    Assertions.assertEquals(BigDecimal.ONE, grid.getCurrentPeriodBalanceSum());
    grid.periodicalBalancing(BigDecimal.ONE, BigDecimal.ZERO, LocalDateTime.of(2024, 1, 17, 21, 04), false);
    Assertions.assertEquals(BigDecimal.valueOf(2), grid.getCurrentPeriodBalanceSum());
    grid.periodicalBalancing(BigDecimal.ZERO, BigDecimal.ONE, LocalDateTime.of(2024, 1, 17, 21, 04), false);
    Assertions.assertEquals(BigDecimal.ONE, grid.getCurrentPeriodBalanceSum());
    Assertions.assertEquals(BigDecimal.ZERO, grid.getForwardEnergy());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 17, 22, 00), grid.getCurrentPeriodEnd());

    grid.setCurrentPeriodEnd(LocalDateTime.of(2024, 1, 17, 22, 00));
    grid.periodicalBalancing(BigDecimal.ONE, BigDecimal.ZERO, LocalDateTime.of(2024, 1, 17, 22, 04), false);
    Assertions.assertEquals(BigDecimal.ONE, grid.getCurrentPeriodBalanceSum());
    Assertions.assertEquals(BigDecimal.ONE, grid.getForwardEnergy());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 17, 23, 00), grid.getCurrentPeriodEnd());
  }

}