package io.techwolf.gvs.application.energy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleValueResponse<T> {
  private T value;
}
