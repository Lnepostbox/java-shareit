package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ItemRequestServiceImplMockTest {

    ItemRequestService itemRequestService;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestService = new ItemRequestServiceImpl(userRepository, itemRepository, itemRequestRepository);
    }

    @Test
    void findAllByUserIdTest() {
        List<ItemRequest> requests = List.of(
                new ItemRequest(1L, "testDescription", null, LocalDateTime.now()));

        User user = new User(1L, "testName", "test@mail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(Mockito.anyLong()))
                .thenReturn(requests);

        Mockito.when(itemRepository.findAllByRequestIn(requests))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> requestsDto = itemRequestService.findAllByUserId(1L);

        Assertions.assertNotNull(requestsDto);
        Assertions.assertEquals(1, requestsDto.size());
    }

    @Test
    void findAllByUserIdTestThrowsExceptionWithWrongUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findAllByUserId(1L)
        );
        Assertions.assertEquals("User ID 1 doesn't exist.", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        User user = new User(1L, "testName", "test@mail.com");
        ItemRequest request = new ItemRequest(1L, "testDescription", user, LocalDateTime.now());
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(request.getId()))
                .thenReturn(Optional.of(request));

        Mockito.when(itemRepository.findAllByRequestId(request.getId()))
                .thenReturn(Collections.emptyList());

        ItemRequestDto requestDto = itemRequestService.findById(user.getId(), request.getId());
        Assertions.assertNotNull(requestDto);
    }

    @Test
    void findByIdTestThrowsExceptionWithWrongUserId() {
        User user = new User(1L, "testName", "test@mail.com");
        ItemRequest request = new ItemRequest(1L, "testDescription", user, LocalDateTime.now());

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
               NotFoundException.class,
                () -> itemRequestService.findById(user.getId(), request.getId()));

        Assertions.assertEquals("User ID 1 doesn't exist.", exception.getMessage());
    }

    @Test
    void findByIdTestThrowsExceptionWithWrongRequestId() {
        User user = new User(1L, "testName", "test@mail.com");
        ItemRequest request = new ItemRequest(1L, "testDescription", user, LocalDateTime.now());

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(request.getId()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
             NotFoundException.class,
                () -> itemRequestService.findById(user.getId(), request.getId()));

        Assertions.assertEquals("ItemRequest ID 1 doesn't exist.", exception.getMessage());
    }

    @Test
    void saveTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(null, "testDescription", null, null);

        User user = new User(1L, "test", "test@gmail.com");

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.save(Mockito.any()))
                .thenReturn(itemRequest);

        ItemRequestDto request = itemRequestService.save(user.getId(), itemRequestDto);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(itemRequestDto.getDescription(), request.getDescription());
    }

    @Test
    void saveTestThrowsExceptionWithWrongUserId() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(null, "testDescription", null, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.save(1L, itemRequestDto)
        );
        Assertions.assertEquals("User ID 1 doesn't exist.", exception.getMessage());
    }
}
