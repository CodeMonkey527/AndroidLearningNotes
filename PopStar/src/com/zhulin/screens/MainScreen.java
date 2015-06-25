package com.zhulin.screens;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import loon.LTouch;
import loon.action.map.Field2D;
import loon.core.Assets;
import loon.core.graphics.Screen;
import loon.core.graphics.Screen.MoveMethod;
import loon.core.graphics.component.Actor;
import loon.core.graphics.component.LLayer;
import loon.core.graphics.component.LTextArea;
import loon.core.graphics.device.LColor;
import loon.core.graphics.opengl.GLEx;
import loon.core.graphics.opengl.LTexture;
import loon.core.timer.LTimerContext;
import loon.media.Sound;
import android.util.Log;

import com.zhulin.algorithm.Algorithm;
import com.zhulin.listener_interface.MyClickListener;
import com.zhulin.star.Star;

public class MainScreen extends Screen {
	private static final String TAG = "MainScreen";

	/** 用以填充星星的layer */
	private LLayer layer;
	/** 全部星星二维数组 */
	private LLayer infoLayer;
	public static Star[][] starMatrix;
	/** 星星边长 */
	public static int sideLength;
	/** 用以播放音乐 */
	private Sound backgroundPlayer;

	/** 庆祝actor */
	private Actor combo_1, combo_2, combo_3;

	/** 显示关卡文本控件 */
	private LTextArea roundText;
	private LTextArea roundText1;
	/** 显示目标分数文本控件 */
	private LTextArea thresholdText;
	private LTextArea thresholdText1;
	/** 显示当前得分文本控件 */
	private LTextArea scoreText;
	private LTextArea scoreText1;

	private Timer timer;

	public MainScreen() {
	}

	@Override
	public void onLoad() {
		super.onLoad();
		setBackground("assets/background/bg_mainscene.jpg");
		sideLength = getWidth() / 10;
		layer = new LLayer(0, getHeight() - getWidth(), getWidth(), getWidth());
		infoLayer = new LLayer(0, 0, getWidth(), getHeight()
				- layer.getHeight());
		infoLayer.setActorDrag(false);
		add(infoLayer);
		layer.setActorDrag(false);
		layer.alpha = 0.2f;
		Field2D field2d = new Field2D("assets/map.txt", 48, 48);
		layer.setField2D(field2d);
		backgroundPlayer = Assets.getMusic("music/readygo.mp3");
		backgroundPlayer.prepare();
		backgroundPlayer.play();
		initInfo();
		initialStars();
		add(layer);
	}

	/**
	 * 初始化游戏信息
	 */
	private void initInfo() {
		// 关卡文本
		roundText = new LTextArea(10, 10, getWidth() / 3, 50, (LTexture) null);
		roundText.setWaitFlag(false);
		roundText1 = new LTextArea(70, 10, getWidth() / 3, 50, (LTexture) null);
		roundText1.setWaitFlag(false);
		roundText1.setPostLine(0);
		infoLayer.add(roundText);
		infoLayer.add(roundText1);

		// 目标分数文本
		thresholdText = new LTextArea(getWidth() / 3, 10, getWidth() / 3, 50,
				(LTexture) null);
		thresholdText.setWaitFlag(false);
		thresholdText1 = new LTextArea(getWidth() / 3 + 100, 10,
				getWidth() / 3, 50, (LTexture) null);
		thresholdText1.setWaitFlag(false);
		thresholdText1.setPostLine(0);
		infoLayer.add(thresholdText);
		infoLayer.add(thresholdText1);

		scoreText = new LTextArea(2 * getWidth() / 3, 10, getWidth() / 3, 50,
				(LTexture) null);
		scoreText.setWaitFlag(false);
		scoreText1 = new LTextArea(2 * getWidth() / 3 + 60, 10, getWidth() / 3,
				50, (LTexture) null);
		scoreText1.setWaitFlag(false);
		scoreText1.setPostLine(0);
		infoLayer.add(scoreText);
		infoLayer.add(scoreText1);

		roundText.put("关卡：");
		roundText1.put("" + Algorithm.currentRound);
		thresholdText.put("目标分数：");
		thresholdText1.put(Algorithm.getThreshold() + "");
		scoreText.put("得分：");
		scoreText1.put(Algorithm.score + "");
		// Log.d(TAG+".......", scoreText.getPostLine() + "");
	}

