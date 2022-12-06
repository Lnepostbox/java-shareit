package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;

}
