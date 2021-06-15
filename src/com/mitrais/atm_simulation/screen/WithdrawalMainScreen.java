package com.mitrais.atm_simulation.screen;

import static com.mitrais.atm_simulation.constant.AtmMachineConstants.WithDrawalConstant.WITHDRAWAL_AMOUNT_MAP;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.exception.LowBalanceException;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.main.Main;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.service.AccountService;
import com.mitrais.atm_simulation.validator.NumberValidator;

public class WithdrawalMainScreen extends Screen {
	AccountService accountService;

	public WithdrawalMainScreen(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public ScreenTypeEnum displayScreen() {
		String selectedAccountNunmber = Main.loggedInAccount.getAccountNumber();

		System.out.print("1. $10 \n2. $50 \n3. $100 \n4. Other \n5. Back \nPlease choose option[5]:");
		String selectedAmount = Main.scanner.nextLine();

		if (selectedAmount.isEmpty()) {
			return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
		}


		BigDecimal inputedAmount = WITHDRAWAL_AMOUNT_MAP.get(selectedAmount);
		try {
			switch (selectedAmount) {
			case "1":
			case "2":
			case "3":
				break;
			case "4":
				inputedAmount = showOtherAmountInput();
				break;
			case "5":
				return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
			default:
				return displayScreen();
			}
			Main.loggedInAccount.setBalance(accountService.withdraw(selectedAccountNunmber, inputedAmount).getBalance());
		} catch (LowBalanceException e) {
			System.out.println(e.getMessage());
			return displayScreen();
		} catch (NoDataFoundException e) {
			System.out.println(e.getMessage());
			return displayScreen();
		}
		return showWithdrawSummary(Main.loggedInAccount, inputedAmount);
	}

	private BigDecimal showOtherAmountInput() {
		final BigDecimal maxWithdrawAmount = new BigDecimal(1000);
		final int multiplier = 10;
		System.out.print("Other Withdraw \nEnter amount to withdraw: ");
		String inputedAmount = Main.scanner.nextLine();

		BigDecimal inputedAmountNumber = NumberValidator.isNumber(inputedAmount) ? new BigDecimal(inputedAmount)
				: BigDecimal.ZERO;
		if (!NumberValidator.isNumber(inputedAmount)
				|| !NumberValidator.isMultiplierOf(inputedAmountNumber, multiplier)) {
			System.out.println("Invalid ammount");
			return showOtherAmountInput();
		} else if (NumberValidator.isMoreThan(inputedAmountNumber, maxWithdrawAmount)) {
			System.out.println("Maximum amount to withdraw is $" + maxWithdrawAmount);
			return showOtherAmountInput();
		}
		return BigDecimal.ZERO;
	}
	
	private ScreenTypeEnum showWithdrawSummary(Account loggedInAccount, BigDecimal withdrawAmmount) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		System.out.printf(
				"Summary \nDate : %s \nWithdraw : $%s \nBalance : $%s \n1. Transaction  \n2. Exit Choose option[2]:",
				currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")), withdrawAmmount,
				loggedInAccount.getBalance());
		String selection = Main.scanner.nextLine();
		if (selection.equalsIgnoreCase("1")) {
			return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
		} else {
			return ScreenTypeEnum.WELCOME_SCREEN;
		}

	}

}
