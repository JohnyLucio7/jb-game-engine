package com.jb.world;

public class Camera {
	public static int x = 0;
	public static int y = 0;

	public static int clamp(int xCurrent, int xMin, int xMax) {
		if (xCurrent < xMin)
			xCurrent = xMin;
		if (xCurrent > xMax)
			xCurrent = xMax;

		return xCurrent;
	}
}
