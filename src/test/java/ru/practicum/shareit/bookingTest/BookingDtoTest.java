package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jsonBookingDto;

    @Autowired
    private JacksonTester<BookingFromItemDto> jsonBookingFromItem;

    @Autowired
    private JacksonTester<BookingResponseDto> jsonBookingResponse;

    @Test
    public void testBookingDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();

        BookingDto booking = new BookingDto(
                1L,
                1L,
                1L,
                Status.APPROVED.name(),
                localDateTime,
                localDateTime
        );

        JsonContent<BookingDto> result = jsonBookingDto.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.name());
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
    }

    @Test
    public void testBookingFromItemDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();

        BookingFromItemDto booking = new BookingFromItemDto(
                1L,
                1L,
                Status.APPROVED,
                localDateTime,
                localDateTime
        );

        JsonContent<BookingFromItemDto> result = jsonBookingFromItem.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.name());
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");

    }

    @Test
    public void testBookingResponseDto() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();

        BookingResponseDto booking = new BookingResponseDto(
                1L,
                new UserDto(
                        1L,
                        "name",
                        "email@email.email"
                ),
                new ItemDto(
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
                ),
                Status.APPROVED,
                localDateTime,
                localDateTime
        );

        JsonContent<BookingResponseDto> result = jsonBookingResponse.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("email@email.email");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.item.comments[0].authorName").isEqualTo("authorName");
        assertThat(result).hasJsonPathValue("$.item.comments[0].created");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.name());
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
    }
}
