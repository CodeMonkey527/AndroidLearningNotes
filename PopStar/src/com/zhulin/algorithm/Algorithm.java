package com.zhulin.algorithm;

import java.util.ArrayList;
import java.util.List;

import loon.core.graphics.component.LLayer;

import android.util.Log;

import com.zhulin.screens.MainScreen;
import com.zhulin.star.Star;

public class Algorithm {
	/**
	 * 当前类TAG
	 */
	private static final String TAG = "Algorithm";
	/**
	 * 被选中星星列表
	 */
	public static List<Star> selectedList;

	/**
	 * 当前总列数（由于消除，列数可能会减少）
	 */
	private static int currentColumn = 9;
	/**
	 * 当前关卡
	 */
	public static int currentRound = 1;

	/** 用以记录当前得分 */
	public static int score = 0;

	/**
	 * 当前关卡过关分数要求
	 */
	public static int threshold = 1000;

	/**
	 * 检查当前星星的四个方向是否有相同颜色星星
	 */
	public void checkFourDrection(Star star) {

		if (selectedList == null) {
			selectedList = new ArrayList<Star>();
		}
		if (selectedList.contains(star)) {
			return;
		}
		selectedList.add(star);
		star.setSelected(true);

		// 开始检查上下左右
		Star upStar, rightStar, bottomStar, leftStar;
		upStar = getUpStar(star);
		if (upStar != null && upStar.getColor() == star.getColor()) {
			checkFourDrection(upStar);
		}
		rightStar = getRightStar(star);
		if (rightStar != null && rightStar.getColor() == star.getColor()) {
			checkFourDrection(rightStar);
		}
		leftStar = getLeftStar(star);
		if (leftStar != null && leftStar.getColor() == star.getColor()) {
			checkFourDrection(leftStar);
		}
		bottomStar = getBottomStar(star);
		if (bottomStar != null && bottomStar.getColor() == star.getColor()) {
			checkFourDrection(bottomStar);
		}
	}

	/** 获取上面一颗星星 */
	public Star getUpStar(Star star) {
		if (star.getRaw() != 0) {
			return MainScreen.starMatrix[star.getRaw() - 1][star.getColumn()];
		} else {
			return null;
		}
	}

	/** 获取右边一颗星星 */
	public Star getRightStar(Star star) {
		if (star.getColumn() != 9) {
			return MainScreen.starMatrix[star.getRaw()][star.getColumn() + 1];
		} else {
			return null;
		}
	}

	/** 获取下面一颗星星 */
	public Star getBottomStar(Star star) {
		if (star.getRaw() != 9) {
			return MainScreen.starMatrix[star.getRaw() + 1][star.getColumn()];
		} else {
			return null;
		}
	}

	/** 获取左边一颗星星 */
	public Star getLeftStar(Star star) {
		if (star.getColumn() != 0) {
			return MainScreen.starMatrix[star.getRaw()][star.getColumn() - 1];
		} else {
			return null;
		}
	}

	/** 消除选中星星 */
	public void PopStars(LLayer layer, Star[][] starMatrix) {
		int raw, column;
		for (Star star : selectedList) {
			raw = star.getRaw();
			column = star.getColumn();
			layer.removeObject(starMatrix[raw][column]);
			starMatrix[raw][column].dispose();
			starMatrix[raw][column] = null;
		}
		selectedList = null;
		fillBottomGaps(starMatrix);
		fillLeftGaps(starMatrix);
	}

	/** 计算消除得分 */
	public static int getPopScore() {
		return selectedList.size() * selectedList.size() * 5;
	}

	/**
	 * 计算当前关卡过关阈值
	 * 
	 * @return threshold
	 */
	public static int getThreshold() {
		if (currentRound < 10) {
			switch (currentRound) {
			case 1:

				break;
			case 2:
				threshold = threshold + 2000;
				break;
			case 3:
				threshold = threshold + 3000;
				break;
			case 4:
				threshold = threshold + 2000;
				break;
			case 5:
				threshold = threshold + 2000;
				break;
			case 6:
				threshold = threshold + 3000;
				break;
			case 7:
				threshold = threshold + 2000;
				break;
			case 8:
				threshold = threshold + 2000;
				break;
			case 9:
				threshold = threshold + 3000;
				break;
			}
		} else {
			threshold = threshold + 4000;
		}
		return threshold;
	}

