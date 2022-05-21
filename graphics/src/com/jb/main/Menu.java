package com.jb.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.jb.enums.GameState;

public class Menu {

	public String[] options = { "novo jogo", "carregar", "sair" };

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	private boolean up;
	private boolean down;
	private boolean enter;
	private boolean pause = false;

	public void tick() {
		if (up) {
			setUp(false);
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}

		if (down) {
			setDown(false);
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}

		if (enter) {
			setEnter(false);
			if (options[currentOption] == "novo jogo") {
				Game.gameState = GameState.NORMAL;
				setPause(false);
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial", Font.BOLD, 36));
		// title
		g.drawString("Clone Zelda", (Game.WIDTH * Game.SCALE) / 2 - 125, (Game.HEIGHT * Game.SCALE) / 2 - 120);

		// options
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.drawString(!(pause) ? "novo jogo" : "continuar", (Game.WIDTH * Game.SCALE) / 2 - 125,
				(Game.HEIGHT * Game.SCALE) / 2 - 70);
		g.drawString("carregar", (Game.WIDTH * Game.SCALE) / 2 - 125, (Game.HEIGHT * Game.SCALE) / 2 - 30);
		g.drawString("sair", (Game.WIDTH * Game.SCALE) / 2 - 125, (Game.HEIGHT * Game.SCALE) / 2 + 10);

		if (options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 160, (Game.HEIGHT * Game.SCALE) / 2 - 70);
			return;
		}

		if (options[currentOption] == "carregar") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 160, (Game.HEIGHT * Game.SCALE) / 2 - 30);
			return;
		}

		if (options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 160, (Game.HEIGHT * Game.SCALE) / 2 + 10);
			return;
		}
	}

	/** getters and setters */

	public boolean getUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean getDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean getEnter() {
		return enter;
	}

	public void setEnter(boolean enter) {
		this.enter = enter;
	}

	public boolean getPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
