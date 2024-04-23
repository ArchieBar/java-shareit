package ru.practicum.shareit.item.model.item;

import ru.practicum.shareit.booking.model.dto.BookingFromItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBooking;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getComments()
        );
    }

    public static List<ItemDto> toListItemDto(List<Item> listItem) {
        return listItem.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public static ItemWithBooking toItemWithBooking(Item item,
                                                    BookingFromItemDto lastBooking,
                                                    BookingFromItemDto nextBooking) {
        return new ItemWithBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getComments(),
                lastBooking,
                nextBooking
        );
    }

    public static Item toNewItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                new ArrayList<>()
        );
    }
}
