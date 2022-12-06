package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String SHAREIT_HEADER = "X-Sharer-User-Id";

    @Test
    void shouldSaveBooking() throws Exception {
        BookingDtoResponse bookingDto = new BookingDtoResponse(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                null,
                null,
                null);

        Mockito.when(bookingService.save(Mockito.anyLong(), Mockito.any(BookingDtoRequest.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(SHAREIT_HEADER, 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));
    }

    @Test
    void shouldUpdateState() throws Exception {
        BookingDtoResponse bookingDto = new BookingDtoResponse(1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BookingDtoResponse.Item(1L, "testName"),
                new BookingDtoResponse.Booker(1L, "testName"),
                Status.APPROVED);

        Mockito.when(bookingService.updateState(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status",
                        Matchers.is(bookingDto.getStatus().toString()),
                        Boolean.class))
                .andExpect(jsonPath("$.booker.id",
                        Matchers.is(bookingDto.getBooker().getId()),
                        Long.class))
                .andExpect(jsonPath("$.item.id",
                        Matchers.is(bookingDto.getItem().getId()),
                        Long.class));
    }
}
