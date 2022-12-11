package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.model.Status;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDtoResponse> findAllByStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("BookingServerController: findAllByState implementation. User ID {}, stateText {}.",
                userId, stateText);
        return bookingService.findAllByStatus(userId, Status.valueOf(stateText), from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoResponse> findAllByOwnerIdAndStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("BookingServerController: findAllByOwnerIdAndStatus implementation. User ID {}, stateText {}.",
                userId, stateText);
        return bookingService.findAllByOwnerIdAndStatus(userId, Status.valueOf(stateText), from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoResponse findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("BookingServerController: findById implementation. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    public BookingDtoResponse save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("BookingServerController: save implementation. User ID {}.", userId);
        return bookingService.save(userId, bookingDtoRequest);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoResponse updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("BookingController: updateState implementation. User ID {}, booking ID {}.", userId, bookingId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @DeleteMapping(value = "/{bookingId}")
    public void delete(@PathVariable Long bookingId) {
        log.info("BookingController: delete implementation. Booking ID {}.", bookingId);
        bookingService.delete(bookingId);
    }
}