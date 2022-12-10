package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTest {
    @Autowired
    JacksonTester<ItemDtoResponse> json;

    @Test
    void itemDtoTest() throws Exception {
        ItemDtoResponse itemDto = new ItemDtoResponse(
                1L,
               "testName",
                "testDescription",
                true,
                null,
                null,
                null,
                null,
                null);

        JsonContent<ItemDtoResponse> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("testDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
    }
}
