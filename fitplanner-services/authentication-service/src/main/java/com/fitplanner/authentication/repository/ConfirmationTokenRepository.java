package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUserEmail(String userEmail);
}
