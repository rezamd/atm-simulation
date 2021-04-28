package com.mitrais.atm_simulation.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mitrais.atm_simulation.model.Account;

public class InMemoryAccountRepository {
	List<Account> accounts = initializedData();
	
	public List<Account> initializedData() {
		List<Account> accounts = new ArrayList<>();
		Account firstAccount = new Account("John Doe", "012108", new BigDecimal(100), "112233");
		Account secondAccount = new Account("Jane Doe", "932012", new BigDecimal(30), "112244");
		accounts.add(firstAccount);
		accounts.add(secondAccount);
		return accounts;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
}
