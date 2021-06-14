package com.mitrais.atm_simulation.main;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.exception.LowBalanceException;
import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.model.FundTransfer;
import com.mitrais.atm_simulation.repository.AccountRepository;
import com.mitrais.atm_simulation.screen.Screen;
import com.mitrais.atm_simulation.service.AccountService;
import com.mitrais.atm_simulation.service.LoginService;
import com.mitrais.atm_simulation.validator.NumberValidator;
public class Main {
	public static Scanner scanner;
	private static AccountRepository accountRepo;
	private static AccountService accountService;
	private static Random random = new Random();
	public static Account loggedInAccount;
	private static Screen screen;
	
	public static void main(String[] args) {
		accountRepo = new AccountRepository();
		new LoginService(accountRepo);
		accountService = new AccountService(accountRepo);
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
					ScreenTypeEnum.TRANSACTION_MAIN_SCREEN);
			if (migratedScreen.contains(nextScreen))
				navigateToScreen(nextScreen);
			else {
				switch (nextScreen) {
				case WITHDRAWAL_MAIN_SCREEN:
					navigateToScreen(showWithdrawScreen());
					break;
				case TRANSFER_FUND_MAIN_SCREEN:
					showFundTransferScreen(loggedInAccount);
					break;
				default:
					break;
				}
			}
		}

	}

	public static ScreenTypeEnum showWithdrawScreen() {
		String selectedAccountNunmber = loggedInAccount.getAccountNumber();

		System.out.print("1. $10 \n2. $50 \n3. $100 \n4. Other \n5. Back \nPlease choose option[5]:");
		String selectedAmount = Main.scanner.nextLine();

		if (selectedAmount.isEmpty()) {
			return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
		}
		Map<String, BigDecimal> withdrawAmountMap = new HashMap<String, BigDecimal>();
		withdrawAmountMap.put("1", new BigDecimal(10));
		withdrawAmountMap.put("2", new BigDecimal(50));
		withdrawAmountMap.put("3", new BigDecimal(100));

		BigDecimal inputedAmount = withdrawAmountMap.get(selectedAmount);
		try {
			switch (selectedAmount) {
			case "1":
			case "2":
			case "3":
				break;
			case "4":
				inputedAmount = showOtherAmountScreen(loggedInAccount);
				break;
			case "5":
				return ScreenTypeEnum.TRANSACTION_MAIN_SCREEN;
			default:
				return showWithdrawScreen();
			}
			loggedInAccount.setBalance(accountService.withdraw(selectedAccountNunmber, inputedAmount).getBalance());
		} catch (LowBalanceException e) {
			System.out.println(e.getMessage());
			return showWithdrawScreen();
		} catch (NoDataFoundException e) {
			System.out.println(e.getMessage());
			return showWithdrawScreen();
		}
		return showWithdrawSummaryScreen(loggedInAccount, inputedAmount);
	}

	private static ScreenTypeEnum showWithdrawSummaryScreen(Account loggedInAccount, BigDecimal withdrawAmmount) {
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

	private static BigDecimal showOtherAmountScreen(Account account) {
		final BigDecimal maxWithdrawAmount = new BigDecimal(1000);
		final int multiplier = 10;
		String inputedAmount;
		System.out.print("Other Withdraw \nEnter amount to withdraw: ");
		inputedAmount = Main.scanner.nextLine();
		if (inputedAmount.equalsIgnoreCase("")) {
			return showOtherAmountScreen(account);
		}
		BigDecimal inputedAmountNumber = NumberValidator.isNumber(inputedAmount) ? new BigDecimal(inputedAmount)
				: BigDecimal.ZERO;
		if (!NumberValidator.isNumber(inputedAmount)
				|| !NumberValidator.isMultiplierOf(inputedAmountNumber, multiplier)) {
			System.out.println("Invalid ammount");
			return showOtherAmountScreen(account);
		} else if (NumberValidator.isMoreThan(inputedAmountNumber, maxWithdrawAmount)) {
			System.out.println("Maximum amount to withdraw is $" + maxWithdrawAmount);
			return showOtherAmountScreen(account);
		}
		return inputedAmountNumber;
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
