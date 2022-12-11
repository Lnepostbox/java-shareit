package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("UserGatewayController: getUsers implementation.");
        return userClient.getUsers();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("UserGatewayController: getUser implementation. User ID {}.", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @Validated(Create.class)
            @RequestBody UserRequestDto requestDto) {
        log.info("UserGatewayController: createUser implementation.");
        return userClient.createUser(requestDto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @Validated(Update.class)
            @RequestBody UserRequestDto requestDto) {
        log.info("UserGatewayController: updateUser implementation. User ID {}.", userId);
        return userClient.updateUser(userId, requestDto);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("UserGatewayController: deleteUser implementation. User ID {}.", userId);
        return userClient.deleteUser(userId);
    }
}
