package io.techwolf.gvs.application.energy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadingsDto {
  private Double gridForwardEnergy;
  private Double gridReverseEnergy;
}
