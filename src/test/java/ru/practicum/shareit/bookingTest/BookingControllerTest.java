package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingOwnershipException;
import ru.practicum.shareit.booking.exception.DoubleApprovedException;
import ru.practicum.shareit.booking.exception.ItemNotAvailableForBookingException;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.state.exception.InvalidArgumentStateException;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @MockBean
    private BookingService service;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    public void setUp() {
        UserDto userDto = new UserDto(
                1L,
                "name",
                "email@email.ru"
        );

        ItemDto itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                new ArrayList<>()
        );

        bookingResponseDto = new BookingResponseDto(
                1L,
                userDto,
                itemDto,
                Status.APPROVED,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );
    }

    @Test
    public void getAllBookings() throws Exception {
        when(service.getAllBooking(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", is(bookingResponseDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].item", is(bookingResponseDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto.getStatus().toString()), String.class));
    }

    @Test
    public void getAllBookingsForOwner() throws Exception {
        when(service.getAllBookingFromItemOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", is(bookingResponseDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].item", is(bookingResponseDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto.getStatus().toString()), String.class));
    }

    @Test
    public void getBookingById() throws Exception {
        when(service.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingResponseDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingResponseDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString()), String.class));
    }

    @Test
    public void createBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStatus(Status.APPROVED.name());
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));

        when(service.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(bookingResponseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingResponseDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingResponseDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString()), String.class));
    }

    @Test
    public void updateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingResponseDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingResponseDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString()), String.class));
    }

    @Test
    public void throwBookingNotFoundExceptionUpdateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new BookingNotFoundException("Бронирование не найдено"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Объект не найден"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("Бронирование не найдено"), String.class));
    }

    @Test
    public void throwBookingOwnershipExceptionUpdateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new BookingOwnershipException("ID бронирования не совпадет"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Объект не найден"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("ID бронирования не совпадет"), String.class));
    }

    @Test
    public void throwDoubleApprovedExceptionUpdateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new DoubleApprovedException("Бронь уже подтверждена"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Ошибка валидации"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("Бронь уже подтверждена"), String.class));
    }

    @Test
    public void throwItemNotAvailableForBookingExceptionUpdateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new ItemNotAvailableForBookingException("Вещь не доступна для бронирования"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Вещь не доступна для бронирования"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("Вещь не доступна для бронирования"), String.class));

    }

    @Test
    public void throwableServerErrorUpdateApprovedBooking() throws Exception {
        when(service.updateApprovedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new RuntimeException("Что-то пошло не так"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Ошибка сервера"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("Что-то пошло не так"), String.class));

    }

    @Test
    public void throwInvalidArgumentStateExceptionGetAllBookings() throws Exception {
        when(service.getAllBooking(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(new InvalidArgumentStateException("state"));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Unknown state: state"), String.class))
                .andExpect(jsonPath("$.errorMessage", is("Unknown state: state"), String.class));
    }
}
