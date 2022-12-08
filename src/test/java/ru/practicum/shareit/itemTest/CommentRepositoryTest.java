package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "testName1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "testName2", "test2@mail.ru"));
        item1 = itemRepository
                .save(new Item(null, "testName", "testDescription", true, user2, null));
        item2 = itemRepository
                .save(new Item(null, "testName", "testDescription", true, user1, null));
        comment1 = commentRepository.save(new Comment(null, "text", item1, user1, LocalDateTime.now()));
    }

    @Test
    void findAllByItemIdTest() {
        List<Comment> results = commentRepository.findAllByItemId(item1.getId());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findAllByItemIdTestWithEmptyList() {
        List<Comment> results = commentRepository.findAllByItemId(item2.getId());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void findAllByItemInTest() {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        List<Comment> results = commentRepository.findAllByItemIn(items, Sort.unsorted());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
