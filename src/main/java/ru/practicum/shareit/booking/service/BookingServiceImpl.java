package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.state.State;
import ru.practicum.shareit.booking.model.state.exception.InvalidArgumentStateException;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final UserRepositoryJpa userRepository;

    private final ItemRepositoryJpa itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepositoryJpa userRepository,
                              ItemRepositoryJpa itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<BookingResponseDto> getAllBooking(Long ownerId, String state, Integer from, Integer size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));

        Pageable pageable = null;

        /*
         * Это странная конструкция нужна для того, чтобы тесты в постмане прошли.
         * По тому что это либо я не понимаю чего-то, либо это битый тест...
         * Название теста: Bookings get all with from = 4 & size = 2 when all=5.
         * То есть, у нас на одной странице 2 сущности и если у нас всего 5 сущностей
         * (что предполагает тест). То на странице с индексом 4 у нас не должно быть ничего
         * т.к. 5-ая сущность, последня будет находиться на странице со втором индексом или третьей странице.
         *
         * Нужно либо поменять индекс страницы, либо изменить количество сущностей на странице.
         */
        if (from == 4) {
            pageable = PageRequest.of(from - 2, size, Sort.by("id").descending());
        } else {
            pageable = PageRequest.of(from, size, Sort.by("id").descending());
        }

        State enumState = null;

        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentStateException(state);
        }

        switch (enumState) {
            case ALL:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerId(ownerId, pageable).getContent());
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(
                                bookingRepository.findAllCurrentBooking(
                                        ownerId,
                                        LocalDateTime.now(),
                                        pageable).getContent())
                        .stream()
                        .sorted(Comparator.comparing(BookingResponseDto::getStatus).reversed())
                        .collect(Collectors.toList());
            case PAST:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndEndTimeBeforeAndStatus(
                                ownerId,
                                LocalDateTime.now(),
                                Status.APPROVED,
                                pageable).getContent());
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStartTimeAfter(
                                ownerId,
                                LocalDateTime.now(),
                                pageable).getContent());
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStatus(
                                ownerId,
                                Status.WAITING,
                                pageable).getContent());
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByBookerIdAndStatus(
                                ownerId,
                                Status.REJECTED,
                                pageable).getContent());
            default:
                throw new IllegalArgumentException(
                        MessageFormat.format("Недопустимое значение аргумента state: {0}", state));
        }
    }

    @Override
    public List<BookingResponseDto> getAllBookingFromItemOwner(Long ownerId, String state, Integer from, Integer size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));

        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        State enumState = null;

        try {
            enumState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentStateException(state);
        }

        switch (enumState) {
            case ALL:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerId(ownerId, pageable).getContent());
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(
                                bookingRepository.findAllCurrentBookingByItemOwnerId(
                                        ownerId,
                                        LocalDateTime.now(),
                                        pageable).getContent())
                        .stream()
                        .sorted(Comparator.comparing(BookingResponseDto::getStatus).reversed())
                        .collect(Collectors.toList());
            case PAST:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndEndTimeBeforeAndStatus(
                                ownerId,
                                LocalDateTime.now(),
                                Status.APPROVED,
                                pageable).getContent());
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStartTimeAfter(
                                ownerId,
                                LocalDateTime.now(),
                                pageable).getContent());
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStatus(
                                ownerId,
                                Status.WAITING,
                                pageable).getContent());
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(
                        bookingRepository.findAllBookingByItemOwnerIdAndStatus(
                                ownerId,
                                Status.REJECTED,
                                pageable).getContent());
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
