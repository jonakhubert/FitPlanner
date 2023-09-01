package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import org.junit.jupiter.api.BeforeEach;
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
    UserRepository underTest;

    private User user;

    @BeforeEach
    public void beforeEach() {
       user = new User(
            "John", "Smith",
            "johnsmith@gmail.com",
            "1234",
            Role.USER
        );
    }

    @Test
    public void shouldReturnExistingUserWhenExistingEmailIsProvided() {
        // given
        String email = "johnsmith@gmail.com";
        underTest.save(user);

        // when
        User result = underTest.findByEmail(email).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void shouldReturnNullWhenNonExistingEmailIsProvided() {
        // given
        String email = "non-existing";

        // when
        User result = underTest.findByEmail(email).orElse(null);

        // then
        assertNull(result);
    }
}