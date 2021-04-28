package com.mitrais.atm_simulation.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mitrais.atm_simulation.model.Account;

public class InMemoryAccountRepository implements IBaseRepository<Account, String> {
	private static final String CSV_LINE_SEPARATOR = ",";
	private static final int CSV_FIELDS_NUM = 4;
	private List<Account> accounts = initializedData();
	Logger logger = Logger.getLogger(InMemoryAccountRepository.class.getName());

	public List<Account> initializedData() {
		List<Account> accounts = new ArrayList<>();
		Account firstAccount = new Account("John Doe", "012108", new BigDecimal(100), "112233");
		Account secondAccount = new Account("Jane Doe", "932012", new BigDecimal(30), "112244");
		accounts.add(firstAccount);
		accounts.add(secondAccount);
		return accounts;
	}

	public void importCsvData(String filePath) throws FileNotFoundException, IOException {
		List<Account> currentAccounts = new ArrayList<>(accounts);

		try (FileInputStream inputStream = new FileInputStream(filePath);
				Scanner sc = new Scanner(inputStream, "UTF-8")) {

			boolean isCsvHeaderValid = validateCsvHeader(sc);
			int lineNumber = 1;
			while (isCsvHeaderValid && sc.hasNext()) {
				String line = sc.nextLine();
				lineNumber++;
				String[] fieldsData = line.split(CSV_LINE_SEPARATOR);
				loadAccountData(fieldsData, currentAccounts, lineNumber);
				if (sc.ioException() != null) {
					throw sc.ioException();
				}
			}

		}
		this.accounts = currentAccounts;
		System.out.println("Import Finished");
	}

	private void loadAccountData(String[] fieldsData, List<Account> accounts, int lineNumber) {
		if (Arrays.asList(fieldsData).stream().anyMatch(fieldValue -> fieldValue.isEmpty())) {
			logger.log(Level.WARNING, "There are empty value please check  line {0}, skipped",
					new Object[] { lineNumber });
			return;
		}
		Account account = new Account(fieldsData[0], fieldsData[1], new BigDecimal(fieldsData[2]), fieldsData[3]);
		if (accounts.stream()
				.anyMatch(streamAccount -> streamAccount.getAccountNumber().equals(account.getAccountNumber()))) {
			logger.log(Level.WARNING,
					"There are existing account {0} please check your file, new record for this account will be skipped",
					new Object[] { account.getAccountNumber() });
		} else {
			accounts.add(account);
		}

	}

	public boolean validateCsvHeader(Scanner sc) {
		boolean isCsvValid = true;
		if (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] fieldsData = line.split(CSV_LINE_SEPARATOR);

			if (fieldsData.length != CSV_FIELDS_NUM) {
				isCsvValid = false;
			}
		}
		return isCsvValid;
	}

	@Override
	public List<Account> getAll() {
		return accounts;
	}

	@Override
	public Account getById(String accountNumber) {
		List<Account> results = this.accounts.stream().filter(account -> account.getAccountNumber() == accountNumber)
				.collect(Collectors.toList());
		return !results.isEmpty() ? results.get(0).clone() : null;
	}

	@Override
	public int update(Account editedAccount) {
		int updatedAccountNum = 0;
		if (null != editedAccount && null != editedAccount.getAccountNumber()) {
			Account dbAccount = this.getById(editedAccount.getAccountNumber());
			if (null != dbAccount) {
				dbAccount.setFullName(editedAccount.getFullName());
				dbAccount.setBalance(editedAccount.getBalance());
				updatedAccountNum = 1;
			}
		}
		return updatedAccountNum;
	}

	@Override
	public Account save(Account object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Account persistenceObject) {
		// TODO Auto-generated method stub
		return 0;
	}
}
