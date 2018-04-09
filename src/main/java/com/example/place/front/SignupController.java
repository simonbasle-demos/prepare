package com.example.place.front;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.example.place.server.auth.ITokenSenderService;
import com.example.place.server.auth.TokenService;
import com.example.place.server.data.User;
import com.example.place.server.user.UserRepository;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Simon Baslé
 */
@Controller
public class SignupController {

	private final TokenService        tokenService;
	private final ITokenSenderService tokenSenderService;
	private final UserRepository      userRepository;

	public SignupController(TokenService tokenService, ITokenSenderService tokenSenderService,
			UserRepository userRepository) {
		this.tokenService = tokenService;
		this.tokenSenderService = tokenSenderService;
		this.userRepository = userRepository;
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Mono<String> signup(@RequestBody MultiValueMap<String, String> body) {
		String email = body.getFirst("email");
		if (email.isEmpty()) {
			return Mono.just("error");
		}
		// verify that the user is in the database.
		return userRepository.findById(email)
		                     //otherwise create it
		                     .switchIfEmpty(userRepository.save(new User(email,
				                     LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))))
		                     //create a token
		                     .map(it -> tokenService.createToken(it.email))
		                     //send the token by email
		                     .flatMap(token -> tokenSenderService.send(email, token)
		                                                         .map(response -> "login_link_sent")
		                     );
	}

	@GetMapping("/signup/{token}")
	public String signup(@RequestParam("uid") String uid, @PathVariable("token") String token) {
		if (tokenService.authenticate(uid, token)) {
			return "redirect:/login";
		}
		else {
			return "invalid_login_link";
		}
	}

}
