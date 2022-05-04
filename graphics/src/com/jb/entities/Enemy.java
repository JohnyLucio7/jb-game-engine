package com.jb.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.World;

public class Enemy extends Entity {

	private int speed = 1;
	private int maskx = 8;
	private int masky = 8;
	private int maskw = 10;
	private int maskh = 10;
	private boolean enableCollisionMask = false;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {

		if (Game.rand.nextInt(100) < 50) {

			if (this.getX() < Game.player.getX() && World.isFree(this.getX() + speed, this.getY())
					&& !isColliding(this.getX() + speed, this.getY()))
				this.setX(this.getX() + speed);
			else if (this.getX() > Game.player.getX() && World.isFree(this.getX() - speed, this.getY())
					&& !isColliding(this.getX() - speed, this.getY()))
				this.setX(this.getX() - speed);

			if (this.getY() < Game.player.getY() && World.isFree(this.getX(), this.getY() + speed)
					&& !isColliding(this.getX(), this.getY() + speed))
				this.setY(this.getY() + speed);
			else if (this.getY() > Game.player.getY() && World.isFree(this.getX(), this.getY() - speed)
					&& !isColliding(this.getX(), this.getY() - speed))
				this.setY(this.getY() - speed);
		}
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;

			Rectangle enemyTarget = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);

			if (enemyCurrent.intersects(enemyTarget))
				return true;
		}

		return false;
	}

	public void render(Graphics g) {
		super.render(g);
		if (enableCollisionMask)
			showCollisionMask(g);
	}

	private void showCollisionMask(Graphics g) {
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
