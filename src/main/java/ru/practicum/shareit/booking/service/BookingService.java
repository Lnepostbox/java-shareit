package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

public interface BookingService {

    List<BookingDto> findAll(Long userId, String stateParam);

    BookingDto findById(Long userId, Long bookingId);

    List<BookingDto> findAllByUserItems(Long userId, String stateParam);

    BookingDto save(Long userId, BookingDtoRequest bookingDto);

    BookingDto update(Long userId, Long bookingId, Boolean approved);

    void deleteById(Long bookingId);
}