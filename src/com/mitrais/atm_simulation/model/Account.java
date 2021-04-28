package com.mitrais.atm_simulation.model;

import java.math.BigDecimal;

public class Account implements Cloneable {
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

	public String getPin() {
		return pin;
	}

	public boolean isAccountValid() {
		if (isFullNameValid() && isPinValid() && isBalanceValid() && isAccountNumberValid())
			return true;
		else
			return false;
	}
	private boolean isAccountNumberValid() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isBalanceValid() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isPinValid() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isFullNameValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account clone() {
		Account clonedAccount = null;
		try {
			clonedAccount = (Account) super.clone();
		} catch (CloneNotSupportedException e) {
			clonedAccount = new Account(this.getFullName(), this.getPin(), this.getBalance(), this.getAccountNumber());
		}
		return clonedAccount;
	}
}
