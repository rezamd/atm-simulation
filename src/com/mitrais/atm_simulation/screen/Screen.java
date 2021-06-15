package com.mitrais.atm_simulation.screen;

import com.mitrais.atm_simulation.enumerator.*;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.service.AccountService;
import com.mitrais.atm_simulation.service.LoginService;

public abstract class Screen {
	private static AccountRepository accountRepository = new AccountRepository();
	protected static LoginService loginService = new LoginService(accountRepository);
	protected static AccountService accountService = new AccountService(accountRepository);

	public static Screen getScreen(ScreenTypeEnum screenType) {
		switch (screenType) {
		case TRANSACTION_MAIN_SCREEN:
			return new TransactionMainScreen();
		case WITHDRAWAL_MAIN_SCREEN:
			return new WithdrawalMainScreen(accountService);
		case WELCOME_SCREEN:
		default:
			return new WelcomeScreen(loginService);
		}
	}

	public abstract ScreenTypeEnum displayScreen();

}
