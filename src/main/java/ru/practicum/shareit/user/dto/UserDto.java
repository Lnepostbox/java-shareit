package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.validator.Create;
import ru.practicum.shareit.user.validator.Update;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotNull(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
    @NotBlank(groups = {Create.class})
    private String name;
}
