package com.zhulin.popstar;

import com.zhulin.screens.WelcomeScreen;

import loon.LGame;
import loon.LSetting;

public class MainActivity extends LGame {

	@Override
	public void onGamePaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameResumed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMain() {

		LSetting setting = new LSetting();
		setting.landscape = false;
		setting.height = 800;
		setting.width = 480;
		setting.fps = 60;
		setting.showFPS = false;
		setting.showLogo = false;
		register(setting, WelcomeScreen.class);
	}

}
