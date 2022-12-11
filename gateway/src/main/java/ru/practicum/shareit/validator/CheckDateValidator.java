package ru.practicum.shareit.validator;

<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
=======
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
>>>>>>> main:src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java
public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookItemRequestDto> {
=======
public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingDtoRequest> {
>>>>>>> main:src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java

    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java
    public boolean isValid(BookItemRequestDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
=======
    public boolean isValid(BookingDtoRequest bookingDto, ConstraintValidatorContext constraintValidatorContext) {
>>>>>>> main:src/main/java/ru/practicum/shareit/validator/CheckDateValidator.java
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
