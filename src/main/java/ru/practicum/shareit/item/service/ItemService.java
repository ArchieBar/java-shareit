package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBooking;

import java.util.List;

public interface ItemService {
    ItemWithBooking getItemById(Long itemId, Long userId);

    List<ItemWithBooking> getAllItemsUserById(Long userId);

    List<ItemDto> searchThingByText(String text);

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);
}
