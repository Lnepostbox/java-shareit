package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

@Mapper
public class BookingMapper {

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(new BookingDtoResponse.Item(
                booking.getItem().getId(),
                booking.getItem().getName()));
        bookingDtoResponse.setBooker(new BookingDtoResponse.Booker(
                booking.getBooker().getId(),
                booking.getBooker().getName()));
        bookingDtoResponse.setStatus(booking.getStatus());
        return bookingDtoResponse;
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
