package com.example.place;

import com.example.place.server.canvas.CanvasService;
import com.example.place.server.user.UserRepository;
import com.example.place.server.auth.SigninProperties;
import com.example.place.server.data.User;
import com.example.place.server.email.EmailProperties;
import com.example.place.server.auth.EmailSigninService;
import com.example.place.server.auth.ISigninService;
import com.example.place.server.auth.OffbandSigningService;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceApplication.class, args);
	}

	@Bean
	@ConditionalOnProperty(value = "signin.method", havingValue = "offband")
	protected ISigninService offbandSigninService(SigninProperties signinConf) {
		return new OffbandSigningService(signinConf);
	}

	@Bean
	@ConditionalOnProperty(value = "signin.method", havingValue = "email")
	protected ISigninService emailSigninService(SigninProperties signinConf,
			EmailProperties emailConf) {
		return new EmailSigninService(emailConf, signinConf);
	}

	@Bean
	protected CommandLineRunner initDB(@Autowired CanvasService canvasService,
			@Autowired UserRepository userRepository) {
		return args -> Mono.when(
				canvasService.loadData().then(),
				userRepository.save(new User("simon.basle@dev.null", 0L))
		).block();
	}

}
