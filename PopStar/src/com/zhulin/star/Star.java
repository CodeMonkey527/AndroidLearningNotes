package com.zhulin.star;

import loon.core.graphics.component.Actor;
import loon.core.graphics.device.LColor;
import loon.core.graphics.opengl.GLEx;
import loon.core.graphics.opengl.LTexture;
import loon.core.graphics.opengl.LTextures;
import android.util.Log;

import com.zhulin.listener_interface.MyClickListener;

public class Star extends Actor {
	private static final String TAG = "Star";
	/** 星星的颜色 */
	private LColor color;
	/** 星星是否被选择或者被标记为相同颜色 */
	private boolean isSelected;
	/** 星星所在行 */
	private int raw;
	/** 星星所在列 */
	private int column;
	/** 星星横坐标 */
	private int xCoordinate;
	/** 星星纵坐标 */
	private int yCoordinate;
	/** MyClickListener */
	private MyClickListener myClickListener;

	public static Star loadStar(int x, int y, int raw, int column,
			int sidelength) {
		int random = (int) (Math.random() * 10);
		LTexture texture;
		Star star;
		Log.d(TAG, random + "");
		switch (random % 5) {
		case 0:
			texture = LTextures.loadTexture("assets/stars/blue.png").scale(
					sidelength, sidelength);
			star = new Star(texture, x, y);
			star.color = LColor.blue;
			break;
		case 1:
			texture = LTextures.loadTexture("assets/stars/green.png").scale(
					sidelength, sidelength);
			star = new Star(texture, x, y);
			star.color = LColor.green;
			break;
		case 2:
			texture = LTextures.loadTexture("assets/stars/orange.png").scale(
					sidelength, sidelength);
			star = new Star(texture, x, y);
			star.color = LColor.orange;
			break;
		case 3:
			texture = LTextures.loadTexture("assets/stars/purple.png").scale(
					sidelength, sidelength);
			star = new Star(texture, x, y);
			star.color = LColor.purple;
			break;
		case 4:
			texture = LTextures.loadTexture("assets/stars/red.png").scale(
					sidelength, sidelength);
			star = new Star(texture, x, y);
			star.color = LColor.red;
			break;
		default:
			Log.d(TAG, "开关语句进入default!");
			star = new Star();
			break;
		}
		star.raw = raw;
		star.column = column;
		star.isSelected = false;
		star.xCoordinate = x;
		star.yCoordinate = y;
		return star;
	}

	// 重写父类构造方法
	public Star(LTexture image, int x, int y) {
		super(image, x, y);
		isSelected = false;
	}

	public Star() {
		super();
	}

	public void setClickListener(MyClickListener listener) {
		myClickListener = listener;
	}

	@Override
	public void upClick(int x, int y) {
		super.upClick(x, y);
		if (this.isSelected()) {
			myClickListener.onPopClick(this);
		} else {
			this.setSelected(true);
			myClickListener.onSelectClick(this);
		}
	}

	// getters and setters
	public int getRaw() {
		return raw;
	}

	public void setRaw(int raw) {
		this.raw = raw;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public LColor getColor() {
		return color;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
}
