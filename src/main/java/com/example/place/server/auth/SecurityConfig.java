package com.example.place.server.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Simon Baslé
 */
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.httpBasic().disable()
		           .csrf().disable()
		           .authorizeExchange()
				.pathMatchers(HttpMethod.POST, "/paint/").hasRole("USER")
				.anyExchange().permitAll()
				.and()
				.build();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() throws Exception {
		// ensure the passwords are encoded properly
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		return manager;
	}

}
