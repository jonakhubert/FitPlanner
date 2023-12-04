package com.fitplanner.nutrition.repository;

import com.fitplanner.nutrition.model.food.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{'name': {$regex : ?0, $options: 'i'}}")
    Optional<List<Product>> findByNameIgnoreCase(String name);
}
