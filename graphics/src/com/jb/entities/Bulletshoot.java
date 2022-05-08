package com.jb.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;

public class Bulletshoot extends Entity {

	private int CurrTimeLife;
	private int LimitTimeLife;
	private int posXOffset;
	private int posYOffset;
	private double dirX;
	private double dirY;
	private double speed;

	public Bulletshoot(int x, int y, int width, int height, BufferedImage sprite, double dirX, double dirY) {
		super(x, y, width, height, sprite);

		this.dirX = dirX;
		this.dirY = dirY;
		this.speed = 4;
		this.posXOffset = 0;
		this.posYOffset = 0;
		this.CurrTimeLife = 0;
		this.LimitTimeLife = 45;

	}

	public void tick() {
		setX(getX() + (int) (dirX * speed));
		setY(getY() + (int) (dirY * speed));
		CurrTimeLife++;
		if (CurrTimeLife == LimitTimeLife) {
			Game.bulletshoot.remove(this);
			return;
		}
	}

	public void setX(double b) {
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() + getPosXOffset() - Camera.x, this.getY() + getPosYOffset() - Camera.y, this.getWidth(),
				this.getHeight());
	}

	public int getPosXOffset() {
		return this.posXOffset;
	}

	public void setPosXOffset(int offset) {
		this.posXOffset = offset;
	}

	public int getPosYOffset() {
		return this.posYOffset;
	}

	public void setPosYOffset(int offset) {
		this.posYOffset = offset;
	}
}
