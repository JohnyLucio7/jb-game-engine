package com.jb.entities;

import java.awt.image.BufferedImage;

public class Lifepack extends Entity {

	private int lifePoints = 20;

	public Lifepack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

	}

	public int getLifePoints() {
		return this.lifePoints;
	}

	public void setLifePoints(int lp) {
		this.lifePoints = lp;
	}

}
