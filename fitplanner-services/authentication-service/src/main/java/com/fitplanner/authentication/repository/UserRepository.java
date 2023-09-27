package com.fitplanner.authentication.repository;

import java.util.Optional;

import com.fitplanner.authentication.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("{'accessToken.token': ?0}")
    Optional<User> findByAccessToken(String token);

    @Query("{'verificationToken.token': ?0}")
    Optional<User> findByVerificationToken(String token);

    @Query("{'resetPasswordToken.token': ?0}")
    Optional<User> findByResetPasswordToken(String token);
}
