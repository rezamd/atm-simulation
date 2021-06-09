package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class Account implements Cloneable{
	private String fullName;
	private String pin;
	private BigDecimal balance;
	private String accountNumber;
	
	
	public Account(String fullName, String pin, BigDecimal balance, String accountNumber) {
		super();
		this.fullName = fullName;
		this.pin = pin;
		this.balance = balance;
		this.accountNumber = accountNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public Boolean isPinMatch(String pin) {
		return this.pin.equals(pin);
	}
	
	@Override
	public Account clone() {
		Account clonedAccount = null;
		try {
			clonedAccount = (Account) super.clone();
		} catch (CloneNotSupportedException e) {
			clonedAccount = new Account(this.fullName, this.pin, this.balance, this.accountNumber);
		}
		return clonedAccount;
	}
}
