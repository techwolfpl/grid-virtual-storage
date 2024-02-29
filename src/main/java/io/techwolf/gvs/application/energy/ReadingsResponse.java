package io.techwolf.gvs.application.energy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadingsResponse {
  private Float energyChargedToStorage;
  private Float energyUsedFromStorage;
  private Float reverseEnergy;
  private Float forwardEnergy;
}
