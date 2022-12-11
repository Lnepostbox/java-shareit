package ru.practicum.shareit.item.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
