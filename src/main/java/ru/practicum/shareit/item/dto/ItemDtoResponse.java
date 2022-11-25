package ru.practicum.shareit.item.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class ItemDtoResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private ItemOwner owner;

    private ItemBooking lastBooking;

    private ItemBooking nextBooking;

    private List<CommentDto> comments;

    @Data
    public static class ItemOwner {
        private final long id;
        private final String name;
    }

    @Data
    public static class ItemBooking {
        private final long id;
        private final long bookerId;
    }
}
