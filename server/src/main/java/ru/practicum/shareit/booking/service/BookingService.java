package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingService {

    List<BookingDtoResponse> findAllByStatus(Long userId, Status status, int from, int size);

    List<BookingDtoResponse> findAllByOwnerIdAndStatus(Long userId, Status status, int from, int size);

    BookingDtoResponse findById(Long userId, Long bookingId);

    BookingDtoResponse save(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDtoResponse updateStatus(Long userId, Long bookingId, Boolean approved);

    void delete(Long bookingId);
}