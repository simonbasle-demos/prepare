package com.example.place.server;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * @author Simon Baslé
 */
@Service
public class RateLimitingService {

	private Duration delay = Duration.ZERO;

	public void setDelay(Duration newDelay) {
		this.delay = delay;
	}

	public Duration getDelay() {
		return delay;
	}

	public boolean checkRate(LocalDateTime lastUpdate) {
		return lastUpdate.plus(delay).isBefore(LocalDateTime.now());
	}
}
