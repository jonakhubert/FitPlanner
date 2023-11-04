package com.fitplanner.user.service;

import com.fitplanner.user.exception.model.UserNotFoundException;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.api.UserDetailsRequest;
import com.fitplanner.user.model.food.MealPlan;
import com.fitplanner.user.model.training.WorkoutPlan;
import com.fitplanner.user.model.user.NutritionInfo;
import com.fitplanner.user.model.user.User;
import com.fitplanner.user.model.user.UserDTO;
import com.fitplanner.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTOMapper userDTOMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService underTest;

    @Test
    public void changePassword_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var newPassword = "password";
        var user = new User();
        user.setEmail(email);
        var expected = new ConfirmationResponse("Password has been changed.");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        var result = underTest.changePassword(email, newPassword);

        // then
        assertEquals(result, expected);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(passwordEncoder, times(1)).encode(eq(newPassword));
        verify(userRepository, times(1)).save(eq(user));
    }

    @Test
    public void changePassword_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";
        var newPassword = "password";

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.changePassword(email, newPassword));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(passwordEncoder, times(0)).encode(eq(newPassword));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void deleteAccount_ExistingEmail_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var user = new User();
        user.setEmail(email);
        var expected = new ConfirmationResponse("Account has been deleted.");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        var result = underTest.deleteAccount(email);

        // then
        assertEquals(result, expected);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).delete(eq(user));
    }

    @Test
    public void deleteAccount_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.deleteAccount(email));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Test
    public void updateUserDetails_ValidEmailAndChangedUserDetails_ConfirmationResponse() {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var nutritionInfo = new NutritionInfo(3000, 200, 100, 250, 191, 88, 1, 2, "2023-10-10", "");
        var user = new User(firstName, lastName, email, nutritionInfo, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var request = new UserDetailsRequest(firstName, lastName, 191, 95, 1, 2);
        var expected = new ConfirmationResponse("User updated successfully.");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        var result = underTest.updateUserDetails(email, request);

        // then
        assertEquals(result, expected);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(eq(user));
    }

    @Test
    public void updateUserDetails_ValidEmailAndTheSameUserDetails_ConfirmationResponse() {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var nutritionInfo = new NutritionInfo(3402, 188, 94, 451, 191, 94, 3, 3, "2023-10-28", "");
        var user = new User(firstName, lastName, email, nutritionInfo, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var request = new UserDetailsRequest(firstName, lastName, 191, 94, 3, 3);
        var expected = new ConfirmationResponse("User data hasn't changed.");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        var result = underTest.updateUserDetails(email, request);

        // then
        assertEquals(result, expected);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(0)).save(eq(user));
    }

    @Test
    public void updateUserDetails_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.updateUserDetails(email, any(UserDetailsRequest.class)));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void findUserByEmail_ExistingEmail_UserDTO() {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var nutritionInfo = new NutritionInfo(3402, 188, 94, 451, 191, 94, 3, 3, "2023-10-28", "");
        var user = new User(firstName, lastName, email, nutritionInfo, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var userDTO = new UserDTO(firstName, lastName, email, nutritionInfo, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userDTOMapper.apply(eq(user))).thenReturn(userDTO);

        // when
        var result = underTest.findUserByEmail(email);

        // then
        assertEquals(result, userDTO);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userDTOMapper, times(1)).apply(eq(user));
    }

    @Test
    public void findUserByEmail_NonExistingEmail_UserDTO() {
        // given
        var email = "any@gmail.com";
        var user = new User();

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.deleteAccount(email));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userDTOMapper, times(0)).apply(eq(user));
    }

    @Test
    public void saveUserMealPlanList_ValidParameters_SavedUser() {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var mealPlanList = new ArrayList<MealPlan>();
        var nutritionInfo = new NutritionInfo(3402, 188, 94, 451, 191, 94, 3, 3, "2023-10-28", "");
        var user = new User(firstName, lastName, email, nutritionInfo, new ArrayList<>(), mealPlanList, new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
       underTest.saveUserMealPlanList(email, mealPlanList);

        // then
        assertEquals(user.getMealPlanList(), mealPlanList);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(eq(user));
    }

    @Test
    public void saveUserMealPlanList_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";
        var user = new User();

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.saveUserMealPlanList(email, new ArrayList<>()));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(0)).save(eq(user));
    }

    @Test
    public void saveUserWorkoutPlanList_ValidParameters_SavedUser() {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var nutritionInfo = new NutritionInfo(3402, 188, 94, 451, 191, 94, 3, 3, "2023-10-28", "");
        var user = new User(firstName, lastName, email, nutritionInfo, new ArrayList<>(), new ArrayList<>(), workoutPlanList);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        underTest.saveUserWorkoutPlanList(email, workoutPlanList);

        // then
        assertEquals(user.getWorkoutPlanList(), workoutPlanList);
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(eq(user));
    }

    @Test
    public void saveUserWorkoutPlanList_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";
        var user = new User();

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.saveUserWorkoutPlanList(email, new ArrayList<>()));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(0)).save(eq(user));
    }
}
