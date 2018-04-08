package com.example.place.server.auth;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.benmanes.caffeine.cache.Caffeine;
import reactor.core.publisher.Mono;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

/**
 * @author Simon Baslé
 */
@Service
public class TokenService implements ReactiveUserDetailsService {

	private static final int TOKEN_BYTE_SIZE = 16;

	private final Map<Object, Object> store = Caffeine.newBuilder()
	                                                  .expireAfterWrite(Duration.ofMinutes(30))
	                                                  .build()
	                                                  .asMap();

	private final Map<String, UserDetails> userDetailsMap = new HashMap<>();

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
		if (!expectedToken.equals(token)) return false;

		userDetailsMap.put(uid, User.withDefaultPasswordEncoder()
		                            .username(uid)
		                            .password(token)
		                            .roles("USER")
		                            .build());
		return true;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		UserDetails original = userDetailsMap.get(username);
		if (original == null) {
			return Mono.empty();
		}
		return Mono.just(User.withUserDetails(original).build());
	}
}
