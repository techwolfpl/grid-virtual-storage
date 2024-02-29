package io.techwolf.gvs.application.energy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ReadingsDto {
  private Float gridForwardEnergy;
  private Float gridReverseEnergy;
}
