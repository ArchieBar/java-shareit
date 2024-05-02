package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.practicum.shareit.item.model.item.dto.ItemWithBooking;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ItemServiceJpa")
public class ItemServiceJpa implements ItemService {
    @Autowired
    private ItemRepositoryJpa itemRepository;

    @Autowired
    private UserRepositoryJpa userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ItemWithBooking getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(
                MessageFormat.format("Вещь с ID: {0} не найдена.", itemId)));
        Pageable pageableLast = PageRequest.of(0, 1, Sort.by("endTime").descending());
        Pageable pageableNext = PageRequest.of(0, 1, Sort.by("startTime").ascending());
        Optional<Booking> lastBookingOptional =
                bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(itemId, LocalDateTime.now(), pageableLast, Status.APPROVED)
                        .stream().findFirst();
        Optional<Booking> nextBookingOptional =
                bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(itemId, LocalDateTime.now(), pageableNext, Status.APPROVED)
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
    public List<ItemWithBooking> getAllItemsUserById(Long ownerId) {
        List<Item> items = itemRepository.findAllItemsByOwner(ownerId);
        List<ItemWithBooking> itemsWithBooking = new ArrayList<>();
        Pageable pageableLast = PageRequest.of(0, 1, Sort.by("endTime").descending());
        Pageable pageableNext = PageRequest.of(0, 1, Sort.by("startTime").ascending());
        for (Item item : items) {
            Optional<Booking> lastBookingOptional =
                    bookingRepository.findFirstBookingByItemIdAndStartTimeBeforeAndStatus(item.getId(), LocalDateTime.now(), pageableLast, Status.APPROVED)
                            .stream().findFirst();
            Optional<Booking> nextBookingOptional =
                    bookingRepository.findFirstBookingByItemIdAndStartTimeAfterAndStatus(item.getId(), LocalDateTime.now(), pageableNext, Status.APPROVED)
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
        return itemsWithBooking;
    }

    @Override
    public List<ItemDto> searchThingByText(String text) {
        return ItemMapper.toListItemDto(
                itemRepository.findAllItemsByText(text));
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", ownerId)));
        return ItemMapper.toItemDto(itemRepository.save(
                new Item(
                        itemDto.getName(),
                        itemDto.getDescription(),
                        itemDto.getAvailable(),
                        owner
                )));
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
        List<Booking> bookings = bookingRepository.findAllBookingByItemIdAndBookerIdAndEndTimeBefore(itemId, userId, LocalDateTime.now());
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
