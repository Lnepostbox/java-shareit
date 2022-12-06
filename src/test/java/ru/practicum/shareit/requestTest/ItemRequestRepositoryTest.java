package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    User user1;
    User user2;
    ItemRequest request1;
    ItemRequest request2;
    ItemRequest request3;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "testName1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "testName2", "test2@mail.ru"));

        request1 = itemRequestRepository
                .save(new ItemRequest(null, "testDescription1", user1, LocalDateTime.now()));
        request2 = itemRequestRepository
                .save(new ItemRequest(null, "testDescription2", user1, LocalDateTime.now()));
        request3 = itemRequestRepository
                .save(new ItemRequest(null, "testDescription3", user2, LocalDateTime.now()));

    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindAllByRequestorId() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(user1.getId());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(2, requests.size());
    }

    @Test
    void shouldFindAllByRequestorIdNotLike() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNotLike(
                user1.getId(),
                Pageable.unpaged());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(request3.getId(), requests.get(0).getId());
    }
}
