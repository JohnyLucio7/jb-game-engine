package com.jb.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.Tile;

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

	private int maskx;
	private int masky;
	private int maskw;
	private int maskh;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}

	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}

	public int getMaskX() {
		return this.maskx;
	}

	public int getMaskY() {
		return this.masky;
	}

	public int getMaskW() {
		return this.maskw;
	}

	public int getMaskH() {
		return this.maskh;
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
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

	public static boolean isCollinding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getMaskW(),
				e1.getMaskH());
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskW(),
				e2.getMaskH());

		return e1Mask.intersects(e2Mask);
	}

	public static boolean isCollindingWithTile(Entity e, Tile t) {
		Rectangle EMask = new Rectangle(e.getX() + e.getMaskX(), e.getY() + e.getMaskY(), e.getMaskW(), e.getMaskH());
		Rectangle TMask = new Rectangle(t.getX(), t.getY(), 16, 16);

		return EMask.intersects(TMask);
	}

}
