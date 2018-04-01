package com.example.place.server.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.example.place.server.canvas.CanvasRepository;
import com.example.place.server.canvas.CanvasService;
import com.example.place.server.data.Color;
import com.example.place.server.data.Pixel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon Baslé
 */
@RestController
public class AdminController {

	private final CanvasRepository pixelRepository;
	private final CanvasService    canvasService;

	public AdminController(CanvasRepository repository, CanvasService service) {
		pixelRepository = repository;
		canvasService = service;
	}

	@GetMapping("/admin/clearDB")
	public Mono<String> clearDB() {
		return Mono.when(pixelRepository.deleteAll(), canvasService.clearCanvas().then())
		           .then(Mono.fromRunnable(canvasService::resendCanvas))
		           .thenReturn("Deleted Canvas from DB");
	}

	@GetMapping("/admin/clear")
	public Mono<String> clear() {
		return canvasService.clearCanvas()
		                    .then(Mono.fromRunnable(canvasService::resendCanvas))
		                    .thenReturn("Cleared Canvas");
	}

	@GetMapping("/admin/paint")
	public Mono<Void> paint(@RequestParam int x,
			@RequestParam int y,
			@RequestParam int r,
			@RequestParam int g,
			@RequestParam int b,
			@RequestParam(required = false, defaultValue = "255") int a) {
		return canvasService.setPixelAt(x, y, new Color(r, g, b, a));
	}

	@GetMapping("/admin/paintSpring")
	public Mono<String> paintSpringLogo() {
		final BufferedImage springLogo;
		try {
			springLogo = ImageIO.read(this.getClass().getClassLoader()
					.getResourceAsStream("images/spring.png"));
		}
		catch (IOException e) {
			return Mono.error(e);
		}

		final int width = springLogo.getWidth();
		final int height = springLogo.getHeight();

		return Flux.<Pixel>create(sink -> {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = springLogo.getRGB(x, y);
					int alpha = (pixel >> 24) & 0xff;
					int red = (pixel >> 16) & 0xff;
					int green = (pixel >> 8) & 0xff;
					int blue = (pixel) & 0xff;

					sink.next(new Pixel(x, y,
							alpha == 0 ? Color.WHITE :
									new Color(red, green, blue, alpha)));
				}
			}
			sink.complete();
		})
		           .flatMap(pixel -> canvasService.setPixelAt(
				           pixel.getX(),
				           pixel.getY(),
				           pixel.getColor()))
		           .then(Mono.just("painted"));
	}

}
