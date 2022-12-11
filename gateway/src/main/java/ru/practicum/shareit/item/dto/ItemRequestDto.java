package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Create;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/item/dto/ItemRequestDto.java
public class ItemRequestDto {
=======
@EqualsAndHashCode
public class ItemDtoRequest {

    private Long id;
>>>>>>> main:src/main/java/ru/practicum/shareit/item/dto/ItemDtoRequest.java

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long requestId;
<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/item/dto/ItemRequestDto.java
}
=======
}
>>>>>>> main:src/main/java/ru/practicum/shareit/item/dto/ItemDtoRequest.java
