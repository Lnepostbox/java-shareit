package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import java.util.List;

public interface BookingService {

    List<Booking> findAllBookings();

    Booking findBookingById(Long bookingId);

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBookingById(Long bookingId);
}
