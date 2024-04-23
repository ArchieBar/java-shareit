package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/*
 * Оставил на всякий случай.
 */
public interface ItemRepository {
    ItemDto getItemById(Long idItem);

    List<ItemDto> getAllItemsUserById(Long idUser);

    List<ItemDto> searchThingByText(String text);

    ItemDto createItem(ItemDto itemDto, User owner);

    ItemDto updateItem(ItemDto itemDto, Long idItem, Long idOwner);
}
