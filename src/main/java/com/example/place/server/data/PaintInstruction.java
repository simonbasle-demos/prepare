package com.example.place.server.data;

/**
 * @author Simon Baslé
 */

public class PaintInstruction {

	int   x;
	int   y;
	Color color;
	Long  userId;

	public PaintInstruction(int x, int y, Color color, Long userId) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.userId = userId;
	}

	public PaintInstruction() {
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
