package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class FundTransfer {
	private Account sourceAccount;
	private Account destinationaccount;
	private BigDecimal amount;
	private String referenceNumber;
	
	public Account getSourceAccount() {
		return sourceAccount;
	}
	public void setSourceAccount(Account sourceAccount) {
		this.sourceAccount = sourceAccount;
	}
	public Account getDestinationaccount() {
		return destinationaccount;
	}
	public void setDestinationaccount(Account destinationaccount) {
		this.destinationaccount = destinationaccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

}
