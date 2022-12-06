package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class ItemServiceImplTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
    }

    @Test
    void shouldSaveItemWithRightParameters() {
        User user = new User(1L, "testName", "test@mail.com");
        Item item = new Item(1L, "testName", "testDescription", true, user, null);
        ItemDtoRequest itemDto =
                new ItemDtoRequest(null, "testName", "testDescription", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDtoResponse foundItem = itemService.save(user.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldThrowExceptionForSaveItemWithWrongUserId() {
        ItemDtoRequest itemDto =
                new ItemDtoRequest(null, "testName", "testDescription", true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> itemService.save(1L, itemDto));

        Assertions.assertEquals("User ID 1 doesn't exist.", exception.getMessage());
    }

    @Test
    void shouldSaveItemWithRequest() {
        User user1 = new User(1L, "testName1", "test1@mail.com");
        User user2 = new User(2L, "testName2", "test2@mail.com");
        ItemRequest itemRequest =
                new ItemRequest(1L, "testDescription", user2, LocalDateTime.now());
        Item item =
                new Item(1L, "testName", "testDescription", true, user1, itemRequest);
        ItemDtoRequest itemDto =
                new ItemDtoRequest(null, "testName", "testDescription", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDtoResponse foundItem = itemService.save(user1.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldThrowExceptionForSaveItemWithWrongRequestId() {
        User user = new User(1L, "testName", "test@mail.com");
        ItemDtoRequest itemDto =
                new ItemDtoRequest(null, "testName", "testDescription", true, 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> itemService.save(1L, itemDto));

        Assertions.assertEquals("ItemRequest not found.", exception.getMessage());
    }

    @Test
    void shouldUpdateItemWithRightParameters() {
        User user = new User(1L, "testName", "test@mail.com");
        Item item = new Item(1L, "testName", "testDescription", true, user, null);
        Item itemUpdate =
                new Item(1L, "testNameUpdate", "testDescriptionUpdate",
                        true, user, null);
        ItemDtoRequest itemDto =
                new ItemDtoRequest(null, "testNameUpdate", "testDescriptionUpdate",
                        true, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemUpdate);

        ItemDtoResponse foundItem = itemService.update(user.getId(), item.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void shouldReturnEmptyListForFindAllByText() {
        User user = new User(1L, "testName", "test@mail.com");
        String text = "";

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        List<ItemDtoResponse> results = itemService.findAllByText(text, 0, 10);

        Assertions.assertEquals(0, results.size());
    }
}
