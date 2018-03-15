package com.example.place.server;

import com.example.place.server.data.User;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Simon Baslé
 */
public interface UserRepository extends ReactiveMongoRepository<User, Long> {

}
