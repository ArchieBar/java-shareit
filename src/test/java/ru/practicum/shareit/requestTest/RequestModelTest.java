package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestModelTest {
    @Autowired
    private JacksonTester<ItemRequestCreatedDto> jsonCreated;

    @Autowired
    private JacksonTester<ItemRequestWithItemsDto> jsonWithItems;

    @Test
    public void testCreatedRequestDto() throws IOException {
        ItemRequestCreatedDto itemRequestCreatedDto = new ItemRequestCreatedDto();
        itemRequestCreatedDto.setDescription("description");

        JsonContent<ItemRequestCreatedDto> result = jsonCreated.write(itemRequestCreatedDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    public void testRequestWithItemsDto() throws IOException {
        ItemRequestWithItemsDto itemRequestWithItemsDto = new ItemRequestWithItemsDto(
                1L,
                "description",
                LocalDateTime.now(),
                new ArrayList<>()
        );
        itemRequestWithItemsDto.addItem(new ItemDto());

        JsonContent<ItemRequestWithItemsDto> result = jsonWithItems.write(itemRequestWithItemsDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}
