package com.mitrais.atm_simulation.util;

import java.util.concurrent.ThreadLocalRandom;

public class NumberRandomizer {

	public static String getRandomNumber(int digit) {
		StringBuilder sb = new StringBuilder(digit);
		for (int i = 0; i < digit; i++)
			sb.append((char) ('0' + ThreadLocalRandom.current().nextInt(10)));
		return sb.toString();
	}

}
