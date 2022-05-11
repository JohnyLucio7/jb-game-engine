package com.jb.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity {

	private int ammo = 18;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public int getAmmo() {
		return this.ammo;
	}

	public void setAmmo(int a) {

		if (a < 0) {
			this.ammo = 0;
		} else {
			this.ammo = a;
		}

	}

}
