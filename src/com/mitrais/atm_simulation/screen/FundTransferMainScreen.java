package com.mitrais.atm_simulation.screen;

import java.math.BigDecimal;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.exception.CancelledInputException;
import com.mitrais.atm_simulation.exception.LowBalanceException;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.FundTransfer;
import com.mitrais.atm_simulation.service.AccountService;
import com.mitrais.atm_simulation.util.NumberRandomizer;
import com.mitrais.atm_simulation.validator.NumberValidator;

public class FundTransferMainScreen extends Screen {
	AccountService accountService;

	public FundTransferMainScreen(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	@Override
	public ScreenTypeEnum displayScreen() {

			System.out.println(
					"Please enter destination account and press enter to continue or  press enter to go back to Transaction:");
			String trasferDestinationInput = Screen.scanner.nextLine();
			if (trasferDestinationInput.isEmpty())
				return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
			if (!NumberValidator.isNumber(trasferDestinationInput)
					|| Screen.loggedInAccount.getAccountNumber().equals(trasferDestinationInput)) {
				System.out.println("Invalid account");
				return displayScreen();

			}
			FundTransfer fundTransfer = new FundTransfer();
			try {

				fundTransfer.setSourceAccount(Screen.loggedInAccount.getAccountNumber());
				fundTransfer.setDestinationaccount(trasferDestinationInput);
				boolean isAmountValid = showTransferAmountInput(fundTransfer);
				if (isAmountValid) {
					fundTransfer.setReferenceNumber(NumberRandomizer.getRandomNumber(6));
					System.out.printf("Reference Number: %s press enter to continue %n",
							fundTransfer.getReferenceNumber());
					Screen.scanner.nextLine();
					System.out.printf("Transfer Confirmation %nDestination Account : %s %n",
							fundTransfer.getDestinationaccount());
					System.out.printf("Transfer Amount     : $%s %n", fundTransfer.getAmount());
					System.out.printf("Reference Number    : %s %n", fundTransfer.getReferenceNumber());
					System.out.println("1. Confirm Trx \n2. Cancel Trx \nChoose option[2]: ");
					String transferOption = Screen.scanner.nextLine();
					if("1".equals(transferOption)) {
						Screen.loggedInAccount.setBalance(accountService.fundTransfer(fundTransfer.getSourceAccount(), fundTransfer.getDestinationaccount(), fundTransfer.getAmount()).getBalance());
					}else{
						return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
					}

				}

			} catch (NoDataFoundException e) {
				System.out.println("Invalid account");
				return displayScreen();
			} catch (CancelledInputException e) {
				return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
			} catch (LowBalanceException e) {
				System.out.println(e.getMessage());
				return displayScreen();
			}
			return transferSummary(fundTransfer);
	}

	private boolean showTransferAmountInput(FundTransfer fundTransfer) throws CancelledInputException {
		boolean isAmountValid = false;
		BigDecimal maxTransferAmount = new BigDecimal(1000);
		BigDecimal minTransferAmount = new BigDecimal(1);

		System.out.println(
				"Please enter transfer amount and  \npress enter to continue or  \npress enter to go back to Transaction: ");
		String inputedAmount = Screen.scanner.nextLine();
		BigDecimal inputedAmountNumber;
		if (inputedAmount.isEmpty())
			throw new CancelledInputException();
		if (!NumberValidator.isNumber(inputedAmount)) {
			System.out.println("Invalid amount");
			return showTransferAmountInput(fundTransfer);
		} else {
			inputedAmountNumber = new BigDecimal(inputedAmount);
		}
		if (NumberValidator.isMoreThan(inputedAmountNumber, maxTransferAmount)) {
			System.out.printf("Maximum amount to withdraw is $%s%n", maxTransferAmount);
			return showTransferAmountInput(fundTransfer);
		}
		if (NumberValidator.isLessThan(inputedAmountNumber, minTransferAmount)) {
			System.out.printf("Minimum amount to withdraw is $%s%n", minTransferAmount);
			return showTransferAmountInput(fundTransfer);
		}
		isAmountValid = true;
		fundTransfer.setAmount(inputedAmountNumber);
		return isAmountValid;
	}
	
	public ScreenTypeEnum transferSummary(FundTransfer fundTransfer){
		System.out.printf("Fund Transfer Summary %nDestination Account : %s %n",
				fundTransfer.getDestinationaccount());
		System.out.printf("Transfer Amount     : $%s %n", fundTransfer.getAmount());
		System.out.printf("Reference Number    : %s %n", fundTransfer.getReferenceNumber());
		System.out.printf("Balance     : $%s %n", Screen.loggedInAccount.getBalance());
		System.out.println("1. Transaction \n2. Exit \nChoose option[2]: ");
		String fundTransferSummaryOption = Screen.scanner.nextLine();
		if (fundTransferSummaryOption.equalsIgnoreCase("1")) {
			return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
		} else {
			return ScreenTypeEnum.WELCOME_SCREEN;
		}
	}
}
