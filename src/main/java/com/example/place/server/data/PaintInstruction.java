package com.example.place.server.data;

/**
 * @author Simon Baslé
 */

public class PaintInstruction {

	public final int   x;
	public final int   y;
	public final Color color;
	public final Long  userId;

	public PaintInstruction(int x, int y, Color color, Long userId) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.userId = userId;
	}
}
