package com.mitrais.atm_simulation.main;

import com.mitrais.atm_simulation.enumerator.ScreenTypeEnum;
import com.mitrais.atm_simulation.screen.Screen;

public class AtmMachine {
	private Screen screen;

	public void navigateToScreen(ScreenTypeEnum screenType) {
		synchronized (Screen.class) {
			Screen newScreen = Screen.getScreen(screenType);
			setScreen(newScreen);
			ScreenTypeEnum nextScreen = screen.displayScreen();
			navigateToScreen(nextScreen);
		}
	}

	private void setScreen(Screen screen) {
		this.screen = screen;
	}
}
