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
	private boolean enableRectCollisionMask = false;
	private boolean enableRectCollisionWithPlayer = false;
	private boolean enableRectBorderColissionWithPlayer = false;

	/** Variáveis de Animação */

	private int enemyAnimFrames = 0;
	private int enemyAnimeMaxFrames = 5;
	private int enemyAnimSpriteIndex = 0;
	private int enemyAnimeMaxSpriteIndex = 4;
	private BufferedImage[] enemyLeft;
	private BufferedImage[] enemyRight;
	private int dir = 0, dir_left = 1, dir_right = 0; // substituir por enum

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		enemyRight = new BufferedImage[4];
		enemyLeft = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			enemyRight[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, width, height);
			enemyLeft[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, width, height);
		}
	}

	public void tick() {

		if (!isCollidingWithPlayer()) {
			if (Game.rand.nextInt(100) < 50) {

				if (this.getX() < Game.player.getX() && World.isFree(this.getX() + speed, this.getY())
						&& !isColliding(this.getX() + speed, this.getY())) {
					//
					this.setX(this.getX() + speed);
					dir = dir_right;
				} else if (this.getX() > Game.player.getX() && World.isFree(this.getX() - speed, this.getY())
						&& !isColliding(this.getX() - speed, this.getY())) {
					//
					this.setX(this.getX() - speed);
					dir = dir_left;
				}

				if (this.getY() < Game.player.getY() && World.isFree(this.getX(), this.getY() + speed)
						&& !isColliding(this.getX(), this.getY() + speed)) {
					//
					this.setY(this.getY() + speed);
				} else if (this.getY() > Game.player.getY() && World.isFree(this.getX(), this.getY() - speed)
						&& !isColliding(this.getX(), this.getY() - speed)) {
					//
					this.setY(this.getY() - speed);
				}
			}
		} else {
			// perto do player

			if (Game.rand.nextInt(100) < 10) {
				Game.player.setLife(Game.player.getLife() - Game.rand.nextInt(3));
			}
		}

		if (enemyAnimFrames >= enemyAnimeMaxFrames) {
			enemyAnimSpriteIndex++;
			enemyAnimSpriteIndex %= enemyAnimeMaxSpriteIndex;
		}
		enemyAnimFrames %= enemyAnimeMaxFrames;
		enemyAnimFrames++;

	}

	public void render(Graphics g) {
		if (dir == dir_right) {
			g.drawImage(enemyRight[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == dir_left) {
			g.drawImage(enemyLeft[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		if (enableRectCollisionMask) {
			showRectCollisionMask(g);
		}

		if (enableRectCollisionWithPlayer) {
			showRectCollisionWithPlayer(g);
		}

		if (enableRectBorderColissionWithPlayer) {
			showRectBorderColissionWithPlayer(g);
		}
	}

	public boolean isCollidingWithPlayer() {
		this.setMask(3, 4, 10, 12);
		Rectangle enemyCurrent = new Rectangle(this.getX() + this.getMaskX(), this.getY() + this.getMaskY(),
				this.getMaskW(), this.getMaskH());
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xnext, int ynext) {
		this.setMask(8, 8, 10, 10);
		Rectangle enemyCurrent = new Rectangle(xnext + this.getMaskX(), ynext + this.getMaskY(), this.getMaskW(),
				this.getMaskH());

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;

			Rectangle enemyTarget = new Rectangle(e.getX() + this.getMaskX(), e.getY() + this.getMaskY(),
					this.getMaskW(), this.getMaskH());

			if (enemyCurrent.intersects(enemyTarget))
				return true;
		}

		return false;
	}

	private void showRectCollisionMask(Graphics g) {
		this.setMask(8, 8, 10, 10);
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(this.getX() + this.getMaskX() - Camera.x, this.getY() + this.getMaskY() - Camera.y, this.getMaskW(),
				this.getMaskH());
	}

	private void showRectCollisionWithPlayer(Graphics g) {
		this.setMask(3, 4, 10, 12);
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(this.getX() + this.getMaskX() - Camera.x, this.getY() + this.getMaskY() - Camera.y, this.getMaskW(),
				this.getMaskH());
	}

	private void showRectBorderColissionWithPlayer(Graphics g) {
		this.setMask(3, 4, 10, 12);
		
		g.setColor(new Color(255, 255, 0));

		int[] dx = new int[] { this.getX() + this.getMaskX() - Camera.x,
				this.getX() + this.getMaskX() + this.getMaskW() - Camera.x,
				this.getX() + this.getMaskX() + this.getMaskW() - Camera.x, this.getX() + this.getMaskX() - Camera.x };

		int[] dy = new int[] { this.getY() + this.getMaskY() - Camera.y, this.getY() + this.getMaskY() - Camera.y,
				this.getY() + this.getMaskY() + this.getMaskH() - Camera.y,
				this.getY() + this.getMaskY() + this.getMaskH() - Camera.y };
		g.drawPolygon(dx, dy, 4);
	}

}
