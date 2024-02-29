package io.techwolf.gvs.application.energy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergyStorageDto {

  private UUID id;
  private String name;
  private LocalDate chargedFrom;
  private LocalDate chargedTo;
  private LocalDate dischargedFrom;
  private LocalDate dischargedTo;
  private Float energy;
  private Float chargeEfficiencyFactor;
}