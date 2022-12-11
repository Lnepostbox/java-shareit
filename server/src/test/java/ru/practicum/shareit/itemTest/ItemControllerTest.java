package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDtoRequest itemDtoRequest;

    private ItemDtoRequest itemDtoRequest1;

    private UserDto userDto;

    private UserDto userDto1;

    private ItemRequestDto itemRequestDto;

    private CommentDto comment;

    @BeforeEach
    void init() {
        itemDtoRequest = new ItemDtoRequest(
                null, "testName", "testDescription", true, null);

        itemDtoRequest1 = new ItemDtoRequest(
                null, "testName1", "testDescription1", true, null);

        userDto = new UserDto(null, "testName", "test@mail.com");

        userDto1 = new UserDto(null, "testName1", "test1@mail.com");

        itemRequestDto = new ItemRequestDto(null, "testDescription", null, null);

        comment = new CommentDto(null, "text", null, null);
    }

    @Test
    void findAllByTextTest() {
        userController.save(userDto);
        itemController.save(1L, itemDtoRequest);
        assertEquals(1, itemController.findAllByText("Desc", 0, 10).size());
    }

    @Test
    void findAllByTextTestWithEmptyText() {
        userController.save(userDto);
        itemController.save(1L, itemDtoRequest);
        assertEquals(new ArrayList<ItemDtoResponse>(), itemController.findAllByText("", 0, 10));
    }

    @Test
    void findAllByTextTestWithWrongFrom() {
        assertThrows(IllegalArgumentException.class, () -> itemController.findAllByText("t", -1, 10));
    }

    @Test
    void findAllWithWrongFromTest() {
        assertThrows(IllegalArgumentException.class,
                () -> itemController.findAllByOwnerId(1L, -1, 10));
    }

    @Test
    void saveTest() {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(1L, itemDtoRequest);
        assertEquals(item.getId(), itemController.findById(item.getId(), user.getId()).getId());
    }

    @Test
    void saveTestWithRequest() {
        UserDto user = userController.save(userDto);
        itemRequestController.save(user.getId(), itemRequestDto);
        itemDtoRequest.setRequestId(1L);
        userController.save(userDto1);
        ItemDtoResponse item = itemController.save(2L, itemDtoRequest);
        assertEquals(item.getId(), itemController.findById(2L, 1L).getId());
    }

    @Test
    void saveTestWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> itemController.save(1L, itemDtoRequest));
    }

    @Test
    void saveTestWithWrongItemRequest() {
        itemDtoRequest.setRequestId(10L);
        userController.save(userDto);
        assertThrows(NotFoundException.class, () -> itemController.save(1L, itemDtoRequest));
    }

    @Test
    void saveCommentTest() throws InterruptedException {
        UserDto user = userController.save(userDto);
        ItemDtoResponse item = itemController.save(user.getId(), itemDtoRequest);
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), new BookingDtoRequest(
                null,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                item.getId()));

        bookingController.updateStatus(1L, user.getId(), true);
        TimeUnit.SECONDS.sleep(2);
        itemController.saveComment(user1.getId(), item.getId(), comment);
        assertEquals(1, itemController.findById(1L, 1L).getComments().size());
    }

    @Test
    void saveCommentTestWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> itemController.saveComment(1L, 1L, comment));
    }

    @Test
    void saveCommentTestWithWrongItemId() {
        userController.save(userDto);
        assertThrows(NotFoundException.class, () -> itemController.saveComment(1L, 1L, comment));
        itemController.save(1L, itemDtoRequest);
        assertThrows(BookingException.class, () -> itemController.saveComment(1L, 1L, comment));
    }

    @Test
    void updateTest() {
        userController.save(userDto);
        itemController.save(1L, itemDtoRequest);
        itemController.update(1L, 1L, itemDtoRequest1);
        assertEquals(itemDtoRequest1.getDescription(), itemController.findById(1L, 1L).getDescription());
    }

    @Test
    void updateTestWithWrongItemId() {
        assertThrows(NotFoundException.class, () -> itemController.update(1L, 1L, itemDtoRequest));
    }

    @Test
    void updateTestWithWrongUserId() {
        userController.save(userDto);
        itemController.save(1L, itemDtoRequest);
        assertThrows(NotFoundException.class, () -> itemController.update(10L, 1L, itemDtoRequest1));
    }

    @Test
    void deleteTest() {
        userController.save(userDto);
        itemController.save(1L, itemDtoRequest);
        assertEquals(1, itemController.findAllByOwnerId(1L, 0, 10).size());
        itemController.delete(1L, 1L);
        assertEquals(0, itemController.findAllByOwnerId(1L, 0, 10).size());
    }
}
