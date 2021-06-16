package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class FundTransfer {
	private String sourceAccount;
	private String destinationaccount;
	private BigDecimal amount;
	private String referenceNumber;
	
	public String getSourceAccount() {
		return sourceAccount;
	}
	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}
	public String getDestinationaccount() {
		return destinationaccount;
	}
	public void setDestinationaccount(String destinationaccount) {
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
