package com.jb.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.jb.enums.GameState;
import com.jb.main.Game;

public class UI {
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(7, 3, 72, 9);
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 7);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.getLife() / Game.player.getMaxLife()) * 70), 7);
	}

	public void renderWithoutScale(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString(alignHpStr() + "/" + Game.player.getMaxLife(), 27 * Game.SCALE, 30);
		g.drawString("Munição " + Game.player.getWeaponAmmoInClip() + "/" + Game.player.getAmmo(), Game.SCALE * 185,
				30);
	}

	private String alignHpStr() {
		String res = "" + (int) Game.player.getLife();
		double life = Game.player.getLife();

		if (life < 100.0 && life > 10.0) {
			res = "0" + (int) Game.player.getLife();
		} else if (life < 10.0 && life > 0.0) {
			res = "00" + (int) Game.player.getLife();
		} else if (life <= 0.0) {
			res = "000";
		}

		return res;
	}

	public void gameOverUI(Graphics g) {
		if (Game.gameState == GameState.GAMEOVER) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.drawString("Game Over", (Game.WIDTH * Game.SCALE) / 2 - 95, (Game.HEIGHT * Game.SCALE) / 2);
			g.setFont(new Font("arial", Font.BOLD, 32));
			if (Game.enableShowMessageGameOver) {
				g.drawString(">Enter para reiniciar<", (Game.WIDTH * Game.SCALE) / 2 - 165,
						(Game.HEIGHT * Game.SCALE) / 2 + 40);
			}
		}
	}
}
