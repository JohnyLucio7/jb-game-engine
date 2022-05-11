package com.jb.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.Direction;
import com.jb.world.World;

public class Enemy extends Entity {

	/** enemy attributes */

	private int life = 10;
	private int maxLife = 10;
	private int speed = 1;
	private Direction direction = Direction.RIGHT;

	/** collision and information rendering */

	private boolean enableShowLife = true;
	private boolean enableRectCollisionMask = false;
	private boolean enableRectCollisionWithPlayer = false;
	private boolean enableRectBorderColissionWithPlayer = false;

	/** control booleans */

	private boolean isDamaged = false;

	/** animation variables */

	private int enemyAnimFrames = 0;
	private int enemyAnimeMaxFrames = 5;
	private int enemyAnimSpriteIndex = 0;
	private int enemyAnimeMaxSpriteIndex = 4;
	private int enemyAnimIsDamagedFrames = 0;
	private int enemyAnimMaxIsDamagedFrames = 8;

	/** image vectors */

	private BufferedImage[] enemyLeft;
	private BufferedImage[] enemyRight;
	private BufferedImage[] enemyLeftDamage;
	private BufferedImage[] enemyRightDamage;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		enemyRight = new BufferedImage[4];
		enemyLeft = new BufferedImage[4];
		enemyRightDamage = new BufferedImage[4];
		enemyLeftDamage = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			enemyRight[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, width, height);
			enemyLeft[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, width, height);
			enemyRightDamage[i] = Game.spritesheet.getSprite(32 + (i * 16), 96, width, height);
			enemyLeftDamage[i] = Game.spritesheet.getSprite(32 + (i * 16), 112, width, height);
		}
	}

	public void tick() {

		if (!isCollidingWithPlayer()) {
			if (Game.rand.nextInt(100) < 50) {

				if (this.getX() < Game.player.getX() && World.isFree(this.getX() + speed, this.getY())
						&& !isColliding(this.getX() + speed, this.getY())) {
					//
					this.setX(this.getX() + speed);
					direction = Direction.RIGHT;
				} else if (this.getX() > Game.player.getX() && World.isFree(this.getX() - speed, this.getY())
						&& !isColliding(this.getX() - speed, this.getY())) {
					//
					this.setX(this.getX() - speed);
					direction = Direction.LEFT;
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
				Game.player.setIsDamaged(true);
			}
		}

		if (enemyAnimFrames >= enemyAnimeMaxFrames) {
			enemyAnimSpriteIndex++;
			enemyAnimSpriteIndex %= enemyAnimeMaxSpriteIndex;
		}
		enemyAnimFrames %= enemyAnimeMaxFrames;
		enemyAnimFrames++;

		this.isCollidingWithBullet();

		if (this.getLife() <= 0) {
			destroySelf();
		}

		if (this.getIsDamaged()) {
			enemyAnimIsDamagedFrames++;
			enemyAnimIsDamagedFrames %= enemyAnimMaxIsDamagedFrames;
			if (enemyAnimIsDamagedFrames == 0) {
				this.setIsDamaged(false);
			}
		}

	}

	public void render(Graphics g) {

		if (!this.getIsDamaged()) {
			if (direction == Direction.RIGHT) {
				g.drawImage(enemyRight[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (direction == Direction.LEFT) {
				g.drawImage(enemyLeft[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {
			if (direction == Direction.RIGHT) {
				g.drawImage(enemyRightDamage[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y,
						null);
			} else if (direction == Direction.LEFT) {
				g.drawImage(enemyLeftDamage[enemyAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y,
						null);
			}
		}

		if (enableRectCollisionMask) {
			this.showRectCollisionMask(g);
		}

		if (enableRectCollisionWithPlayer) {
			this.showRectCollisionWithPlayer(g);
		}

		if (enableRectBorderColissionWithPlayer) {
			this.showRectBorderColissionWithPlayer(g);
		}

		if (enableShowLife) {
			this.showLife(g);
		}
	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	private void showLife(Graphics g) {

		g.setColor(Color.black);
		setMask(3, -1, 10, 12);
		g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, 10, 4);
		g.setColor(Color.green);
		setMask(4, 0, 10, 12);
		double currentLife = life;
		g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, (int) (currentLife / maxLife * 8),
				2);
	}

	public void isCollidingWithBullet() {
		for (int i = 0; i < Game.bulletshoot.size(); i++) {
			Entity e = Game.bulletshoot.get(i);
			if (e instanceof Bulletshoot) {
				if (Entity.isCollinding(this, e)) {
					this.setIsDamaged(true);
					this.setLife(this.getLife() - 1);
					Game.bulletshoot.remove(i);
					return;
				}
			}
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

	/** getters and setters */

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		if (life <= 0)
			this.life = 0;
		else if (life >= this.maxLife)
			this.life = maxLife;
		else
			this.life = life;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		if (maxLife > 0) {
			this.maxLife = maxLife;
		}
	}

	public boolean getIsDamaged() {
		return isDamaged;
	}

	public void setIsDamaged(boolean isDamaged) {
		this.isDamaged = isDamaged;
	}

}
