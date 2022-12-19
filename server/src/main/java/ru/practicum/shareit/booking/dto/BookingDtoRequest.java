package ru.practicum.shareit.booking.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingDtoRequest {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
