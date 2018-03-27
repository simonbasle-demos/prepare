package com.example.place;

import com.example.place.server.CanvasService;
import com.example.place.server.UserRepository;
import com.example.place.server.data.User;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceApplication.class, args);
	}

	@Bean
	protected CommandLineRunner initDB(@Autowired CanvasService canvasService,
			@Autowired UserRepository userRepository) {
		return args -> Mono.when(
				canvasService.loadData().then(),
				userRepository.save(new User(0L, "simonbasle", 0L))
		).block();
	}
}
