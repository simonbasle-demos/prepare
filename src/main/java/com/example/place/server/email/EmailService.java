package com.example.place.server.email;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Simon Baslé
 */
@Service
public class EmailService {

	@Value("${app.rootUrl}")
	private String rootUrl;

	private final EmailProperties conf;
	private final WebClient mailSender;

	public EmailService(EmailProperties conf) {
		this.conf = conf;
		this.mailSender = WebClient.builder()
		                           .baseUrl(conf.getBaseUrl())
		                           .filter(ExchangeFilterFunctions.basicAuthentication("api", conf.getApiKey()))
		                           .build();
	}

	public Mono<String> send(String userEmail, String aToken) {
		return mailSender.post()
		                 .uri("/messages")
		                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		                 .accept(MediaType.APPLICATION_JSON )
		                 .body(BodyInserters.fromFormData("from", conf.getFrom())
		                                    .with("to", userEmail)
		                                    .with("subject", "Your Collaborative Canvas demo signin link")
		                                    .with("text", String.format("Hello!\nStart painting by signing in here: %s/signin/%s?uid=%s",rootUrl, aToken, userEmail))
		                 )
		                 .retrieve()
		                 .bodyToMono(String.class)
		                 .doOnNext(System.out::println);
	}

}
