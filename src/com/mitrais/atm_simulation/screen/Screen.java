package com.mitrais.atm_simulation.screen;

import java.util.Scanner;

import com.mitrais.atm_simulation.enumerator.*;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.service.AccountService;
import com.mitrais.atm_simulation.service.LoginService;

public abstract class Screen {
	private static AccountRepository accountRepository = new AccountRepository();
	private static LoginService loginService = new LoginService(accountRepository);
	private static AccountService accountService = new AccountService(accountRepository);
	protected static Account loggedInAccount;
	protected static Scanner scanner = new Scanner(System.in);

	public static Screen getScreen(ScreenTypeEnum screenType) {
		switch (screenType) {
		case TRANSACTION_MAIN_SCREEN:
			return new TransactionMainScreen();
		case WITHDRAWAL_MAIN_SCREEN:
			return new WithdrawalMainScreen(accountService);
		case TRANSFER_FUND_MAIN_SCREEN:
			return new FundTransferMainScreen(accountService);
		case WELCOME_SCREEN:
		default:
			loggedInAccount = null;
			return new WelcomeScreen(loginService);
		}
	}


	public abstract ScreenTypeEnum displayScreen();

}
