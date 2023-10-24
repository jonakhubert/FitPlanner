package com.fitplanner.user.service;

import com.fitplanner.user.model.user.User;
import com.fitplanner.user.model.user.UserDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getNutritionInfo(),
            user.getHistoricalNutritionInfos(),
            user.getMealPlans()
        );
    }
}
