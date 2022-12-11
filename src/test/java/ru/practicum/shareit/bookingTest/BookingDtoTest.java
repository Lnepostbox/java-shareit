package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    JacksonTester<BookingDtoResponse> json;

    @Test
    void bookingDtoTest() throws Exception {
        BookingDtoResponse bookingDto = new BookingDtoResponse(
                1L,
                LocalDateTime.of(2022, 12, 12, 10, 10, 1),
                LocalDateTime.of(2022, 12, 20, 10, 10, 1),
                null,
                null,
                null);

        JsonContent<BookingDtoResponse> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2022, 12, 12, 10, 10, 1).toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2022, 12, 20, 10, 10, 1).toString());
    }
}
