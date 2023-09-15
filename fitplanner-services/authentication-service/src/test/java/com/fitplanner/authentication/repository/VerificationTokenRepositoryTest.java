package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository underTest;

    @Test
    public void findByToken_ExistingConfirmationToken_ConfirmationToken() {
        //given
        var token = "conf_token";
        var confirmationToken = new ConfirmationToken(
            token,
            null,
            null,
            "user_email"
        );
        underTest.save(confirmationToken);

        // when
        var result = underTest.findByToken(token).orElse(null);

        // then
        assertEquals(result, confirmationToken);
    }

    @Test
    public void findByToken_NonExistingConfirmationToken_Null() {
        // given
        var invalidToken = "invalid-token";

        // when
        var result = underTest.findByToken(invalidToken).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByUserEmail_ExistingUserEmail_ConfirmationToken() {
        // given
        var email = "any@gmail.com";
        var confirmationToken = new ConfirmationToken(
            "any-token",
            null,
            null,
            email
        );
        underTest.save(confirmationToken);

        // when
        var result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertEquals(result, confirmationToken);
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