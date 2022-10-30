package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(Long id);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUserById(Long id);
}
