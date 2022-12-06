package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.validator.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDtoResponse> findAllByState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive int size) {
        if (Status.from(stateText) == null) {
            throw new IllegalArgumentException("Unknown state: " + stateText);
        }
        log.info("BookingController: findAllByState implementation. User ID {}, stateText {}.", userId, stateText);
        return bookingService.findAllByState(userId, stateText, from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoResponse> findAllByOwnerIdAndState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive int size) {
        if (Status.from(stateText) == null) {
            throw new IllegalArgumentException("Unknown state: " + stateText);
        }
        log.info("BookingController: findAllByOwnerIdAndState implementation. User ID {}, stateText {}.",
                userId, stateText);
        return bookingService.findAllByOwnerIdAndState(userId, stateText, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoResponse findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("BookingController: findById implementation. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    public BookingDtoResponse save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class) @RequestBody BookingDtoRequest bookingDtoRequest) {
        if (!bookingDtoRequest.getEnd().isAfter(bookingDtoRequest.getStart())) {
            throw new BookingException("Incorrect booking time insertion.");
        }
        log.info("BookingController: save implementation. User ID {}.", userId);
        return bookingService.save(userId, bookingDtoRequest);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoResponse updateState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        if (approved == null) {
            throw new BookingException("Incorrect (approved) state insertion.");
        }
        log.info("BookingController: updateState implementation. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.updateState(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId) {
        log.info("BookingController: delete implementation. Booking ID {}.", bookingId);
        bookingService.delete(bookingId);
    }
}