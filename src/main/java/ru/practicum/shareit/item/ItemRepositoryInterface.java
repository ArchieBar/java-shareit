package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryInterface {
    ItemDto getItemById(Long idItem);

    List<ItemDto> getAllItemsUserById(Long idUser);

    List<ItemDto> searchThingByText(String text);

    ItemDto createItem(Item item, Long idOwner);

    ItemDto updateItem(Item item, Long idItem, Long idOwner);
}
