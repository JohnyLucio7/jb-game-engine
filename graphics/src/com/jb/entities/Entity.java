package com.jb.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;

public class Entity {
	
	public static final BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(96, 64, 16, 16);
	public static final BufferedImage WEAPON_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static final BufferedImage BULLET_EN = Game.spritesheet.getSprite(112, 64, 16, 16);
	public static final BufferedImage ENEMY_EN = Game.spritesheet.getSprite(32, 64, 16, 16);
	
	private int x;
	private int y;
	private int width;
	private int height;
	private BufferedImage sprite;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int w) {
		this.width = w;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int h) {
		this.height = h;
	}

	public void tick() {
	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}

}
