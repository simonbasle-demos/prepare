package com.example.place.server.data;

import org.springframework.data.annotation.Id;

/**
 * @author Simon Baslé
 */
public class User {

	@Id
	public final Long id;
	public final String login;
	public long lastUpdate;

	public User(Long id, String login, long lastUpdate) {
		this.id = id;
		this.login = login;
		lastUpdate = lastUpdate;
	}
}
