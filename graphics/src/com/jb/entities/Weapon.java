package com.jb.entities;

import java.awt.image.BufferedImage;
import com.jb.main.Game;

public class Weapon extends Entity {

	private int ammoInClip;
	private int maxAmmoInClip;

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		this.ammoInClip = 3;
		this.maxAmmoInClip = 6;

	}

	public void reload(int ammo) {

		if (ammoInClip != maxAmmoInClip) {
			int ammoNeed = maxAmmoInClip - ammoInClip;

			if (ammoNeed <= Game.player.getAmmo()) {
				ammoInClip += ammoNeed;
				Game.player.setAmmo(Game.player.getAmmo() - ammoNeed);
			} else if (ammoNeed > Game.player.getAmmo()) {
				ammoInClip += Game.player.getAmmo();
				Game.player.setAmmo(0);
			} else {
				ammoInClip += ammoNeed;
				Game.player.setAmmo(0);
			}
		}
	}

	public int getMaxAmmoInClip() {
		return this.maxAmmoInClip;
	}

	public void setMaxAmmoInClip(int maic) {
		if (maic < 0) {
			this.maxAmmoInClip = 0;
		} else {
			this.maxAmmoInClip = maic;
		}
	}

	public int getAmmoInClip() {
		return this.ammoInClip;
	}

	public void setAmmoInClip(int a) {

		if (a < 0) {
			this.ammoInClip = 0;
		} else {
			this.ammoInClip = a;
		}

	}

}
