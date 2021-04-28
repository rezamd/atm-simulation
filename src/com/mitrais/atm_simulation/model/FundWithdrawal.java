package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class FundWithdrawal extends AccountTransaction {
	String accountNo;
	BigDecimal amount;
	String kioskID;
}