	@Override
	public void onLoaded() {
		super.onLoaded();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (!backgroundPlayer.isPlaying()) {
					backgroundPlayer.release();
					backgroundPlayer = Assets
							.getMusic("music/tiantianaixiaochu.mp3");
					backgroundPlayer.setVolume(0.2f);
					backgroundPlayer.setLooping(true);
					backgroundPlayer.prepare();
					backgroundPlayer.play();
				}
			}
		}, 2000);
	}

	/** 初始化全局星星 */
	private void initialStars() {
		Algorithm.reSetForRoundNext();
		starMatrix = new Star[10][10];
		/** 记录当前星星的y坐标 */
		int y = 0;
		for (int raw = 0; raw < 10; raw++) {
			/** 记录当前星星的x坐标 */
			int x = 0;
			for (int column = 0; column < 10; column++) {
				starMatrix[raw][column] = Star.loadStar(x, y, raw, column,
						sideLength);
				layer.addObject(starMatrix[raw][column]);
				starMatrix[raw][column].setClickListener(new MyClickListener() {

					@Override
					public void onSelectClick(Star star) {
						// 如果list不为空，则将其中星星选中标记取消，然后将list设置为空
						if (Algorithm.selectedList != null) {
							deMarkStars();
							Algorithm.selectedList = null;
						}
						// 检查被点击星星四周是否有相同颜色星星
						new Algorithm().checkFourDrection(star);
						// 如果list中只有一颗星星，则将这颗星星isSelected属性设置为false,
						// 并且将list设置空；否则将list中的星星标记为选中
						if (Algorithm.selectedList.size() > 1) {
							playSound("assets/music/select.wav");
							markStars();
						} else {
							Algorithm.selectedList.get(0).setSelected(false);
							Algorithm.selectedList = null;
						}
					}

					@Override
					public void onPopClick(Star star) {

						Log.d(TAG, "onPopClick被触发 ： " + star.getRaw() + " "
								+ star.getColumn());
						playSound("assets/music/pop.wav");
						int numberOfStars = Algorithm.selectedList.size();
						Algorithm.score += Algorithm.getPopScore();
						new Algorithm().PopStars(layer, starMatrix);

						if (numberOfStars >= 20) {
							playSound("assets/music/combo_3.wav");
							combo_3 = new Actor("assets/background/combo_3.png");
							infoLayer.addObject(combo_3);
							combo_3.setLocation(infoLayer.getWidth() / 2
									- combo_3.getWidth() / 2, 150);
							combo_3.fadeIn();
							timer = new Timer();
							timer.schedule(new TimerTask() {

								@Override
								public void run() {
									infoLayer.removeObject(combo_3);
								}
							}, 1500);
						} else if (numberOfStars >= 15) {
							playSound("assets/music/combo_2.wav");
							combo_2 = new Actor("assets/background/combo_2.png");
							infoLayer.addObject(combo_2);
							combo_2.setLocation(infoLayer.getWidth() / 2
									- combo_2.getWidth() / 2, 150);
							combo_2.fadeIn();
							timer = new Timer();
							timer.schedule(new TimerTask() {

								@Override
								public void run() {
									infoLayer.removeObject(combo_2);
								}
							}, 1500);
						} else if (numberOfStars >= 10) {
							playSound("assets/music/combo_1.wav");
							combo_1 = new Actor("assets/background/combo_1.png");
							infoLayer.addObject(combo_1);
							combo_1.setLocation(infoLayer.getWidth() / 2
									- combo_1.getWidth() / 2, 150);
							combo_1.fadeIn();
							timer = new Timer();
							timer.schedule(new TimerTask() {

								@Override
								public void run() {
									infoLayer.removeObject(combo_1);
								}
							}, 1500);
						}
						scoreText1.put(Algorithm.score + "");
						// 判断是否为死局
						if (new Algorithm().isDead()) {
							Log.d(TAG, "检查死局结果：true");

							if (Algorithm.score >= Algorithm.threshold) { // 过关
								popDeadStars();
								Algorithm.currentRound++;
								Log.v("查看进入下一关判断没有:", "true");
								// 更新显示信息 进入下一关
								roundText1.put("" + Algorithm.currentRound);
								thresholdText1.put(Algorithm.getThreshold()
										+ "");
								scoreText1.put(Algorithm.score + "");
								initialStars();
							} else { // 结束游戏
								setReplaceScreenSpeed(getReplaceScreenSpeed() * 100);
								replaceScreen(new WelcomeScreen(),
										MoveMethod.FROM_LEFT);
								Log.v("查看进入下一关判断没有:", "false");
								Algorithm.reSetVariate();
								resourceRecycle();

							}
						}
						Log.d(TAG, "当前得分" + Algorithm.score);
					}
				});
				x += sideLength;
			}
			y += sideLength;
		}
	}

	/**
	 * 消除剩余的星星
	 */
	private void popDeadStars() {
		ArrayList<Star> deadStarList = new ArrayList<Star>();
		for (int raw = 0; raw < 10; raw++) {
			for (int column = 0; column < 10; column++) {
				if (null != starMatrix[raw][column]) {
					deadStarList.add(starMatrix[raw][column]);
				}
			}
		}
		// 计算剩余多少个，如果少于10个有得分奖励
		if (deadStarList.size() <= 10) {
			Algorithm.score += 2000 - (deadStarList.size()
					* deadStarList.size() * 20);
		}
		// 开始消除
		for (int raw = 0; raw < 10; raw++) {
			for (int column = 0; column < 10; column++) {
				if (null != starMatrix[raw][column]) {
					layer.removeObject(starMatrix[raw][column]);
					starMatrix[raw][column].dispose();
					starMatrix[raw][column] = null;
				}
			}
		}
		starMatrix = null;
	}

	/** 标记被选中的星星 */
	private void markStars() {
		/** 被选中星星列表中的第一个星星，用以判断这组星星的颜色 */
		Star firstStar = Algorithm.selectedList.get(0);
		if (firstStar.getColor() == LColor.red) {
			for (Star star2 : Algorithm.selectedList)
				star2.setImage("assets/stars/red_selected.png");
		} else if (firstStar.getColor() == LColor.blue) {
			for (Star star2 : Algorithm.selectedList)
				star2.setImage("assets/stars/blue_selected.png");
		} else if (firstStar.getColor() == LColor.green) {
			for (Star star2 : Algorithm.selectedList)
				star2.setImage("assets/stars/green_selected.png");
		} else if (firstStar.getColor() == LColor.orange) {
			for (Star star2 : Algorithm.selectedList)
				star2.setImage("assets/stars/orange_selected.png");
		} else if (firstStar.getColor() == LColor.purple) {
			for (Star star2 : Algorithm.selectedList)
				star2.setImage("assets/stars/purple_selected.png");
		}
	}

	/** 取消星星的标记状态 */
	private void deMarkStars() {
		/** 被选中星星列表中的第一个星星，用以判断这组星星的颜色 */
		Star firstStar = Algorithm.selectedList.get(0);
		if (firstStar.getColor() == LColor.red) {
			for (Star star2 : Algorithm.selectedList) {
				star2.setImage("assets/stars/red.png");
				star2.setSelected(false);
			}
		} else if (firstStar.getColor() == LColor.blue) {
			for (Star star2 : Algorithm.selectedList) {
				star2.setImage("assets/stars/blue.png");
				star2.setSelected(false);
			}
		} else if (firstStar.getColor() == LColor.green) {
			for (Star star2 : Algorithm.selectedList) {
				star2.setImage("assets/stars/green.png");
				star2.setSelected(false);
			}
		} else if (firstStar.getColor() == LColor.orange) {
			for (Star star2 : Algorithm.selectedList) {
				star2.setImage("assets/stars/orange.png");
				star2.setSelected(false);
			}
		} else if (firstStar.getColor() == LColor.purple) {
			for (Star star2 : Algorithm.selectedList) {
				star2.setImage("assets/stars/purple.png");
				star2.setSelected(false);
			}
		}
	}

	/**
	 * 资源回收
	 */
	private void resourceRecycle() {
		// TODO
		if (null != layer) {
			layer.dispose();
		}
		if (null != infoLayer) {
			infoLayer.dispose();
		}
		if (null != starMatrix) {
			starMatrix = null;
		}
		if (backgroundPlayer.isPlaying()) {
			backgroundPlayer.stop();
			backgroundPlayer.release();
			backgroundPlayer = null;
		}
	}

	@Override
	public void alter(LTimerContext arg0) {

	}

	@Override
	public void draw(GLEx arg0) {

	}

	@Override
	public void touchDown(LTouch arg0) {

	}

	@Override
	public void touchDrag(LTouch arg0) {

	}

	@Override
	public void touchMove(LTouch arg0) {

	}

	@Override
	public void touchUp(LTouch arg0) {

	}
}
