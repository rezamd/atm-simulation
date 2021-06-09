package com.mitrais.atm_simulation.main;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

import com.mitrais.atm_simulation.constant.AtmMachineConstants;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.exception.UnauthorizedException;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.model.FundTransfer;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.service.LoginService;
import com.mitrais.atm_simulation.validator.NumberValidator;
import static com.mitrais.atm_simulation.constant.AtmMachineConstants.LoginValidationConstant.*;
public class Main {
	public static Scanner scanner;
	private static AccountRepository accountRepo;
	private static LoginService loginService;
	private static Random random = new Random();

	public static void main(String[] args) {
		accountRepo = new AccountRepository();
		loginService = new LoginService(accountRepo);
		
		scanner = new Scanner(System.in);

		while (true) {
			String inputedLoginAccountNumber = showAccountNumberInput();
			String currentinputedPin = showPinInput();
			Account loggedInAccount;
			try {
				loggedInAccount = loginService.authenticateUser(inputedLoginAccountNumber, currentinputedPin);
			} catch (UnauthorizedException e) {
				continue;
			}

			showTransactionScreen(loggedInAccount);

		}
	}

	public static boolean showWithdrawScreen(Account loggedInAccount) {
		boolean isBackToWelcomeScreen = false;
		boolean isInWithdrawScreen;
		do {
			isInWithdrawScreen = true;
			System.out.print("1. $10 \n2. $50 \n3. $100 \n4. Other \n5. Back \nPlease choose option[5]:");
			String selectedAmount = Main.scanner.nextLine();
			isBackToWelcomeScreen = true;
			if (selectedAmount.isEmpty()) {
				isBackToWelcomeScreen = false;
				break;
			}
			switch (selectedAmount) {
			case "1":
				isBackToWelcomeScreen = checkAndProcessWithdraw(loggedInAccount, new BigDecimal(10));
				isInWithdrawScreen = false;
				break;
			case "2":
				isBackToWelcomeScreen = checkAndProcessWithdraw(loggedInAccount, new BigDecimal(50));
				isInWithdrawScreen = false;
				break;
			case "3":
				isBackToWelcomeScreen = checkAndProcessWithdraw(loggedInAccount, new BigDecimal(100));
				isInWithdrawScreen = false;
				break;
			case "4":
				BigDecimal inputedAmount = showOtherAmountScreen(loggedInAccount);
				if (inputedAmount.equals(BigDecimal.ZERO)) {
					isBackToWelcomeScreen = false;
				} else {
					isBackToWelcomeScreen = checkAndProcessWithdraw(loggedInAccount, inputedAmount);
					isInWithdrawScreen = false;
				}
				break;
			case "5":
				isBackToWelcomeScreen = false;
				isInWithdrawScreen = false;
				break;
			default:
				isBackToWelcomeScreen = false;
				break;
			}
		} while (isInWithdrawScreen);
		return isBackToWelcomeScreen;
	}

	public static boolean checkAndProcessWithdraw(Account loggedInAccount, BigDecimal withdrawAmmount) {
		boolean isBalanceSufficient = checkBalanceSufficient(withdrawAmmount, loggedInAccount.getBalance());
		boolean isBackToWelcomeScreen = false;
		if (isBalanceSufficient) {
			loggedInAccount.setBalance(loggedInAccount.getBalance().subtract(withdrawAmmount));
			isBackToWelcomeScreen = showWithdrawSummaryScreen(loggedInAccount, withdrawAmmount);
		}
		return isBackToWelcomeScreen;
	}

