package com.mitrais.atm_simulation.main;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Random;
import java.util.Scanner;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.model.FundTransfer;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.screen.Screen;
import com.mitrais.atm_simulation.validator.NumberValidator;
public class Main {
	public static Scanner scanner;
	private static AccountRepository accountRepo;
	private static Random random = new Random();
	public static Account loggedInAccount;
	private static Screen screen;
	
	public static void main(String[] args) {
		accountRepo = new AccountRepository();
		scanner = new Scanner(System.in);
		screen = Screen.getScreen(ScreenTypeEnum.WELCOME_SCREEN);
		while (true) {
			navigateToScreen(ScreenTypeEnum.WELCOME_SCREEN);
		}
	}
	
	public static void navigateToScreen(ScreenTypeEnum screenType) {
		synchronized (Screen.class) {
			Main.screen = Screen.getScreen(screenType);
			ScreenTypeEnum nextScreen = screen.displayScreen();
			EnumSet<ScreenTypeEnum> migratedScreen = EnumSet.of(ScreenTypeEnum.WELCOME_SCREEN,
					ScreenTypeEnum.TRANSACTION_MAIN_SCREEN, ScreenTypeEnum.WITHDRAWAL_MAIN_SCREEN);
			if (migratedScreen.contains(nextScreen))
				navigateToScreen(nextScreen);
			else {
				switch (nextScreen) {
				case TRANSFER_FUND_MAIN_SCREEN:
					showFundTransferScreen(loggedInAccount);
					break;
				default:
					break;
				}
			}
		}

	}

	private static boolean checkBalanceSufficient(BigDecimal withdrawAmount, BigDecimal balance) {
		if (balance.compareTo(withdrawAmount) < 0) {
			System.out.println("Insufficient balance $" + balance);
			return false;
		}
		return true;
	}

	private static boolean showFundTransferScreen(Account loggedInAccount) {
		boolean isExit = false;
		boolean isDiplayTransferScreen = true;
		do {
			System.out.println(
					"Please enter destination account and press enter to continue or  press enter to go back to Transaction:");
			String trasferDestinationInput = Main.scanner.nextLine();
			if (trasferDestinationInput.isEmpty())
				break;
			if (!NumberValidator.isNumber(trasferDestinationInput)
					|| loggedInAccount.getAccountNumber().equals(trasferDestinationInput)) {
				System.out.println("Invalid account");
				continue;

			}
			try {
				Account targetAccount = accountRepo.findById(trasferDestinationInput);

				FundTransfer fundTransfer = new FundTransfer();
				fundTransfer.setSourceAccount(loggedInAccount);
				fundTransfer.setDestinationaccount(targetAccount);
				boolean isAmountValid = showTransferAmountScreen(fundTransfer);
				if (isAmountValid) {
					fundTransfer.setReferenceNumber(getRandomNumber(6));
					System.out.printf("Reference Number: %s press enter to continue \n",
							fundTransfer.getReferenceNumber());
					Main.scanner.nextLine();
					System.out.printf("Transfer Confirmation \nDestination Account : %s \n",
							fundTransfer.getDestinationaccount().getAccountNumber());
					System.out.printf("Transfer Amount     : $%s \n", fundTransfer.getAmount());
					System.out.printf("Reference Number    : %s \n", fundTransfer.getReferenceNumber());
					System.out.println("1. Confirm Trx \n2. Cancel Trx \nChoose option[2]: ");
					String transferOption = Main.scanner.nextLine();
					switch (transferOption) {
					case "1":
						String fundTransferSummaryOption = transfer(fundTransfer);
						if (fundTransferSummaryOption.isEmpty()) {
							isExit = true;
							break;
						}
						switch (fundTransferSummaryOption) {
						case "1":
							isDiplayTransferScreen = false;
							break;
						case "2":
							isDiplayTransferScreen = false;
							isExit = true;
							break;
						default:
							break;
						}
						break;

					default:
						break;
					}

				}

			} catch (NoDataFoundException e) {
				System.out.println("Invalid account");
				continue;
			}
		} while (isDiplayTransferScreen);
		return isExit;
	}

	public static String transfer(FundTransfer fundTransfer) throws NoDataFoundException {
		fundTransfer.getSourceAccount()
				.setBalance(fundTransfer.getSourceAccount().getBalance().subtract(fundTransfer.getAmount()));
		fundTransfer.getDestinationaccount()
				.setBalance(fundTransfer.getDestinationaccount().getBalance().add(fundTransfer.getAmount()));
		loggedInAccount = accountRepo.update(fundTransfer.getSourceAccount());
		accountRepo.update(fundTransfer.getDestinationaccount());
		System.out.printf("Fund Transfer Summary \nDestination Account : %s \n",
				fundTransfer.getDestinationaccount().getAccountNumber());
		System.out.printf("Transfer Amount     : $%s \n", fundTransfer.getAmount());
		System.out.printf("Reference Number    : %s \n", fundTransfer.getReferenceNumber());
		System.out.printf("Balance     : $%s \n", loggedInAccount.getBalance());
		System.out.println("1. Transaction \n2. Exit \nChoose option[2]: ");
		String fundTransferSummaryOption = Main.scanner.nextLine();
		return fundTransferSummaryOption;
	}

	public static String getRandomNumber(int digit) {
		StringBuilder sb = new StringBuilder(digit);
		for (int i = 0; i < digit; i++)
			sb.append((char) ('0' + random.nextInt(10)));
		return sb.toString();
	}

	private static boolean showTransferAmountScreen(FundTransfer fundTransfer) {
		boolean isDisplayTransferAmountScreen = true;
		boolean isAmountValid = false;
		BigDecimal maxTransferAmount = new BigDecimal(1000);
		BigDecimal minTransferAmount = new BigDecimal(1);

		do {
			System.out.println(
					"Please enter transfer amount and  \npress enter to continue or  \npress enter to go back to Transaction: ");
			String inputedAmount = Main.scanner.nextLine();
			BigDecimal inputedAmountNumber;
			if (inputedAmount.isEmpty())
				break;
			if (!NumberValidator.isNumber(inputedAmount)) {
				System.out.println("Invalid amount");
				continue;
			} else {
				inputedAmountNumber = new BigDecimal(inputedAmount);
			}
			if (NumberValidator.isMoreThan(inputedAmountNumber, maxTransferAmount)) {
				System.out.printf("Maximum amount to withdraw is $%s\n", maxTransferAmount);
				continue;
			}
			if (NumberValidator.isLessThan(inputedAmountNumber, minTransferAmount)) {
				System.out.printf("Minimum amount to withdraw is $%s\n", minTransferAmount);
				continue;
			}
			if (!checkBalanceSufficient(inputedAmountNumber, fundTransfer.getSourceAccount().getBalance())) {
				continue;
			}
			isAmountValid = true;
			isDisplayTransferAmountScreen = false;
			fundTransfer.setAmount(inputedAmountNumber);
		} while (isDisplayTransferAmountScreen);
		return isAmountValid;
	}
}
