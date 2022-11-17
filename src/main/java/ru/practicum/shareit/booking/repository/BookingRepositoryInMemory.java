package ru.practicum.shareit.booking.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRepositoryInMemory implements BookingRepository {
    private final HashMap<Long, Booking> bookings = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<Booking> findAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Optional<Booking> findBookingById(Long bookingId) {
        return Optional.of(bookings.get(bookingId));
    }

    @Override
    public Booking createBooking(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(nextId++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        bookings.replace(booking.getId(), booking);
        return booking;
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        bookings.remove(bookingId);
    }

    @Override
    public boolean bookingExists(Long bookingId) {
        return bookings.containsKey(bookingId);
    }
}

