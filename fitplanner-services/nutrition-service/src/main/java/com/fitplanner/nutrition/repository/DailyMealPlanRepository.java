package com.fitplanner.nutrition.repository;

import com.fitplanner.nutrition.model.food.DailyMealPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyMealPlanRepository extends MongoRepository<DailyMealPlan, String> {

}
