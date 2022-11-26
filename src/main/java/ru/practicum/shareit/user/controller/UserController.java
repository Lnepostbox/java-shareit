package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("UserController: finAll implementation.");
        return userService.findAll();
    }

    @GetMapping(value = "/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("UserController: findById implementation. User ID {}.", userId);
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto save(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("UserController: save implementation.");
        return userService.save(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto update(
            @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("UserController: update implementation. User ID {}.", userId);
        return userService.update(userId, userDto);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("UserController: delete implementation. User ID {}.", userId);
        userService.delete(userId);
    }
}