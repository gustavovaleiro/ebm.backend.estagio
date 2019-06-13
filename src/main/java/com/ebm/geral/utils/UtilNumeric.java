package com.ebm.geral.utils;

import java.math.BigDecimal;
import java.util.Optional;

public class UtilNumeric {
	public static  BigDecimal valueOrZero(BigDecimal value) {
		return Optional.ofNullable(value).orElse(BigDecimal.valueOf(0));
	}
	
	public static Double valueOrZero(Double value) {
		return value == null ? 0d: value;
	}
	public static Integer valueOrZero(Integer value) {
		return value == null ? 0 : value;
	}
}
