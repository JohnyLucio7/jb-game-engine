package com.jb.main;

import java.awt.Color;
import java.awt.Graphics;

import com.jb.enums.GameState;

public class FadeIn {

	private int alpha;
	private int alphaDecrement;
	private int fadeCountFrames;
	private int fadeMaxCountFrames;

	private boolean fadeEnd;

	public FadeIn() {
		this.alpha = 255;
		this.alphaDecrement = 25;
		this.fadeCountFrames = 0;
		this.fadeMaxCountFrames = 5;
		this.fadeEnd = false;
	}

	public void tick() {
		fadeCountFrames++;
		fadeCountFrames %= fadeMaxCountFrames;

		if (fadeCountFrames == 0) {
			setAlpha(getAlpha() - alphaDecrement);
		}

		if (getAlpha() > 0) {
			fadeEnd = false;
		} else {
			fadeEnd = true;
		}

		if (fadeEnd) {
			Game.gameState = GameState.NORMAL;
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, getAlpha()));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
	}

	/** getters and setters */

	public void setAlpha(int a) {
		if (a >= 255) {
			this.alpha = 255;
		} else if (a <= 0) {
			this.alpha = 0;
		} else {
			this.alpha = a;
		}
	}

	public int getAlpha() {
		return this.alpha;
	}

	public boolean getFadeEnd() {
		return fadeEnd;
	}

	public void setFadeEnd(boolean fadeEnd) {
		this.fadeEnd = fadeEnd;
	}
}
