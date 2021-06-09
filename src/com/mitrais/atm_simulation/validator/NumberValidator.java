package com.mitrais.atm_simulation.validator;

import java.math.BigDecimal;

public class NumberValidator {

	public static boolean validateNumberAndLength(String input, String fieldName, int length) {
		boolean result = true;
		if (!NumberValidator.isNumber(input)) {
			System.out.println(fieldName + " should only contains numbers");
			result = false;
		} else if (!input.matches("^[0-9]{"+length+"}$")) {
			System.out.println(fieldName + " should have 6 digits length");
			result = false;
		}
	
		return result;
	}

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

	public static boolean isNumber(String input) {
		return input.matches("^[0-9]+[0-9]*$");
	}

}
