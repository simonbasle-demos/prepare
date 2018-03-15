package com.example.place.server.data;

import org.springframework.data.annotation.PersistenceConstructor;

/**
 * @author Simon Baslé
 */
public class Color {

	public static final Color WHITE = new Color(255,255,255, 255);
	public static final Color BLACK = new Color(0,0,0, 255);

	public final int red;
	public final int green;
	public final int blue;
	public final int alpha;

	@PersistenceConstructor
	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	@Override
	public String toString() {
		return "(" + red + "," + green + "," + blue + ")";
	}
}
