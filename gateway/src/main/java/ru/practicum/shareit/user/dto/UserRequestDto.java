package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/user/dto/UserRequestDto.java
public class UserRequestDto {
=======
@EqualsAndHashCode
public class UserDto {
>>>>>>> main:src/main/java/ru/practicum/shareit/user/dto/UserDto.java

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/user/dto/UserRequestDto.java
=======

>>>>>>> main:src/main/java/ru/practicum/shareit/user/dto/UserDto.java
}
