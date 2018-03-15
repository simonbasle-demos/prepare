package com.example.place.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Simon Baslé
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "canvas";
	}

}
