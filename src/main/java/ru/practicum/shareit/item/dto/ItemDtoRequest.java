package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.validator.Create;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemDtoRequest {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;
}