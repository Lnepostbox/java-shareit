package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.validator.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookingsByStatus(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateText,
			@RequestParam(value = "from", defaultValue = "0")
			@PositiveOrZero Integer from,
			@RequestParam(value = "size", defaultValue = "10")
			@Positive Integer size) {
		Status status = checkStatus(stateText);
		log.info("BookingGatewayController: getBookingsByStatus implementation. User ID {}, stateText {}.",
				userId, stateText);
		return bookingClient.getBookingsByStatus(userId, status, from, size);
	}

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> getBookingsByOwnerAndStatus(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateText,
			@RequestParam(value = "from", defaultValue = "0")
			@PositiveOrZero Integer from,
			@RequestParam(value = "size", defaultValue = "10")
			@Positive Integer size) {
		Status status = checkStatus(stateText);
		log.info("BookingGatewayController: getBookingsByOwnerAndStatus implementation. User ID {}, stateText {}.",
				userId, stateText);
		return bookingClient.getBookingsByOwnerAndStatus(userId, status, from, size);
	}

	@GetMapping(value = "/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long bookingId) {
		log.info("BookingGatewayController: getBooking implementation. User ID {}, booking ID {}.", userId, bookingId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@Validated(Create.class)
			@RequestBody BookItemRequestDto bookingRequestDto) {
		log.info("BookingGatewayController: createBooking implementation. User ID {}.", userId);
		return bookingClient.createBooking(userId, bookingRequestDto);
	}


	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> updateBookingStatus(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long bookingId,
			@RequestParam Boolean approved) {
		log.info("BookingGatewayController: updateBookingStatus implementation. User ID {}, booking ID {}.",
				userId, bookingId);
		return bookingClient.updateBookingStatus(userId, bookingId, approved);
	}

	@DeleteMapping(value = "/{bookingId}")
	public void deleteBooking(@PathVariable Long bookingId) {
		log.info("BookingGatewayController: deleteBooking implementation. Booking ID {}.", bookingId);
		bookingClient.deleteBooking(bookingId);
	}

	private Status checkStatus(String stateText) {
		if (Status.from(stateText).isEmpty()) {
			throw new IllegalArgumentException("Unknown state: " + stateText);
		}
		return Status.valueOf(stateText);
	}
}
