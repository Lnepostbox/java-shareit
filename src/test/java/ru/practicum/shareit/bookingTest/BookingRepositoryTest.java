
package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    User user1;
    User user2;
    User user3;
    Item item1;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));
        user3 = userRepository.save(new User(null, "test3", "test3@mail.ru"));
        item1 = itemRepository
                .save(new Item(null, "testName", "testDescription", true, user2, null));
        booking1 = bookingRepository
                .save(new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        item1,
                        user1,
                        Status.APPROVED));
        booking2 = bookingRepository
                .save(new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        item1,
                        user3,
                        Status.APPROVED));
    }

    @Test
    void findByBookerIdAndCurrentOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndCurrentOrderByStartDesc(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByBookerIdAndCurrentOrderByStartDescTestEmpty() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndCurrentOrderByStartDesc(user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void findByItemOwnerIdAndCurrentOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByItemOwnerIdAndCurrentOrderByStartDesc(user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByItemOwnerIdAndCurrentOrderByStartDescTestEmpty() {
        booking1.setStart(booking1.getStart().minusHours(1));
        booking1.setEnd(booking1.getEnd().plusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByItemOwnerIdAndCurrentOrderByStartDesc(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void findLastBookingTest() {
        booking2.setEnd(booking2.getEnd().plusHours(1));
        bookingRepository.save(booking2);

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), user2.getId(), item1.getId());

        Assertions.assertNotNull(lastBooking);
        Assertions.assertEquals(lastBooking.getBooker().getName(), user1.getName());
    }

    @Test
    void findNextBookingTest() {
        booking2.setStart(booking2.getStart().plusHours(1));
        booking2.setEnd(booking2.getEnd().plusHours(2));
        bookingRepository.save(booking2);

        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), user2.getId(), item1.getId());

        Assertions.assertNotNull(nextBooking);
        Assertions.assertEquals(nextBooking.getBooker().getName(), user3.getName());
    }

    @Test
    void findByBookerIdAndEndIsBeforeOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().minusHours(2));
        booking1.setEnd(booking1.getEnd().minusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndEndIsBeforeOrderByStartDesc(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByBookerIdAndStartIsAfterOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().plusHours(2));
        booking1.setEnd(booking1.getEnd().plusHours(3));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndStartIsAfterOrderByStartDesc(user1.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDescTest() {
        booking1.setStatus(Status.REJECTED);
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndStatusOrderByStartDesc(user1.getId(), Status.REJECTED, Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByBookerIdOrderByStartDescTest() {
        List<Booking> results = bookingRepository
                .findByBookerIdOrderByStartDesc(user1.getId(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByItemOwnerIdOrderByStartDescTest() {
        List<Booking> results = bookingRepository
                .findByItemOwnerIdOrderByStartDesc(user2.getId(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }

    @Test
    void findByItemOwnerIdAndEndIsBeforeOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().minusHours(2));
        booking1.setEnd(booking1.getEnd().minusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
    }

    @Test
    void findByItemOwnerIdAndStartIsAfterOrderByStartDescTest() {
        booking1.setStart(booking1.getStart().plusHours(2));
        booking1.setEnd(booking1.getEnd().plusHours(3));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                        user2.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDescTest() {
        booking1.setStatus(Status.REJECTED);
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByItemOwnerIdAndStatusOrderByStartDesc(user2.getId(), Status.REJECTED, Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findByBookerIdAndItemIdAndEndIsBeforeTest() {
        booking1.setStart(booking1.getStart().minusHours(2));
        booking1.setEnd(booking1.getEnd().minusHours(1));
        bookingRepository.save(booking1);

        List<Booking> results = bookingRepository
                .findByBookerIdAndItemIdAndEndIsBefore(user1.getId(), item1.getId(), LocalDateTime.now());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void findAllByItemInAndStatusTest() {
        List<Booking> results = bookingRepository
                .findAllByItemInAndStatus(List.of(item1), Status.APPROVED, Sort.unsorted());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
