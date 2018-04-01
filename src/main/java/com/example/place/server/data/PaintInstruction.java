package com.example.place.server.data;

/**
 * @author Simon Baslé
 */
public class PaintInstruction {

	private int    x;
	private int    y;
	private Color  color;
	private String userId;
	private String userToken;

	public PaintInstruction(int x, int y, Color color, String userId, String userToken) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.userId = userId;
		this.userToken = userToken;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
