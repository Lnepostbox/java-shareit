package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemDtoWithBooking {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Boolean available;

    private BookingItemDto lastBooking;

    private BookingItemDto nextBooking;

    private List<CommentDto> comments;

}
