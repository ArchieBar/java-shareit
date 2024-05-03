package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestCreatedDto> jsonCreated;

    @Autowired
    private JacksonTester<ItemRequestWithItemsDto> jsonWithItems;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testCreatedRequestDto() throws IOException {
        ItemRequestCreatedDto itemRequestCreatedDto = new ItemRequestCreatedDto();
        itemRequestCreatedDto.setDescription("description");

        JsonContent<ItemRequestCreatedDto> result = jsonCreated.write(itemRequestCreatedDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    public void testRequestWithItemsDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

        ItemRequestWithItemsDto itemRequestWithItemsDto = new ItemRequestWithItemsDto(
                1L,
                "description",
                localDateTime,
                List.of(new ItemDto(
                        1L,
                        "name",
                        "description",
                        true,
                        1L,
                        List.of(new Comment(
                                1L,
                                "text",
                                "authorName",
                                localDateTime
                        ))
                ))
        );

        JsonContent<ItemRequestWithItemsDto> result = jsonWithItems.write(itemRequestWithItemsDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        //assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(formatter.format(localDateTime));
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].authorName").isEqualTo("authorName");
        //assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].created").isEqualTo(formatter.format(localDateTime));

    }
}