	/** 判定死局 */
	public boolean isDead() {
		for (int raw = 9; raw >= 0; raw--) {
			for (int column = 0; column <= 9; column++) {
				if (null == MainScreen.starMatrix[raw][column]) {
					continue;
				}
				// 开始检查上下左右
				Star upStar, rightStar, bottomStar, leftStar;
				upStar = getUpStar(MainScreen.starMatrix[raw][column]);
				rightStar = getRightStar(MainScreen.starMatrix[raw][column]);
				leftStar = getLeftStar(MainScreen.starMatrix[raw][column]);
				bottomStar = getBottomStar(MainScreen.starMatrix[raw][column]);
				if (upStar != null
						&& upStar.getColor() == MainScreen.starMatrix[raw][column]
								.getColor()) {
					return false;
				} else if (rightStar != null
						&& rightStar.getColor() == MainScreen.starMatrix[raw][column]
								.getColor()) {
					return false;
				} else if (leftStar != null
						&& leftStar.getColor() == MainScreen.starMatrix[raw][column]
								.getColor()) {
					return false;
				} else if (bottomStar != null
						&& bottomStar.getColor() == MainScreen.starMatrix[raw][column]
								.getColor()) {
					return false;
				}
			}
		}
		return true;
	}

	/** 填补空白 */
	public void fillBottomGaps(Star[][] starMatrix) {
		for (int column = 0; column < 10; column++) {
			/** 用以记录空位数 */
			int n = 0;
			for (int raw = 9; raw >= 0; raw--) {
				if (starMatrix[raw][column] == null) {
					n++;
					continue;
				} else if (n > 0) {
					int x, y;
					x = starMatrix[raw][column].getxCoordinate();
					y = starMatrix[raw][column].getyCoordinate();
					starMatrix[raw][column].moveTo(x, y
							+ (n * MainScreen.sideLength));
					starMatrix[raw][column].setyCoordinate(y
							+ (n * MainScreen.sideLength));
					starMatrix[raw][column].setRaw(raw + n);
					starMatrix[raw + n][column] = starMatrix[raw][column];
					starMatrix[raw][column] = null;
				}
			}
		}
	}

	/** 左移补空 */
	public void fillLeftGaps(Star[][] starMatrix) {
		/** 一次发现的空列数 */
		int n = 0;
		for (int column = 0; column <= currentColumn; column++) {
			if (starMatrix[9][column] == null) { // 发现空列，记录下来
				n++;
				continue;
			} else if (n > 0) {
				for (int raw = 9; raw >= 0; raw--) {
					if (starMatrix[raw][column] != null) {
						int x = starMatrix[raw][column].getxCoordinate();
						int y = starMatrix[raw][column].getyCoordinate();
						starMatrix[raw][column].moveTo(
								starMatrix[raw][column].getxCoordinate()
										- (MainScreen.sideLength * n),
								starMatrix[raw][column].getyCoordinate());
						starMatrix[raw][column].setColumn(column - n);
						starMatrix[raw][column].setxCoordinate(x
								- (n * MainScreen.sideLength));
						starMatrix[raw][column - n] = starMatrix[raw][column];
						starMatrix[raw][column] = null;
					} else {
						break;
					}
				}
			}
		}
		currentColumn -= n;
	}
	
	/**
	 * 游戏结束时重置数据
	 */
	public static void reSetVariate() {
		selectedList =null;
		currentColumn = 9;
		currentRound = 1;
		score = 0;
		threshold = 1000;
	}
	/**
	 * 下一关时重置数据
	 */
	public static void reSetForRoundNext(){
		selectedList =null;
		currentColumn = 9;
	}
}
