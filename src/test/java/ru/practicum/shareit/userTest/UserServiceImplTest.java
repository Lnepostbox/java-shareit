package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import java.util.Optional;

class UserServiceImplTest {

    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldReturnUserForFindByIdWithRightParameters() {
        User user = new User(1L, "testName", "test@mail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto foundUser = userService.findById(user.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionForFindByIdWithWrongUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.findById(1L));

        Assertions.assertEquals("User ID 1 doesn't exist.", exception.getMessage());
    }

    @Test
    void shouldSaveUserForSaveWithRightParameters() {
        User user = new User(1L, "testName", "test@mail.com");
        UserDto userDto = new UserDto(null, "testName", "test@mail.com");

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto foundUser = userService.save(userDto);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldUpdateUserForUpdateWithRightParameters() {
        User user = new User(1L, "testName", "test@mail.com");
        User updateUser = new User(1L, "testNameUpdate", "testUpdate@mail.com");
        UserDto userDto = new UserDto(null, "testNameUpdate", "testUpdate@mail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(updateUser);

        UserDto foundUser = userService.update(user.getId(), userDto);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(updateUser.getId(), foundUser.getId());
        Assertions.assertEquals(updateUser.getName(), foundUser.getName());
        Assertions.assertEquals(updateUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionForUpdateWithWrongUserId() {
        UserDto userDto = new UserDto(null, "testNameUpdate", "testUpdate@mail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.update(1L, userDto));

        Assertions.assertEquals("User ID 1 is already exist.", exception.getMessage());
    }
}
