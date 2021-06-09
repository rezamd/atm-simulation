package com.mitrais.atm_simulation.service;

import java.math.BigDecimal;

import com.mitrais.atm_simulation.exception.LowBalanceException;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.validator.NumberValidator;

public class AccountService {
	AccountRepository accountRepository;
	
	public AccountService(AccountRepository accountRepository){
		this.accountRepository = accountRepository;
	}
	
	private void checkBalanceSufficient(BigDecimal withdrawAmount, BigDecimal balance) throws LowBalanceException {
		if (NumberValidator.isLessThan(balance, withdrawAmount)) {
			throw new LowBalanceException(balance);
		}
	}
	
	public Account withdraw(String accountNumber, BigDecimal amount) throws NoDataFoundException, LowBalanceException{
		Account account = accountRepository.findById(accountNumber);
		checkBalanceSufficient(amount, account.getBalance());
		account.setBalance(account.getBalance().subtract(amount));
		return accountRepository.update(account);
	}
}
