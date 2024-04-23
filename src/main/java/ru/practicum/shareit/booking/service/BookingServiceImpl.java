package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.state.State;
import ru.practicum.shareit.booking.model.state.exception.IllegalArgumentsStateException;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepositoryJpa userRepository;
    @Autowired
    private ItemRepositoryJpa itemRepository;

    @Override
    public List<BookingResponseDto> getAllBooking(Long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));
        State enumState = null;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentsStateException(state);
        }
        switch (enumState) {
            case ALL:
                return BookingMapper.toBookingResponseDtoList(bookingRepository.findAllBookingByBookerId(ownerId));
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllCurrentBooking(ownerId, LocalDateTime.now()));
            case PAST:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndEndTimeBefore(ownerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStartTimeAfter(ownerId, LocalDateTime.now()));
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStatus(ownerId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStatus(ownerId, Status.REJECTED));
            default:
                throw new IllegalArgumentException(
                        MessageFormat.format("Недопустимое значение аргумента state: {0}", state));
        }
    }

    @Override
    public List<BookingResponseDto> getAllBookingFromItemOwner(Long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));
        State enumState = null;
        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentsStateException(state);
        }
        switch (enumState) {
            case ALL:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerId(ownerId));
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllCurrentBookingByItemOwnerId(ownerId, LocalDateTime.now()));
            case PAST:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndEndTimeBefore(ownerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStartTimeAfter(ownerId, LocalDateTime.now()));
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStatus(ownerId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStatus(ownerId, Status.REJECTED));
            default:
                throw new IllegalArgumentException(
                        MessageFormat.format("Недопустимое значение аргумента state: {0}", state));
        }
    }

    @Override
    public BookingResponseDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(
                MessageFormat.format("Бронирование с ID: {0} не найдено.", bookingId)));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingOwnershipException(
                    MessageFormat.format("ID {0} владельца вещи или бронирования не совпадают.", userId));
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto createBooking(Long userId, BookingDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new BookingStartTimeException("Дата начала бронирования должна быть до её окончания.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", userId)));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ItemNotFoundException(
                MessageFormat.format("Вещь с ID: {0} не найдена.", bookingDto.getItemId())));
        if (item.getOwner().getId().equals(userId)) {
            throw new BookingYourOwnException("Попытка брони своей же вещи.");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableForBookingException(
                    MessageFormat.format("Вещь с ID: {0} не доступна для бронирования.", item.getId()));
        }

        Booking booking = new Booking(
                user,
                item,
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }


    @Override
    public BookingResponseDto updateApprovedBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(
                MessageFormat.format("Бронирование с ID: {0} не найдено.", bookingId)));
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new BookingOwnershipException(
                    MessageFormat.format("ID {0} владельца бронирования не совпадает.", ownerId));
        }
        if (approved) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new DoubleApprovedException("Бронь уже подтверждена");
            }
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }
}
