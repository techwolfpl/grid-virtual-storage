package io.techwolf.gvs.application.energy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EnergyGridDto {

  private UUID id;
  private String name;
  private Float forwardEnergy;
  private Float reverseEnergy;
  private Float realForwardEnergy;
  private Float realReverseEnergy;
  private Float energyChargedToStorage;
  private Float energyUsedFromStorage;
  private List<EnergyStorageDto> storages;
  private Boolean reportingEnabled;
  private Float currentPeriodBalanceSum;
  private LocalDateTime currentPeriodEnd;
}
