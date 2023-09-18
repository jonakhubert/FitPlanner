package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class AccessTokenRepositoryTest {

    @Autowired
    private AccessTokenRepository underTest;

    @Test
    public void findByToken_ExistingToken_AccessToken() {
        // given
        var validToken = "john-token";
        var token = new AccessToken(
            validToken,
            "johnsmith@gmail.com"
        );
        underTest.save(token);

        // when
        var result = underTest.findByToken(validToken).orElse(null);

        // then
        assertEquals(result, token);
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
    public void findByUserEmail_ExistingUserEmail_AccessToken() {
        // given
        var email = "emmabrown@gmail.com";
        var token = new AccessToken(
            "emma-token",
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
        var email = "invalid";

        // when
        var result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertNull(result);
    }
}