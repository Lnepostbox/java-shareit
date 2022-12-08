package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDtoRequest itemDtoRequest;

    private UserDto userDto;

    private UserDto userDto1;

    private BookingDtoRequest bookingDtoRequest;

    @BeforeEach
    void init() {
        itemDtoRequest = new ItemDtoRequest(
                null,
                "testName",
                "tasteDescription",
                true,
                null);

        userDto = new UserDto(null, "testName", "test@mail.com");
        userDto1 = new UserDto(null, "testName1", "test1@mail.com");

        bookingDtoRequest = new BookingDtoRequest(
                null,
                LocalDateTime.of(2023, 10, 24, 12, 30),
                LocalDateTime.of(2023, 11, 10, 13, 0),
                null);
    }

    @Test
    void saveTest() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingDtoResponse booking = bookingController.save(user1.getId(), bookingDtoRequest);
        assertEquals(1L, bookingController.findById(user1.getId(), booking.getId()).getId());
    }

    @Test
    void saveTestWithWrongUser() {
        assertThrows(NotFoundException.class,
                () -> bookingController.save(1L, bookingDtoRequest));
    }

    @Test
    void saveTestWithWrongItem() {
        bookingDtoRequest.setItemId(30L);
        UserDto user1 = userController.save(userDto1);
        assertThrows(NotFoundException.class,
                () -> bookingController.save(user1.getId(), bookingDtoRequest));
    }

    @Test
    void saveTestWithOwner() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        assertThrows(NotFoundException.class,
                () -> bookingController.save(user.getId(), bookingDtoRequest));
    }

    @Test
    void saveTestWithUnavailableItem() {
        UserDto user = userController.save(userDto);
        itemDtoRequest.setAvailable(false);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        assertThrows(ValidationException.class,
                () -> bookingController.save(user1.getId(), bookingDtoRequest));
    }

    @Test
    void saveTestWithWrongEndDate() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingDtoRequest.setEnd(LocalDateTime.of(2022, 9, 24, 12, 30));
        assertThrows(BookingException.class,
                () -> bookingController.save(user1.getId(), bookingDtoRequest));
    }

    @Test
    void updateStateTest() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingDtoResponse booking = bookingController.save(user1.getId(), bookingDtoRequest);
        assertEquals(Status.WAITING, bookingController.findById(user1.getId(), booking.getId()).getStatus());
        bookingController.updateState(user.getId(), booking.getId(), true);
        assertEquals(Status.APPROVED, bookingController.findById(user1.getId(), booking.getId()).getStatus());
    }

    @Test
    void updateStateTestWithWrongBooking() {
        assertThrows(NotFoundException.class,
                () -> bookingController.updateState(1L, 1L, true));
    }

    @Test
    void updateStateTestWithWrongUser() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), bookingDtoRequest);
        assertThrows(NotFoundException.class,
                () -> bookingController.updateState(1L, 2L, true));
    }

    @Test
    void updateStateToBookingWithWrongStatus() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), bookingDtoRequest);
        bookingController.updateState(1L, 1L, true);
        assertThrows(BookingException.class,
                () -> bookingController.updateState(1L, 1L, true));
    }

    @Test
    void findAllByUserTest() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingDtoResponse booking = bookingController.save(user1.getId(), bookingDtoRequest);
        assertEquals(1, bookingController.
                findAllByState(user1.getId(), "WAITING", 0, 10).size());
        assertEquals(1, bookingController.
                findAllByState(user1.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByState(user1.getId(), "PAST", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByState(user1.getId(), "CURRENT", 0, 10).size());
        assertEquals(1, bookingController.
                findAllByState(user1.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByState(user1.getId(), "REJECTED", 0, 10).size());
        bookingController.updateState(booking.getId(), user.getId(), true);
        assertEquals(0, bookingController.
                findAllByState(user.getId(), "CURRENT", 0, 10).size());
        assertEquals(1, bookingController.
                findAllByOwnerIdAndState(user.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByOwnerIdAndState(user.getId(), "WAITING", 0, 10).size());
        assertEquals(1, bookingController.
                findAllByOwnerIdAndState(user.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByOwnerIdAndState(user.getId(), "REJECTED", 0, 10).size());
        assertEquals(0, bookingController.
                findAllByOwnerIdAndState(user.getId(), "PAST", 0, 10).size());
    }

    @Test
    void findAllByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingController
                .findAllByState(1L, "ALL", 0, 10));
        assertThrows(NotFoundException.class, () -> bookingController
                .findAllByOwnerIdAndState(1L, "ALL", 0, 10));
    }

    @Test
    void findByIdTestWithWrongId() {
        assertThrows(NotFoundException.class,
                () -> bookingController.findById(1L, 1L));
    }

    @Test
    void findByWrongUserTest() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        bookingDtoRequest.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), bookingDtoRequest);
        assertThrows(NotFoundException.class, () -> bookingController.findById(1L, 10L));
    }
}
