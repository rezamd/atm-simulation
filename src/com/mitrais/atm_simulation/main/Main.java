package com.mitrais.atm_simulation.main;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;

public class Main {
	public static void main(String[] args) {
		AtmMachine atmMachine = new AtmMachine();
		atmMachine.navigateToScreen(ScreenTypeEnum.WELCOME_SCREEN);
	}
}
