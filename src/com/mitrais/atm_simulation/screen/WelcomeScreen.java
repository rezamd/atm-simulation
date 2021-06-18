package com.mitrais.atm_simulation.screen;

import com.mitrais.atm_simulation.enumerator.*;
import com.mitrais.atm_simulation.exception.UnauthorizedException;
import com.mitrais.atm_simulation.service.LoginService;
import com.mitrais.atm_simulation.validator.NumberValidator;

import static com.mitrais.atm_simulation.constant.AtmMachineConstants.LoginValidationConstant.*;

public class WelcomeScreen extends Screen {
	LoginService loginService;

	public WelcomeScreen(LoginService loginService) {
		super();
		this.loginService = loginService;
	}

	@Override
	public ScreenTypeEnum displayScreen() {
		String inputedLoginAccountNumber = showAccountNumberInput();
		String currentinputedPin = showPinInput();
		try {
			Screen.setLoggedInAccount(loginService
					.authenticateUser(inputedLoginAccountNumber, currentinputedPin));
			return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
		} catch (UnauthorizedException e) {
			System.out.println("Invalid Account Number/PIN");
			return ScreenTypeEnum.WELCOME_SCREEN;
		}
	}

	public static String showAccountNumberInput() {
		String inputedLoginAccountNumber;
		System.out.print("Enter Account Number: ");
		inputedLoginAccountNumber = Screen.scanner.nextLine();
		boolean isLoginAccountNumberValid = NumberValidator.validateNumberAndLength(inputedLoginAccountNumber,
				"Account Number", ACCOUNT_NUMBER_LENGTH);
		if (!isLoginAccountNumberValid)
			return showAccountNumberInput();
		return inputedLoginAccountNumber;
	}

	public static String showPinInput() {
		String currentinputedPin;
		System.out.print("Enter PIN: ");
		currentinputedPin = Screen.scanner.nextLine();
		boolean isLoginPinValid = NumberValidator.validateNumberAndLength(currentinputedPin, "PIN", PIN_LENGTH);
		if (!isLoginPinValid)
			return showPinInput();
		return currentinputedPin;
	}

}
