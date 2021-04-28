package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class Account {
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
	public String getPin() {
		return pin;
	}
}
