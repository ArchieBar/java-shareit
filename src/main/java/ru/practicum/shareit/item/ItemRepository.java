package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    ItemDto getItemById(Long idItem);

    List<ItemDto> getAllItemsUserById(Long idUser);

    List<ItemDto> searchThingByText(String text);

    ItemDto createItem(ItemDto itemDto, Long idOwner);

    ItemDto updateItem(ItemDto itemDto, Long idItem, Long idOwner);
}
