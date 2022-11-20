package ru.practicum.shareit.item.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemDtoWithBooking {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    private Boolean available;

    private Booking lastBooking;

    private Booking nextBooking;

    private List<Comment> comments;

    @Data
    @Builder
    public static class Booking {
        private final Long id;

        private final Long bookerId;
    }

    @Data
    @Builder
    public static class Comment {
        private final Long id;

        private final String text;

        private final String authorName;

        private final LocalDateTime created;
    }
}
