package com.example.place.server.auth;

import com.example.place.server.email.EmailProperties;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Simon Baslé
 */
public class EmailSigninService implements ISigninService {

	private final EmailProperties  emailConf;
	private final SigninProperties signinConf;
	private final WebClient        mailSender;

	public EmailSigninService(EmailProperties emailConf, SigninProperties signinConf) {
		this.emailConf = emailConf;
		this.signinConf = signinConf;
		this.mailSender = WebClient.builder()
		                           .baseUrl(emailConf.getApiUrl())
		                           .filter(ExchangeFilterFunctions.basicAuthentication("api", emailConf.getApiKey()))
		                           .build();
	}

	@Override
	public Mono<String> send(String userEmail, String aToken) {
		return mailSender.post()
		                 .uri("/messages")
		                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		                 .accept(MediaType.APPLICATION_JSON )
		                 .body(BodyInserters.fromFormData("from", emailConf.getFrom())
		                                    .with("to", userEmail)
		                                    .with("subject", "Your Collaborative Canvas demo signin link")
		                                    .with("text", String.format("Hello!\nStart painting by signing in here: %s/signin/%s?uid=%s",
				                                    signinConf.getSiteUrl(), aToken, userEmail))
		                 )
		                 .retrieve()
		                 .bodyToMono(String.class);
	}

}
