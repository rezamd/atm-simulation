package com.mitrais.atm_simulation.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.Account;

public class AccountRepository implements IBaseRepository<Account, String> {
	List<Account> accounts; 
	
	public AccountRepository() {
		super();
		initializedData();
	}

	private void initializedData() {
		this.accounts = new ArrayList<>();
		Account firstAccount = new Account("John Doe", "012108", new BigDecimal(100), "112233");
		Account secondAccount = new Account("Jane Doe", "932012", new BigDecimal(30), "112244");
		accounts.add(firstAccount);
		accounts.add(secondAccount);
	}

	public List<Account> findAll() {
		return this.accounts;
	}
	
	public List<Account> findCustomerAccount(String inputedAccountNumber,
			String inputedPin) {
		return this.accounts.stream().filter(account -> account.getAccountNumber().equals(inputedAccountNumber)
				&& account.getPin().equals(new String(inputedPin))).collect(Collectors.toList());
	}

	@Override
	public Account findById(String accountNumber) throws NoDataFoundException {
		return this.accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber))
				.findFirst().orElseThrow(()-> new NoDataFoundException("No Data Found"));
	}
}
