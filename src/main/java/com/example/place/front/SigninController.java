package com.example.place.front;

import com.example.place.server.UserRepository;
import com.example.place.server.auth.TokenService;
import com.example.place.server.data.User;
import com.example.place.server.email.EmailService;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder;

/**
 * @author Simon Baslé
 */
@Controller
public class SigninController {

	private final TokenService tokenService;
	private final EmailService emailService;
	private final UserRepository userRepository;
	private final InMemoryUserDetailsManager userDetailsManager;

	public SigninController(TokenService tokenService, EmailService emailService,
			UserRepository userRepository, InMemoryUserDetailsManager manager) {
		this.tokenService = tokenService;
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.userDetailsManager = manager;
	}

	@GetMapping("/signin")
	public String signin () {
		return "signin";
	}

	@PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Mono<String> signin(@RequestBody MultiValueMap<String, String> body) {
		String email = body.getFirst("email");
		System.out.println(email);
		if (email.isEmpty()) {
			return Mono.just("error");
		}
		// verify that the user is in the database.
		return userRepository.findById(email)
		                     //otherwise create it
		                     .switchIfEmpty(userRepository.save(new User(email, System.currentTimeMillis())))
		                     //create a token
		                     .map(it -> tokenService.createToken(it.email))
		                     //send the token by email
		                     .flatMap(token -> emailService.send(email, token)
				                     .map(response -> {
					                     System.out.println("email sent with token " + token);
					                     return "login_link_sent";
				                     })
		                     );
	}

	private void authenticate (String userEmail, String userToken) {
		String token = tokenService.get(userEmail);
		if(!token.equals(userToken)) {
			throw new BadCredentialsException("Invalid auth token for user: " + userEmail);
		}

		userDetailsManager.createUser(
				withDefaultPasswordEncoder()
						.username("admin")
						.password("")
						.roles("USER")
						.build());
	}

	@GetMapping("/signin/{token}")
	public String signin (@RequestParam("uid") String uid, @PathVariable("token") String token) {
		try {
			authenticate(uid, token);
			return "redirect:/";
		}
		catch (BadCredentialsException badCredentialsException) {
			return "invalid_login_link";
		}
	}
}
