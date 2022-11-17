package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.model.Status;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        Status state = Status.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return bookingService.findAll(userId, stateParam);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> findAllByUserItems(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        Status state = Status.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return bookingService.findAllByUserItems(userId, stateParam);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    public BookingDto save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        return bookingService.save(userId, bookingDtoRequest);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteById(@PathVariable Long bookingId) {
        bookingService.deleteById(bookingId);
    }
}