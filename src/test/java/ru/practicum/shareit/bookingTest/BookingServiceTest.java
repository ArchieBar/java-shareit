package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepositoryJpa itemRepository;

    @Test
    public void testGetAllBookingStateALL() {
        Long ownerId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByBookerId(ownerId, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingStateCURRENT() {
        Long ownerId = 1L;
        String state = "CURRENT";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllCurrentBooking(eq(ownerId), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingStatePAST() {
        Long ownerId = 1L;
        String state = "PAST";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByBookerIdAndEndTimeBeforeAndStatus(
                eq(ownerId),
                any(LocalDateTime.class),
                eq(Status.APPROVED),
                eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingStateFUTURE() {
        Long ownerId = 1L;
        String state = "FUTURE";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByBookerIdAndStartTimeAfter(eq(ownerId), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingStateWAITING() {
        Long ownerId = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByBookerIdAndStatus(
                ownerId,
                Status.WAITING,
                pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingStateREJECTED() {
        Long ownerId = 1L;
        String state = "REJECTED";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByBookerIdAndStatus(
                ownerId,
                Status.REJECTED,
                pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBooking(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerMustHaveListLengthOneAndBookerIdOne() {
        Long ownerId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByItemOwnerId(ownerId, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerCURRENT() {
        Long ownerId = 1L;
        String state = "CURRENT";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllCurrentBookingByItemOwnerId(anyLong(), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerPAST() {
        Long ownerId = 1L;
        String state = "PAST";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByItemOwnerIdAndEndTimeBeforeAndStatus(anyLong(), any(LocalDateTime.class), any(Status.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerFUTURE() {
        Long ownerId = 1L;
        String state = "FUTURE";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByItemOwnerIdAndStartTimeAfter(anyLong(), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerWAITING() {
        Long ownerId = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByItemOwnerIdAndStatus(anyLong(), any(Status.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllBookingFromItemOwnerREJECTED() {
        Long ownerId = 1L;
        String state = "REJECTED";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        User user = new User();
        user.setId(ownerId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllBookingByItemOwnerIdAndStatus(anyLong(), any(Status.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingResponseDto> result = bookingService.getAllBookingFromItemOwner(ownerId, state, from, size);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetBookingById() {
        Long userId = 1L;
        Long bookingId = 1L;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(any(Long.class));

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingResponseDto bookingResponseDto = bookingService.getBookingById(userId, bookingId);

        assertEquals(1, bookingResponseDto.getId());
    }

    @Test
    public void createBooking() {
        Long bookingId = 1L;
        Long userId = 1L;
        Long ownerItemId = 2L;
        Long itemId = 1L;
        LocalDateTime dataTimeStart = LocalDateTime.now().minusHours(1);
        LocalDateTime dataTimeEnd = LocalDateTime.now().plusHours(2);

        User user = new User();
        user.setId(userId);

        User ownerItem = new User();
        ownerItem.setId(ownerItemId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(ownerItem);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(dataTimeStart);
        bookingDto.setEnd(dataTimeEnd);

        Booking booking = new Booking(
                user,
                item,
                dataTimeStart,
                dataTimeEnd
        );
        booking.setId(bookingId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingResponseDto bookingResponseDto = bookingService.createBooking(userId, bookingDto);

        assertEquals(1, bookingResponseDto.getId());
    }

    @Test
    public void testCreateBookingThrowUserNotFound() {
        LocalDateTime dataTimeStart = LocalDateTime.now().minusHours(1);
        LocalDateTime dataTimeEnd = LocalDateTime.now().plusHours(2);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(dataTimeStart);
        bookingDto.setEnd(dataTimeEnd);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.createBooking(1L, bookingDto)
        );

        Assertions.assertEquals("Пользователь с ID: 1 не найден.", exception.getMessage());
    }

    @Test
    public void testCreateBookingThrowStartTimeException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().minusHours(1));

        Assertions.assertThrows(
                BookingStartTimeException.class,
                () -> bookingService.createBooking(1L, bookingDto)
        );
    }

    @Test
    public void testCreateBookingThrowYourOwnException() {
        Long userId = 1L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        Assertions.assertThrows(
                BookingYourOwnException.class,
                () -> bookingService.createBooking(userId, bookingDto)
        );
    }

    @Test
    public void testCreateBookingThrowItemNotAvailableException() {
        Long userId = 1L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(false);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        Assertions.assertThrows(
                ItemNotAvailableForBookingException.class,
                () -> bookingService.createBooking(2L, bookingDto)
        );
    }

    @Test
    public void testUpdateApprovedBooking() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(booking))
                .thenReturn(booking);

        BookingResponseDto resultService = bookingService.updateApprovedBooking(user.getId(), booking.getId(), true);

        assertThat(resultService.getId(), equalTo(booking.getId()));
    }

    @Test
    public void testUpdateApprovedBookingNotFound() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.updateApprovedBooking(1L, 1L, true)
        );
    }

    @Test
    public void testUpdateApprovedBookingOwnership() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                BookingOwnershipException.class,
                () -> bookingService.updateApprovedBooking(100L, 1L, true)
        );
    }

    @Test
    public void testUpdateApprovedBookingDoubleApproved() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(2));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                DoubleApprovedException.class,
                () -> bookingService.updateApprovedBooking(user.getId(), booking.getId(), true)
        );
    }
}


