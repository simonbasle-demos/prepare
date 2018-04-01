package com.example.place.server.auth;

import reactor.core.publisher.Mono;

/**
 * @author Simon Baslé
 */
public class OffbandSigningService implements ISigninService {

	private final SigninProperties conf;

	public OffbandSigningService(SigninProperties conf) {
		this.conf = conf;
	}

	@Override
	public Mono<String> send(String userEmail, String aToken) {
		return Mono.fromRunnable(() -> System.out.println(
				String.format("Sign in of %s -> %s (%s/signin/%s?uid=%s)", userEmail, aToken,
						conf.getSiteUrl(), aToken, userEmail)))
		           .thenReturn("ok");
	}
}
