package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findAllByStatus(Long userId, Status status, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));

        Pageable pageable = createPageRequest(from, size);

        switch (status) {
            case CURRENT:
                log.info("BookingService: findAllByState implementation. User ID {}, status CURRENT.", userId);
                return bookingRepository
                        .findByBookerIdAndCurrentOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case PAST:
                log.info("BookingService: findAllByState implementation. User ID {}, status PAST.", userId);
                return bookingRepository
                        .findByBookerIdAndEndIsBeforeOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case FUTURE:
                log.info("BookingService: findAllByState implementation. User ID {}, status FUTURE.", userId);
                return bookingRepository
                        .findByBookerIdAndStartIsAfterOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("BookingService: findAllByState implementation. User ID {}, status WAITING.", userId);
                return bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.WAITING,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("BookingService: findAllByState implementation. User ID {}, status REJECTED.", userId);
                return bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.REJECTED,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            default:
                log.info("BookingService: findAllByState implementation. User ID {}, stateText by default.", userId);
                return bookingRepository
                        .findByBookerIdOrderByStartDesc(
                                userId,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findAllByOwnerIdAndStatus(Long userId, Status status, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId)));

        Pageable pageable = createPageRequest(from, size);

        List<BookingDtoResponse> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(
                        userId,
                        pageable)
                .stream()
                .map(BookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new NotFoundException(String.format("User ID %s doesn't have items.", userId));
        }

        switch (status) {
            case CURRENT:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status CURRENT.",
                        userId);
                return bookingRepository
                        .findByItemOwnerIdAndCurrentOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case PAST:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status PAST.",
                        userId);
                return bookingRepository
                        .findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case FUTURE:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status FUTURE.",
                        userId);
                return bookingRepository
                        .findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                                userId,
                                LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status WAITING.",
                        userId);
                return bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.WAITING,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status REJECTED.",
                        userId);
                return bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(
                                userId,
                                Status.REJECTED,
                                pageable)
                        .stream()
                        .map(BookingMapper::toBookingDtoResponse)
                        .collect(Collectors.toList());
            default:
                log.info("BookingService: findAllByOwnerIdAndStatus implementation. User ID {}, status text by default.",
                        userId);
                return bookings;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDtoResponse findById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking ID %s doesn't exist.", bookingId)));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("User ID %s didn't create booking ID %s.", userId, bookingId));
        }
        log.info("BookingService: findById implementation. User ID {}, booking ID {}.", userId, bookingId);
        return toBookingDtoResponse(booking);
    }

    @Override
    @Transactional
    public BookingDtoResponse save(Long userId, BookingDtoRequest bookingDtoRequest) {
        Booking booking = BookingMapper.toBooking(bookingDtoRequest);
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User ID %s doesn't exist.", userId))));
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Item ID %s doesn't exist.",
                        bookingDtoRequest.getItemId())));

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner can't book his item.");
        }
        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item ID %s isn't available.", item.getId()));
        }
        booking.setItem(item);
        Booking bookingSave = bookingRepository.save(booking);
        log.info("BookingServer: save implementation. User ID {}, booking ID {}.", userId, bookingSave.getId());
        return toBookingDtoResponse(bookingSave);
    }

    @Override
    @Transactional
    public BookingDtoResponse updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking ID %s doesn't exist.", bookingId)));

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Only owner can approve bookings.");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingException("Booking is already approved.");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("BookingService: updateState implementation. User ID {}, booking ID {}.", userId, bookingId);
        return toBookingDtoResponse(booking);
        }

    @Override
    @Transactional
    public void delete(Long bookingId) {
        bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking ID %s doesn't exist.", bookingId)));
        log.info("BookingService: delete implementation. Booking ID {}.", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    private PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from / size, size);
    }
}