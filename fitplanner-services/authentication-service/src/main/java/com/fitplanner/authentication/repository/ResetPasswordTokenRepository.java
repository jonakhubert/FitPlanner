package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.model.ResetPasswordToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends MongoRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String token);
    Optional<ResetPasswordToken> findByUserEmail(String userEmail);
}
