package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validator.Create;
import ru.practicum.shareit.user.validator.Update;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() { return userService.findAll(); }

    @GetMapping(value = "/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto save(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto update(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @PathVariable Long userId) { return userService.update(userDto, userId); }

    @DeleteMapping(value = "/{userId}")
    public void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}