package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.model.accesstoken.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String token);
    Optional<AccessToken> findByUserEmail(String userEmail);
}