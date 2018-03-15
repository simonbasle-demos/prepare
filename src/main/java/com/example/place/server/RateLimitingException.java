package com.example.place.server;

/**
 * @author Simon Baslé
 */
public class RateLimitingException extends IllegalStateException {

	public RateLimitingException(String message) {
		super(message);
	}
}
