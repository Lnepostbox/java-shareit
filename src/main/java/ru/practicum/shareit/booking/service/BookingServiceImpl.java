package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoFromBooking;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> findAll(Long userId, String stateParam) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь ID %s не существует.", userId)));
        switch (Status.valueOf(stateParam)) {
            case CURRENT:
                log.info("Все бронирования пользователя ID {} со статусом {}", userId, stateParam);
                return bookingRepository
                        .findCurrentBookingsByBookerIdOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case PAST:
                log.info("Все бронирования пользователя ID {} со статусом {}", userId, stateParam);
                return bookingRepository
                        .findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case FUTURE:
                log.info("Все бронирования пользователя ID {} со статусом {}", userId, stateParam);
                return bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("Все бронирования пользователя ID {} со статусом {}", userId, stateParam);
                return bookingRepository
                        .findBookingsByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("Все бронирования пользователя ID {} со статусом {}", userId, stateParam);
                return bookingRepository
                        .findBookingsByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            default:
                log.info("Все бронирования пользователя ID {} ", userId);
                return bookingRepository
                        .findByBookerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public BookingDto findById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Бронирование ID %s не существует.", bookingId)));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            log.error("Пользователь ID {} не осущетвлял бронирование", userId);
            throw new NotFoundException(String.format("Пользователь ID %s не осуществлял бронирование.", userId));
        }
        log.info("Бронироавние пользователя ID {}: {}", userId, booking);
        return toBookingDtoFromBooking(booking);
    }

    @Override
    public List<BookingDto> findAllByUserItems(Long userId, String stateParam) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь ID %s не существует.", userId)));
        List<BookingDto> bookingsUserList = bookingRepository.searchBookingByItemOwnerId(userId)
                .stream()
                .map(BookingMapper::toBookingDtoFromBooking)
                .collect(Collectors.toList());

        if (bookingsUserList.isEmpty()) {
            throw new NotFoundException("У пользователя нет вещей");
        }

        switch (Status.valueOf(stateParam)) {
            case CURRENT:
                log.info("Текущие бронирования владельца ID {} ", userId);
                return bookingRepository
                        .findCurrentBookingsByItemOwnerIdOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case PAST:
                log.info("Прошедшие бронирования владельца ID {} ", userId);
                return bookingRepository
                        .findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case FUTURE:
                log.info("Будущие бронирования владельца ID {} ", userId);
                return bookingRepository
                        .searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case WAITING:
                log.info("Бронирования в ожидании владельца ID {} ", userId);
                return bookingRepository
                        .findWaitingBookingsByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            case REJECTED:
                log.info("Отклонённые бронирования владельца ID {} ", userId);
                return bookingRepository
                        .findRejectedBookingsByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDtoFromBooking)
                        .collect(Collectors.toList());
            default:
                log.info("Все бронирования владельца ID {} ", userId);
                bookingsUserList.sort(Comparator.comparing(BookingDto::getStart).reversed());
                return bookingsUserList;
        }
    }

    @Override
    public BookingDto save(Long userId, BookingDtoRequest bookingDtoRequest) {
        Booking booking = BookingMapper.toBookingFromBookingDtoRequest(bookingDtoRequest);
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь ID %s не существует.", userId))));
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещь ID %s не существует.", bookingDtoRequest.getItemId())));

        if (item.getOwner().getId().equals(userId)) {
            log.error("Владелец вещи не может забронировать свою вещь");
            throw new NotFoundException("Владелец вещи не может забронировать свою вещь");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            log.error("Некорректное время окончания бронирования.");
            throw new BookingException("Некорректное время окончания бронирования.");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            log.error("Некорректное время начала бронирования.");
            throw new BookingException("Некорректное время начала бронирования.");
        }
        if (!item.getAvailable()) {
            log.error("Вещь ID {} не доступна для бронирования.", item.getId());
            throw new ValidationException(
                    String.format("Вещь ID %s не доступна для бронирования.", item.getId()));
        }
        booking.setItem(item);
        Booking bookingSave = bookingRepository.save(booking);
        log.info("Создано бронирование ID {}:{}", bookingSave.getId(), bookingSave);
        return toBookingDtoFromBooking(bookingSave);
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(
                String.format("Бронирование ID %s не существует.", bookingId)));

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            log.error("Подтвердить бронирование может только владелец вещи");
            throw new NotFoundException("Подтвердить бронирование может только владелец вещи");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            log.error("Бронирование уже было подтверждено");
            throw new BookingException("Бронирование уже было подтверждено");
        }
        if (approved == null) {
            log.error("Необходимо указать статус возможности аренды (approved).");
            throw new BookingException("Необходимо указать статус возможности аренды (approved).");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking bookingSave = bookingRepository.save(booking);
        log.info("Бронирование ID {} обновлено: {}", bookingSave.getId(), bookingSave);
        return toBookingDtoFromBooking(bookingSave);
        }

    @Override
    public void deleteById(Long bookingId) {
        bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(
                String.format("Бронирование ID %s не существует.", bookingId)));
        log.info("Бронирование ID {} удалено", bookingId);
        bookingRepository.deleteById(bookingId);
    }
}