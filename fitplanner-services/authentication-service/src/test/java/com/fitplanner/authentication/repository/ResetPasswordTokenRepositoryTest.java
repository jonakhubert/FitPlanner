package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.ResetPasswordToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class ResetPasswordTokenRepositoryTest {

    @Autowired
    private ResetPasswordTokenRepository underTest;

    @Test
    public void findByToken_ExistingToken_ResetPasswordToken() {
        //given
        var token = "token";
        var resetPasswordToken = new ResetPasswordToken(
            token,
            null,
            null,
            "user_email"
        );

        underTest.save(resetPasswordToken);

        // when
        var result = underTest.findByToken(token).orElse(null);

        // then
        assertEquals(result, resetPasswordToken);
    }

    @Test
    public void findByToken_NonExistingToken_Null() {
        //given
        var token = "invalid-token";

        // when
        var result = underTest.findByToken(token).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByUserEmail_ExistingUserEmail_ResetPasswordToken() {
        // given
        var email = "any@gmail.com";
        var token = new ResetPasswordToken(
            "any-token",
            null,
            null,
            email
        );

        underTest.save(token);

        // when
        var result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertEquals(result, token);
    }

    @Test
    public void findByUserEmail_NonExistingUserEmail_Null() {
        // given
        var email = "any@gmail.com";

        // when
        var result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertNull(result);
    }
}
