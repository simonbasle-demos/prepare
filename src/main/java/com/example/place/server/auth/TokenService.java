package com.example.place.server.auth;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

/**
 * @author Simon Baslé
 */
@Service
public class TokenService {

	private static final int TOKEN_BYTE_SIZE = 16;

	private final Map<Object, Object> store = Caffeine.newBuilder()
	                                                  .expireAfterWrite(Duration.ofMinutes(30))
	                                                  .build()
	                                                  .asMap();

	private final SecureRandom random = new SecureRandom();


	public String createToken(String userEmail) {
		Objects.requireNonNull(userEmail,"user email can't be null");
		byte bytes[] = new byte[TOKEN_BYTE_SIZE];
		random.nextBytes(bytes);
		String token = String.valueOf(Hex.encode(bytes));
		store.put(userEmail, token);
		return token;
	}

	public String get(String userEmail) {
		Objects.requireNonNull(userEmail,"user email can't be null");
		Object token = store.get(userEmail);
		if (token == null) return null;
		return String.valueOf(token);
	}

	public boolean authenticate(String uid, String token) {
		if (uid == null || token == null) return false;
		String expectedToken = get(uid);
		if (expectedToken == null) return false;
		return token.equals(expectedToken);
	}

}
