package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.VerificationToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository underTest;

    @Test
    public void findByToken_ExistingToken_VerificationToken() {
        //given
        var token = "conf_token";
        var verificationToken = new VerificationToken(
            token,
            null,
            null,
            "user_email"
        );

        underTest.save(verificationToken);

        // when
        var result = underTest.findByToken(token).orElse(null);

        // then
        assertEquals(result, verificationToken);
    }

    @Test
    public void findByToken_NonExistingToken_Null() {
        // given
        var invalidToken = "invalid-token";

        // when
        var result = underTest.findByToken(invalidToken).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByUserEmail_ExistingUserEmail_VerificationToken() {
        // given
        var email = "any@gmail.com";
        var verificationToken = new VerificationToken(
            "any-token",
            null,
            null,
            email
        );

        underTest.save(verificationToken);

        // when
        var result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertEquals(result, verificationToken);
    }

    @Test
    public void findByUserEmail_NonExistingUserEmail_Null() {
        // given
        var invalidUserEmail = "invalid-email";

        // when
        var result = underTest.findByUserEmail(invalidUserEmail).orElse(null);

        // then
        assertNull(result);
    }
}