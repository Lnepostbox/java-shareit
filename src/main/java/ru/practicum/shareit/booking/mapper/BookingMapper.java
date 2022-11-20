package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

@Mapper
public class BookingMapper {

    public static BookingDto toBookingDtoFromBooking(Booking booking) {

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                BookingDto.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build(),
                BookingDto.Booker.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .build(),
                booking.getStatus());
    }

    public static Booking toBookingFromBookingDtoRequest(BookingDtoRequest bookingDtoRequest) {
        return new Booking(
                bookingDtoRequest.getId(),
                bookingDtoRequest.getStart(),
                bookingDtoRequest.getEnd(),
                null,
                null,
                Status.WAITING);
    }

    public static BookingItemDto toBookingItemDtoFromBooking(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getBooker().getId());
    }
}
