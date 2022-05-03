package com.jb.entities;

import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.World;

public class Enemy extends Entity {

	private int speed = 1;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if (this.getX() < Game.player.getX() && World.isFree(this.getX() + speed, this.getY()))
			this.setX(this.getX() + speed);
		else if (this.getX() > Game.player.getX() && World.isFree(this.getX() - speed, this.getY()))
			this.setX(this.getX() - speed);

		if (this.getY() < Game.player.getY() && World.isFree(this.getX(), this.getY() + speed))
			this.setY(this.getY() + speed);
		else if (this.getY() > Game.player.getY() && World.isFree(this.getX(), this.getY() - speed))
			this.setY(this.getY() - speed);

	}

}
