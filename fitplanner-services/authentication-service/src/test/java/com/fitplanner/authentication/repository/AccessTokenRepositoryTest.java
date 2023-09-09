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
public class TokenRepositoryTest {

    @Autowired
    private AccessTokenRepository underTest;

    @Test
    public void findByToken_ExistingAccessToken_AccessToken() {
        // given
        String validToken = "john-token";
        AccessToken token = new AccessToken(
            validToken,
            "johnsmith@gmail.com"
        );
        underTest.save(token);

        // when
        AccessToken result = underTest.findByToken(validToken).orElse(null);

        // then
        assertEquals(result, token);
    }

    @Test
    public void findByToken_NonExistingAccessToken_Null() {
        // given
        String invalidToken = "invalid-token";

        // when
        AccessToken result = underTest.findByToken(invalidToken).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByUserEmail_ExistingUserEmail_AccessToken() {
        // given
        String email = "emmabrown@gmail.com";
        AccessToken token = new AccessToken(
            "emma-token",
            email
        );
        underTest.save(token);

        // when
        AccessToken result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertEquals(result, token);
    }

    @Test
    public void findByUserEmail_NonExistingUserEmail_Null() {
        // given
        String email = "invalid";

        // when
        AccessToken result = underTest.findByUserEmail(email).orElse(null);

        // then
        assertNull(result);
    }
}