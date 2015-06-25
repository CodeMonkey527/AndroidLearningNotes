package com.zhulin.screens;

import loon.LTouch;
import loon.LTransition;
import loon.core.Assets;
import loon.core.graphics.Screen;
import loon.core.graphics.component.LButton;
import loon.core.graphics.opengl.GLEx;
import loon.core.timer.LTimerContext;
import loon.media.Sound;

public class WelcomeScreen extends Screen {
	private static final String TAG = "WelcomeScreen";
	private LButton button;
	private Sound player;

	public LTransition onTransition() {
		return LTransition.newEmpty();
	}

	public WelcomeScreen() {

	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		setBackground("assets/background/bg_menuscene.jpg");

		player = Assets.getMusic("music/music.mp3");
		player.setLooping(true);
		player.prepare();
		player.play();

		button = new LButton("assets/button/menu_start.png", 268, 71) {

			@Override
			public void doClick() {
				super.doClick();
				if (player.isPlaying()) {
					player.stop();
					player.release();
				}
				setReplaceScreenSpeed(getReplaceScreenSpeed() * 100);
				replaceScreen(new MainScreen(), MoveMethod.FROM_RIGHT);
			}
		};
		button.centerOnScreen();
		add(button);
	}

	@Override
	public void alter(LTimerContext arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(GLEx arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void touchDown(LTouch arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchDrag(LTouch arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchMove(LTouch arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchUp(LTouch arg0) {
		// TODO Auto-generated method stub

	}

}
