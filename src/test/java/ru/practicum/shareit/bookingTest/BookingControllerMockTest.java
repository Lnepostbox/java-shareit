package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerMockTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingDtoResponse bookingDtoResponse;

    private BookingDtoRequest bookingDtoRequest;

    @BeforeEach
    void init() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("testName");
        userDto.setEmail("test@mail.ru");

        ItemDtoResponse itemDto = new ItemDtoResponse();
        itemDto.setId(1L);
        itemDto.setName("testName");
        itemDto.setDescription("testDescription");
        itemDto.setAvailable(true);

        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setStart(LocalDateTime.of(2022, 12, 12, 10, 0));
        bookingDtoResponse.setEnd(LocalDateTime.of(2022, 12, 20, 10, 0));
        bookingDtoResponse.setBooker(new BookingDtoResponse.Booker(userDto.getId(), userDto.getName()));
        bookingDtoResponse.setItem(new BookingDtoResponse.Item(itemDto.getId(), itemDto.getName()));

        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setId(1L);
        bookingDtoRequest.setStart(LocalDateTime.of(2022, 12, 12, 10, 0));
        bookingDtoRequest.setEnd(LocalDateTime.of(2022, 12, 20, 10, 0));
        bookingDtoRequest.setItemId(1L);
    }

    @Test
    void saveTest() throws Exception {
        when(bookingService.save(anyLong(), any(BookingDtoRequest.class)))
                .thenReturn(bookingDtoResponse);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoResponse)));
    }

    @Test
    void updateStatusTest() throws Exception {
        bookingDtoResponse.setStatus(Status.APPROVED);
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);
        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoResponse)));
    }

    @Test
    void findAllByOwnerIdAndStatusTest() throws Exception {
        when(bookingService.findAllByOwnerIdAndStatus(anyLong(), any(Status.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDtoResponse))));
    }

    @Test
    void findAllByUserTest() throws Exception {
        when(bookingService.findAllByStatus(anyLong(), any(Status.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDtoResponse))));
    }

    @Test
    void findAllByUserTestWithWrongStatus() throws Exception {
        when(bookingService.findAllByOwnerIdAndStatus(anyLong(), any(Status.class), anyInt(), anyInt()))
                .thenThrow(ValidationException.class);
        mvc.perform(get("/bookings?state=text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void findByIdTest() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingDtoResponse);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoResponse)));
    }
}
