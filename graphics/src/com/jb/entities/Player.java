package com.jb.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {

	private boolean right, up, left, down;
	private int speed = 1;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if (right) {
			setX(getX() + speed);
		} else if (left) {
			setX(getX() - speed);
		}

		if (up) {
			setY(getY() - speed);
		} else if (down) {
			setY(getY() + speed);
		}
	}

	public void setRight(boolean b) {
		this.right = b;
	}

	public void setLeft(boolean b) {
		this.left = b;
	}

	public void setUp(boolean b) {
		this.up = b;
	}

	public void setDown(boolean b) {
		this.down = b;
	}

}
