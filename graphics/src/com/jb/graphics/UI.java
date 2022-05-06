package com.jb.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.jb.main.Game;

public class UI {
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(7, 3, 72, 9);
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 7);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.getLife() / Game.player.getMaxLife()) * 70), 7);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 7));
		// g.drawString((int) Game.player.getLife() + "/" + Game.player.getMaxLife(),
		// 25, 10);
		g.drawString(alignHpStr() + "/" + Game.player.getMaxLife(), 25, 10);
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
}
