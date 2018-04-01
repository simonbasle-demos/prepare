package com.example.place.front;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Simon Baslé
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String home(@RequestParam(required = false) String email,
			@RequestParam(required = false) String token,
			Map<String, Object> model) {
		if (email != null && token != null) {
			model.put("email", email);
			model.put("token", token);
		}
		return "canvas";
	}

}
