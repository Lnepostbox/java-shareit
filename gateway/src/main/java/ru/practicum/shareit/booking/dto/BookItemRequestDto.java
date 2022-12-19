package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.StartBeforeEndDateValid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEndDateValid(groups = {Create.class})
public class BookItemRequestDto {

	private long itemId;

	@FutureOrPresent(groups = {Create.class})
	private LocalDateTime start;

	@Future(groups = {Create.class})
	private LocalDateTime end;
}
