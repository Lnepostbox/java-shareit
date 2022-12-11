package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("UserServerController: finAll implementation.");
        return userService.findAll();
    }

    @GetMapping(value = "/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("UserServerController: findById implementation. User ID {}.", userId);
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto save(@RequestBody UserDto userDto) {
        log.info("UserServerController: save implementation.");
        return userService.save(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto update(
            @PathVariable Long userId,
            @RequestBody UserDto userDto) {
        log.info("UserServerController: update implementation. User ID {}.", userId);
        return userService.update(userId, userDto);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("UserServerController: delete implementation. User ID {}.", userId);
        userService.delete(userId);
    }
}