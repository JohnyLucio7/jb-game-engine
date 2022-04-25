package com.jb.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
						tiles[x + (y * WIDTH)] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
						// player
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
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Tile tile = tiles[x + (y * WIDTH)];
				tile.render(g);
			}
		}
	}
}
