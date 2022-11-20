package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private Booker booker;

    private Status status;

    @Data
    @Builder
    public static class Booker {
        private final long id;
        private final String name;
    }

    @Data
    @Builder
    public static class Item {
        private final long id;
        private final String name;
    }
}