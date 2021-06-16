package com.mitrais.atm_simulation.main;

import java.util.Scanner;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.model.Account;
import com.mitrais.atm_simulation.screen.Screen;

public class Main {
	public static Scanner scanner;
	public static Account loggedInAccount;

	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		navigateToScreen(ScreenTypeEnum.WELCOME_SCREEN);
	}

	public static void navigateToScreen(ScreenTypeEnum screenType) {
		synchronized (Screen.class) {
			Screen currentScreen = Screen.getScreen(screenType);
			ScreenTypeEnum nextScreen = currentScreen.displayScreen();
			navigateToScreen(nextScreen);
		}

	}
}
