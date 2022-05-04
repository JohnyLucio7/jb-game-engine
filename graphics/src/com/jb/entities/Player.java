package com.jb.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.World;

public class Player extends Entity {

	private boolean right, up, left, down;
	private int speed = 2;
	private int dir = 0, dir_left = 1, dir_right = 0; // substituir por enum

	private int playerAnimFrames = 0;
	private int playerAnimeMaxFrames = 5;
	private int playerAnimSpriteIndex = 0;
	private int playerAnimeMaxSpriteIndex = 4;
	private boolean moved = false;

	private BufferedImage[] playerLeft;
	private BufferedImage[] playerRight;

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

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

	}

	public void render(Graphics g) {

		if (dir == dir_right) {
			g.drawImage(playerRight[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == dir_left) {
			g.drawImage(playerLeft[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

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