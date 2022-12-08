package ru.practicum.shareit.userTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.io.IOException;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jackson;

    @Test
    void userDtoTest() throws IOException {
        User user = new User(1L, "testName", "test@mail.com");
        UserDto userDto = UserMapper.toUserDto(user);

        JsonContent<UserDto> json = jackson.write(userDto);

        Assertions.assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        Assertions.assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }
}
