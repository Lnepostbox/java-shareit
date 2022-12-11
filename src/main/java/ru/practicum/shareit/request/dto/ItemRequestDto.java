package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.validator.Create;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String description;

    private LocalDateTime created;

    private List<ItemDtoResponse> items;
}

