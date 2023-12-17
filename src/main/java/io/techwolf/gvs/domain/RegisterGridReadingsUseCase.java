package io.techwolf.gvs.domain;

import java.math.BigDecimal;

public interface RegisterGridReadingsUseCase {
  void register(BigDecimal forwardEnergy, BigDecimal reverseEnergy);
}
