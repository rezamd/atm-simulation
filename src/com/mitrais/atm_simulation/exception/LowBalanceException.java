package com.mitrais.atm_simulation.exception;

import java.math.BigDecimal;

public class LowBalanceException extends Exception {
	private static final long serialVersionUID = 5019363138865980771L;

	public LowBalanceException(BigDecimal balance) {
		super("Insufficient balance $" + balance);
	}
}
