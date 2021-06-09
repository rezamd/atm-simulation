package com.mitrais.atm_simulation.exception;

public class NoDataFoundException extends Exception {
	private static final long serialVersionUID = 8938749508675811328L;

	public NoDataFoundException() {
		super("No Data Found");
	}

	public NoDataFoundException(String string) {
		super(string);
	}
}
