package com.mitrais.atm_simulation.exception;

public class UnauthorizedException extends Exception {
	private static final long serialVersionUID = -4217313606759233468L;

	public UnauthorizedException() {
		super("User Unauthorized");
	}

	public UnauthorizedException(String message) {
		super(message);
	}
	
}