	private static boolean showWithdrawSummaryScreen(Account loggedInAccount, BigDecimal withdrawAmmount) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		System.out.printf(
				"Summary \nDate : %s \nWithdraw : $%s \nBalance : $%s \n1. Transaction  \n2. Exit Choose option[2]:",
				currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")), withdrawAmmount,
				loggedInAccount.getBalance());
		String selection = Main.scanner.nextLine();
		if (selection.equalsIgnoreCase("1")) {
			return false;
		} else {
			return true;
		}

	}

	private static BigDecimal showOtherAmountScreen(Account account) {
		final BigDecimal maxWithdrawAmount = new BigDecimal(1000);
		final int multiplier = 10;
		boolean isAmountValid;
		boolean isBalanceSufficient = false;
		String inputedAmount;
		do {
			System.out.print("Other Withdraw \nEnter amount to withdraw: ");
			inputedAmount = Main.scanner.nextLine();
			if (inputedAmount.equalsIgnoreCase("")) {
				isAmountValid = true;
				continue;
			}
			BigDecimal inputedAmountNumber = NumberValidator.isNumber(inputedAmount) ? new BigDecimal(inputedAmount) : BigDecimal.ZERO;
			if (!NumberValidator.isNumber(inputedAmount) || !NumberValidator.isMultiplierOf(inputedAmountNumber, multiplier)) {
				System.out.println("Invalid ammount");
				isAmountValid = false;
			} else if (NumberValidator.isMoreThan(inputedAmountNumber, maxWithdrawAmount)) {
				System.out.println("Maximum amount to withdraw is $" + maxWithdrawAmount);
				isAmountValid = false;
			} else {
				isBalanceSufficient = checkBalanceSufficient(inputedAmountNumber, account.getBalance());
				isAmountValid = isBalanceSufficient;
			}
		} while (!isAmountValid);
		return isBalanceSufficient ? new BigDecimal(inputedAmount) : BigDecimal.ZERO;
	}

	private static boolean checkBalanceSufficient(BigDecimal withdrawAmount, BigDecimal balance) {
		if (balance.compareTo(withdrawAmount) < 0) {
			System.out.println("Insufficient balance $" + balance);
			return false;
		}
		return true;
	}

	public static String showPinInput() {
		String currentinputedPin;

		boolean isLoginPinValid;
		do {
			System.out.print("Enter PIN: ");
			currentinputedPin = Main.scanner.nextLine();
			isLoginPinValid = NumberValidator.validateNumberAndLength(currentinputedPin, "PIN", PIN_LENGTH);
		} while (!isLoginPinValid);
		return currentinputedPin;
	}

	public static String showAccountNumberInput() {
		boolean isLoginAccountNumberValid;
		String inputedLoginAccountNumber;
		do {
			System.out.print("Enter Account Number: ");
			inputedLoginAccountNumber = Main.scanner.nextLine();
			isLoginAccountNumberValid = NumberValidator.validateNumberAndLength(inputedLoginAccountNumber, "Account Number", ACCOUNT_NUMBER_LENGTH);
		} while (!isLoginAccountNumberValid);
		return inputedLoginAccountNumber;
	}

	public static void showTransactionScreen(Account loggedInAccount) {
		boolean isDisplayTransactionScreen = true;
		do {
			System.out.print("1. Withdraw \n2. Fund Transfer \n3. Exit \nPlease choose option[3]: ");
			String selectedTransaction = Main.scanner.nextLine();
			if (selectedTransaction.isEmpty())
				break;
			switch (selectedTransaction) {
			case "1":
				System.out.println("Withdraw");
				boolean isBackToWelcomeScreen = showWithdrawScreen(loggedInAccount); //
				isDisplayTransactionScreen = !isBackToWelcomeScreen;
				break;
			case "2":
				System.out.println("Fund Transfer");
				boolean isExit = showFundTransferScreen(loggedInAccount);
				isDisplayTransactionScreen = !isExit;
				break;
			case "3":
				isDisplayTransactionScreen = false;
				break;
			default:
				break;
			}
		} while (isDisplayTransactionScreen);
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

	public static String transfer(FundTransfer fundTransfer) {
		fundTransfer.getSourceAccount()
				.setBalance(fundTransfer.getSourceAccount().getBalance().subtract(fundTransfer.getAmount()));
		fundTransfer.getDestinationaccount()
				.setBalance(fundTransfer.getDestinationaccount().getBalance().add(fundTransfer.getAmount()));

		System.out.printf("Fund Transfer Summary \nDestination Account : %s \n",
				fundTransfer.getDestinationaccount().getAccountNumber());
		System.out.printf("Transfer Amount     : $%s \n", fundTransfer.getAmount());
		System.out.printf("Reference Number    : %s \n", fundTransfer.getReferenceNumber());
		System.out.printf("Balance     : $%s \n", fundTransfer.getSourceAccount().getBalance());
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
