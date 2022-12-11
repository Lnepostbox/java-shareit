package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class CommentServiceImplMockTest {

    CommentService commentService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        commentService = new CommentServiceImpl(
                commentRepository,
                itemRepository,
                userRepository,
                bookingRepository
        );
    }

    @Test
    void saveTest() {
        CommentDto commentDto = new CommentDto(null, "text", null, null);
        CommentDto commentInfoDto = new CommentDto(1L, "text", "testName", LocalDateTime.now());
        User user = new User(1L, "testName", "test@mail.com");
        Item item = new Item(1L, "testName", "testDescription", true, user,  null);
        Comment comment = new Comment(1L, "text", item, user, LocalDateTime.now());
        Booking booking = new Booking(null, null, null, null, null, Status.APPROVED);
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito.when(bookingRepository.findByBookerIdAndItemIdAndEndIsBefore(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        CommentDto foundComment = commentService.save(1L, 1L, commentDto);

        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(commentInfoDto.getId(), foundComment.getId());
        Assertions.assertEquals(commentInfoDto.getText(), foundComment.getText());
        Assertions.assertEquals(commentInfoDto.getAuthorName(), foundComment.getAuthorName());
    }

}
