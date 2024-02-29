package io.techwolf.gvs.domain;

import java.math.BigDecimal;
import java.util.HashMap;

public interface RegisterGridReadingsUseCase {

  HashMap<String, String> registerForward(BigDecimal forwardEnergy);

  HashMap<String, String> registerReverse(BigDecimal reverseEnergy);
}
