package com.fitplanner.nutrition.repository;

import com.fitplanner.nutrition.MongoDBContainerConfig;
import com.fitplanner.nutrition.model.food.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @Test
    public void findByNameIgnoreCase_ExistingName_ProductList() {
        // given
        var name = "product";
        var product1 = new Product(name, 10, 10, 10, 10);
        var product2 = new Product(name + "a", 10, 10, 10, 10);

        var expected = new ArrayList<>();
        expected.add(product1);
        expected.add(product2);

        underTest.save(product1);
        underTest.save(product2);

        // when
        var result = underTest.findByNameIgnoreCase(name).orElse(null);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void findByNameIgnoreCase_NonExistingName_EmptyList() {
        // given
        var name = "any";

        // when
        var result = underTest.findByNameIgnoreCase(name).orElse(Collections.emptyList());

        // then
        assertEquals(result, Collections.emptyList());
    }
}
