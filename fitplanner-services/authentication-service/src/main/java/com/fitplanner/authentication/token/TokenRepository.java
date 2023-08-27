package com.fitplanner.authentication.token;

import com.fitplanner.authentication.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
}