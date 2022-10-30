package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User creator;
    private LocalDateTime created;
}
