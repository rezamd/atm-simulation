package com.mitrais.atm_simulation.utils;

public class StringUtils {

	public static boolean isNumber(String input) {
		return input.matches("^[0-9]+[0-9]*$");
	}

	public static boolean validateNumberAndLength(String input, String fieldName) {
		boolean result = true;
		if (!isNumber(input)) {
			System.out.println(fieldName + " should only contains numbers");
			result = false;
		} else if (!input.matches("^[0-9]{6}$")) {
			System.out.println(fieldName + " should have 6 digits length");
			result = false;
		}
	
		return result;
	}

}
