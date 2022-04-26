package com.jb.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jb.entities.Bullet;
import com.jb.entities.Entity;
import com.jb.entities.Lifepack;
import com.jb.entities.Weapon;
import com.jb.main.Game;

public class World {

	private Tile[] tiles;
	public static int WIDTH, HEIGHT;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
					switch (pixels[x + (y * map.getWidth())]) {
					case 0xFF000000:
						// black, floor
						tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
						break;
					case 0xFFFFFFFF:
						// white, wall
						tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_WALL);
						break;
					case 0xFF0000FF:
						// tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
						// blue, player
						Game.player.setX(x * 16);
						Game.player.setY(y * 16);
						break;
					case 0xFF00FF00:
						// green, lifepack
						Game.entities.add(new Lifepack(x * 16, y * 16, 16, 16, Entity.LIFEPACK_EN));
						break;
					case 0xFFFFFF00:
						// yellow, Bullet
						Game.entities.add(new Bullet(x * 16, y * 16, 16, 16, Entity.BULLET_EN));
						System.out.println("Amarelo");
						break;
					case 0xFFFF00FF:
						// rose, Weapon
						Game.entities.add(new Weapon(x * 16, y * 16, 16, 16, Entity.WEAPON_EN));
						break;
					case 0xFFFF0000:
						// red, Enemy
						Game.entities.add(new Entity(x * 16, y * 16, 16, 16, Entity.ENEMY_EN));
						break;
					default:
						tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		for (int x = xstart; x <= xfinal; x++) {
			for (int y = ystart; y <= yfinal; y++) {
				if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT)
					continue;
				Tile tile = tiles[x + (y * WIDTH)];
				tile.render(g);
			}
		}
	}
}
