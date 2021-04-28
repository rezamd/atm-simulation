package com.mitrais.atm_simulation.utils;

import java.math.BigDecimal;

public class NumberUtils {

	public static boolean isMultiplierOf(BigDecimal inputedAmountNumber, final int multiplier) {
		return !inputedAmountNumber.equals(BigDecimal.ZERO)
				&& inputedAmountNumber.remainder(new BigDecimal(multiplier)).compareTo(BigDecimal.ZERO) == 0;
	}

	public static boolean isMoreThan(BigDecimal inputedAmountNumber, BigDecimal maxWithdrawAmount) {
		return inputedAmountNumber.compareTo(maxWithdrawAmount) > 0;
	}

	public static boolean isLessThan(BigDecimal inputedAmountNumber, BigDecimal maxWithdrawAmount) {
		return inputedAmountNumber.compareTo(maxWithdrawAmount) < 0;
	}

}
