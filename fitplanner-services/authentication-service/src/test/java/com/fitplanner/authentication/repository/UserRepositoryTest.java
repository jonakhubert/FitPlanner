package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.tokens.ResetPasswordToken;
import com.fitplanner.authentication.model.tokens.VerificationToken;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    public void findByEmail_ExistingEmail_User() {
        // given
        var email = "johnsmith@gmail.com";
        var user = new User("John", "Smith", email, "1234", Role.USER);

        underTest.save(user);

        // when
        var result = underTest.findByEmail(email).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void findByEmail_NonExistingEmail_Null() {
        // given
        var email = "non-existing";

        // when
        var result = underTest.findByEmail(email).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByAccessToken_ExistingToken_User() {
        // given
        var token = new AccessToken("access-token");
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        user.setAccessToken(token);
        underTest.save(user);

        // when
        var result = underTest.findByAccessToken(token.getToken()).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void findByAccessToken_NonExistingToken_Null() {
        // given
        var token = "non-existing-access-token";
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        underTest.save(user);

        // when
        var result = underTest.findByAccessToken(token).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByVerificationToken_ExistingToken_User() {
        // given
        var token = new VerificationToken("verification-token", null, null);
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        user.setVerificationToken(token);
        underTest.save(user);

        // when
        var result = underTest.findByVerificationToken(token.getToken()).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void findByVerificationToken_NonExistingToken_Null() {
        // given
        var token = new VerificationToken("non-existing-verification-token", null, null);
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        underTest.save(user);

        // when
        var result = underTest.findByVerificationToken(token.getToken()).orElse(null);

        // then
        assertNull(result);
    }

    @Test
    public void findByResetPasswordToken_ExistingToken_User() {
        // given
        var token = new ResetPasswordToken("reset-password-token", null, null);
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        user.setResetPasswordToken(token);
        underTest.save(user);

        // when
        var result = underTest.findByResetPasswordToken(token.getToken()).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void findByResetPasswordToken_NonExistingToken_Null() {
        // given
        var token = new ResetPasswordToken("non-existing-reset-password-token", null, null);
        var user = new User("John", "Smith", "johnsmith@gmail.com", "1234", Role.USER);

        underTest.save(user);

        // when
        var result = underTest.findByResetPasswordToken(token.getToken()).orElse(null);

        // then
        assertNull(result);
    }
}