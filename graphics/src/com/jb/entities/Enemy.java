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
	private boolean enableRectCollisionMask = false;

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
				Game.player.life -= Game.rand.nextInt(3);

				if (Game.player.life <= 0) {
					// Game over
					Game.player.life = 0;
					System.out.println("HP: " + Game.player.life + " Game Over!");
				} else {
					System.out.println("HP: " + Game.player.life);
				}
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
	}

	public boolean isCollidingWithPlayer() {
		int maskxp = 6;
		int maskyp = 6;
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
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

	private void showRectCollisionMask(Graphics g) {
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
