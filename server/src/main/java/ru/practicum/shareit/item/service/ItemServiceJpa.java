package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.BookingNotFoundThisTimeException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;
import ru.practicum.shareit.booking.model.status.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("itemServiceJpa")
public class ItemServiceJpa implements ItemService {
    private final ItemRepositoryJpa itemRepository;

    private final UserRepositoryJpa userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final ItemRequestRepository requestRepository;

    public ItemServiceJpa(ItemRepositoryJpa itemRepository,
                          UserRepositoryJpa userRepository,
                          BookingRepository bookingRepository,
                          CommentRepository commentRepository, ItemRequestRepository requestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public ItemWithBookingDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(
                MessageFormat.format("Вещь с ID: {0} не найдена.", itemId)));
        Pageable pageableLast = PageRequest.of(0, 1, Sort.by("endTime").descending());
        Pageable pageableNext = PageRequest.of(0, 1, Sort.by("startTime").ascending());
        LocalDateTime timeNow = LocalDateTime.now();
        Optional<Booking> lastBookingOptional =
                bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(itemId, timeNow, pageableLast, Status.APPROVED)
                        .stream().findFirst();
        Optional<Booking> nextBookingOptional =
                bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(itemId, timeNow, pageableNext, Status.APPROVED)
                        .stream().findFirst();
        BookingFromItemDto lastBooking = null;
        BookingFromItemDto nextBooking = null;
        if (!item.getOwner().getId().equals(userId)) {
            return ItemMapper.toItemWithBooking(item, lastBooking, nextBooking);
        }
        if (lastBookingOptional.isPresent()) {
            lastBooking = new BookingFromItemDto(
                    lastBookingOptional.get().getId(),
                    lastBookingOptional.get().getBooker().getId(),
                    lastBookingOptional.get().getStatus(),
                    lastBookingOptional.get().getStartTime(),
                    lastBookingOptional.get().getEndTime()
            );
        }
        if (nextBookingOptional.isPresent()) {
            nextBooking = new BookingFromItemDto(
                    nextBookingOptional.get().getId(),
                    nextBookingOptional.get().getBooker().getId(),
                    nextBookingOptional.get().getStatus(),
                    nextBookingOptional.get().getStartTime(),
                    nextBookingOptional.get().getEndTime()
            );
        }
        return ItemMapper.toItemWithBooking(item, lastBooking, nextBooking);
    }

    @Override
    public List<ItemWithBookingDto> getAllItemsUserById(Long ownerId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        List<Item> items = itemRepository.findAllItemsByOwnerId(ownerId, pageable).getContent();

        List<ItemWithBookingDto> itemsWithBooking = new ArrayList<>();

        Pageable pageableLast = PageRequest.of(0, 1, Sort.by("endTime").descending());
        Pageable pageableNext = PageRequest.of(0, 1, Sort.by("startTime").ascending());

        LocalDateTime timeNow = LocalDateTime.now();

        for (Item item : items) {
            Optional<Booking> lastBookingOptional =
                    bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(item.getId(), timeNow, pageableLast, Status.APPROVED)
                            .stream().findFirst();

            Optional<Booking> nextBookingOptional =
                    bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(item.getId(), timeNow, pageableNext, Status.APPROVED)
                            .stream().findFirst();

            BookingFromItemDto lastBooking = null;
            BookingFromItemDto nextBooking = null;

            if (lastBookingOptional.isPresent()) {
                lastBooking = new BookingFromItemDto(
                        lastBookingOptional.get().getId(),
                        lastBookingOptional.get().getBooker().getId(),
                        lastBookingOptional.get().getStatus(),
                        lastBookingOptional.get().getStartTime(),
                        lastBookingOptional.get().getEndTime()
                );
            }

            if (nextBookingOptional.isPresent()) {
                nextBooking = new BookingFromItemDto(
                        nextBookingOptional.get().getId(),
                        nextBookingOptional.get().getBooker().getId(),
                        nextBookingOptional.get().getStatus(),
                        nextBookingOptional.get().getStartTime(),
                        nextBookingOptional.get().getEndTime()
                );
            }

            itemsWithBooking.add(ItemMapper.toItemWithBooking(item, lastBooking, nextBooking));
        }

        return itemsWithBooking.stream()
                .sorted(Comparator.comparing(ItemWithBookingDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchThingByText(String text, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        return ItemMapper.toListItemDto(
                itemRepository.findAllItemsByText(text, pageable).getContent());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));

        Item item = itemRepository.save(
                ItemMapper.toNewItem(itemDto, owner)
        );

        if (itemDto.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new RequestNotFoundException(itemDto.getRequestId()));

            request.addItem(item);
            requestRepository.save(request);

            item.setRequest(request);
            item = itemRepository.save(item);
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(
                MessageFormat.format("Вещь с ID: {0} не найдена.", itemId)));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemOwnershipException(
                    MessageFormat.format("Вещь с id: {0} имеет id владельца отличного от id: {1}.", itemId, ownerId));
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null && itemDto.getAvailable() != item.getAvailable()) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public Comment createComment(CommentDto commentDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(
                MessageFormat.format("Вещь с ID: {0} не найдена.", itemId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с id: {0} не найден.", userId)));
        LocalDateTime timeNow = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toLocalDateTime();
        List<Booking> bookings = bookingRepository.findAllBookingByItemIdAndBookerIdAndEndTimeBefore(itemId, userId, timeNow);
        if (!bookings.isEmpty()) {
            Comment comment = new Comment(user.getName(), commentDto);
            item.addComment(commentRepository.save(comment));
            itemRepository.save(item);
            return comment;
        } else {
            throw new BookingNotFoundThisTimeException("Пользователь не бронировал эту вещь");
        }

    }
}
