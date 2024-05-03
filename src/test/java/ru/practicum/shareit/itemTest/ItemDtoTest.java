package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> jsonItemDto;

    @Autowired
    private JacksonTester<ItemWithBookingDto> jsonItemBookingDto;

    @Test
    public void testItemDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

        ItemDto item = new ItemDto(
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

        );

        JsonContent<ItemDto> result = jsonItemDto.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("authorName");
        //assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo(formatter.format(localDateTime));
    }

    @Test
    public void testItemWithBookingDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

        ItemWithBookingDto item = new ItemWithBookingDto(
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
                )),
                new BookingFromItemDto(
                        1L,
                        1L,
                        Status.APPROVED,
                        localDateTime,
                        localDateTime
                ),
                new BookingFromItemDto(
                        1L,
                        1L,
                        Status.APPROVED,
                        localDateTime,
                        localDateTime
                )
        );

        JsonContent<ItemWithBookingDto> result = jsonItemBookingDto.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("authorName");
        //assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo(formatter.format(localDateTime));
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.status").isEqualTo(Status.APPROVED.name());
        //assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo(formatter.format(localDateTime));
        //assertThat(result).extractingJsonPathStringValue("$.lastBooking.end").isEqualTo(formatter.format(localDateTime));

    }
}
