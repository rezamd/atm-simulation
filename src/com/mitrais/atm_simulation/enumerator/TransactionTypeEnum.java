package com.mitrais.atm_simulation.enumerator;

public enum TransactionTypeEnum {
	WITHDRAWAL("WD", "Fund Withdrawal"), 
	FUNDTRANSFER("FT", "Fund Transfer");

	private String code;
	private String description;

	TransactionTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
