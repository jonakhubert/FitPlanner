package com.fitplanner.authentication.repository;

import com.fitplanner.authentication.MongoDBContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class UserRepositoryTest {

    @Autowired
    TokenRepository userRepository;

    @Test
    public void findByEmail() {

    }
}