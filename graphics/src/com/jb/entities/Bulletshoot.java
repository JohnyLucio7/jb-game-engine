package com.jb.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.Tile;
import com.jb.world.WallTile;
import com.jb.world.World;

public class Bulletshoot extends Entity {

	private int CurrTimeLife;
	private int LimitTimeLife;
	private double dirX;
	private double dirY;
	private double speed;
	private boolean enableShowRectBorderCollisionMask = false;

	public Bulletshoot(int x, int y, int width, int height, BufferedImage sprite, double dirX, double dirY) {
		super(x, y, width, height, sprite);

		this.dirX = dirX;
		this.dirY = dirY;
		this.speed = 4;
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

		isCollidingWithWall();
	}

	public void setX(double b) {
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, this.getWidth(), this.getHeight());

		if (enableShowRectBorderCollisionMask) {
			showRectBorderCollisionMask(g);
		}
	}

	private void isCollidingWithWall() {
		for (int i = 0; i < World.tiles.length; i++) {
			Tile t = World.tiles[i];
			if (t instanceof WallTile) {
				if (Entity.isCollindingWithTile(this, t)) {
					Game.bulletshoot.remove(this);
				}
			}
		}
	}

	private void showRectBorderCollisionMask(Graphics g) {
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
