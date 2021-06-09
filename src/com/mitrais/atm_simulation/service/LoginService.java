package com.mitrais.atm_simulation.service;

import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.exception.UnauthorizedException;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.repository.AccountRepository;

public class LoginService {
	AccountRepository accountRepo;
	
	public LoginService(AccountRepository accountRepo) {
		super();
		this.accountRepo = accountRepo;
	}

	public Account authenticateUser(String inputedLoginAccountNumber, String currentinputedPin) throws UnauthorizedException {
		try {
			return accountRepo.findByIdAndPin(inputedLoginAccountNumber, currentinputedPin);
		} catch (NoDataFoundException e) {
			throw new UnauthorizedException();
		}
	}
	
	

}
