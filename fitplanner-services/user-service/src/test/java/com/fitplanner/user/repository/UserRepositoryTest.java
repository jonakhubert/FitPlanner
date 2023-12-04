package com.fitplanner.user.repository;

import com.fitplanner.user.MongoDBContainerConfig;
import com.fitplanner.user.model.user.User;
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
        var email = "any@gmail.com";
        var user = new User();
        user.setEmail(email);

        underTest.save(user);

        // when
        var result = underTest.findByEmail(email).orElse(null);

        // then
        assertEquals(user, result);
    }

    @Test
    public void findByEmail_NonExistingEmail_Null() {
        // given
        var email = "non-exist";

        // when
        var result = underTest.findByEmail(email).orElse(null);

        // then
        assertNull(result);
    }
}
