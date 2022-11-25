package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import java.util.List;

public interface BookingService {

    List<BookingDtoResponse> findAllByState(Long userId, String stateText);

    List<BookingDtoResponse> findAllByOwnerIdAndState(Long userId, String stateText);

    BookingDtoResponse findById(Long userId, Long bookingId);

    BookingDtoResponse save(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDtoResponse updateState(Long userId, Long bookingId, Boolean approved);

    void delete(Long bookingId);
}