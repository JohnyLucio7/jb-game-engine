package com.jb.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.World;

public class Player extends Entity {

	private boolean right, up, left, down;
	private int speed = 1;
	private int dir = 0, dir_left = 1, dir_right = 0; // substituir por enum

	private int playerAnimFrames = 0;
	private int playerAnimeMaxFrames = 5;
	private int playerAnimSpriteIndex = 0;
	private int playerAnimeMaxSpriteIndex = 4;
	private boolean moved = false;
	private boolean enableRectCollisionMask = true;

	private BufferedImage[] playerLeft;
	private BufferedImage[] playerRight;

	private double life = 100;
	private int maxLife = 100;

	private int ammo = 0;
	private int maxAmmo = 18;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		playerLeft = new BufferedImage[4];
		playerRight = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			playerRight[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, width, height);
			playerLeft[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, width, height);
		}
	}

	public void tick() {

		moved = false;

		if (right && World.isFree(this.getX() + speed, this.getY())) {
			moved = true;
			dir = dir_right;
			setX(getX() + speed);
		} else if (left && World.isFree(this.getX() - speed, this.getY())) {
			moved = true;
			dir = dir_left;
			setX(getX() - speed);
		}

		if (up && World.isFree(this.getX(), this.getY() - speed)) {
			moved = true;
			setY(getY() - speed);
		} else if (down && World.isFree(this.getX(), this.getY() + speed)) {
			moved = true;
			setY(getY() + speed);
		}

		if (moved) {
			if (playerAnimFrames >= playerAnimeMaxFrames) {
				playerAnimSpriteIndex++;
				playerAnimSpriteIndex %= playerAnimeMaxSpriteIndex;
			}
			playerAnimFrames %= playerAnimeMaxFrames;
			playerAnimFrames++;
		} else {
			playerAnimSpriteIndex = 0;
		}

		checkCollisionWithLifepack();
		checkCollisionWithBullet();

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

	}

	public void render(Graphics g) {

		if (dir == dir_right) {
			g.drawImage(playerRight[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == dir_left) {
			g.drawImage(playerLeft[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		if (enableRectCollisionMask) {
			g.setColor(new Color(0, 0, 255, 100));
			g.fillRect(getX() - Camera.x, getY() - Camera.y, 16, 16);
		}

	}

	public void checkCollisionWithBullet() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Bullet) {
				if (Entity.isCollinding(this, current)) {
					this.setAmmo(getAmmo() + ((Bullet) current).getAmmo());
					Game.entities.remove(current);
				}
			}
		}
	}

	public void checkCollisionWithLifepack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Lifepack) {
				if (Entity.isCollinding(this, current)) {
					this.setLife(this.getLife() + ((Lifepack) current).getLifePoints());
					Game.entities.remove(current);
				}
			}
		}
	}

	public int getAmmo() {
		return this.ammo;
	}

	public void setAmmo(int a) {
		if (a < 0) {
			this.ammo = 0;
		} else if (a >= maxAmmo) {
			this.ammo = maxAmmo;
		} else {
			this.ammo = a;
		}
	}

	public int getMaxLife() {
		return this.maxLife;
	}

	public double getLife() {
		return this.life;
	}

	public void setLife(double nl) {

		if (nl <= 0)
			this.life = 0;
		else if (nl >= this.maxLife)
			this.life = maxLife;
		else
			this.life = nl;

	}

	public boolean getRight() {
		return this.right;
	}

	public void setRight(boolean b) {
		this.right = b;
	}

	public boolean getLeft() {
		return this.left;
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
