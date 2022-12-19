package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long userId);

    UserDto save(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    void delete(Long userId);
}