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
	
	public Account fundTransfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount)
			throws LowBalanceException, NoDataFoundException {
		Account sourceAccount = accountRepository.findById(sourceAccountNumber);
		checkBalanceSufficient(amount, sourceAccount.getBalance());
		Account destinationAccount = accountRepository.findById(destinationAccountNumber);
		sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
		destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
		try {
			accountRepository.update(sourceAccount);
			accountRepository.update(destinationAccount);
		} catch (Exception e) {
			sourceAccount.setBalance(sourceAccount.getBalance().add(amount));
			destinationAccount.setBalance(destinationAccount.getBalance().subtract(amount));
			accountRepository.update(sourceAccount);
			accountRepository.update(destinationAccount);
		}
		return sourceAccount;
	}
}
