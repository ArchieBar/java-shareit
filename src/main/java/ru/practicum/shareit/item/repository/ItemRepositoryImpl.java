package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemDuplicateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public ItemDto getItemById(Long idItem) {
        Item item = items.get(idItem);
        if (item == null) {
            throw new ItemNotFoundException(
                    MessageFormat.format("Вещь с id: {0} не найдена.", id));
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsUserById(Long idUser) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(idUser))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchThingByText(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, User owner) {
        if (items.containsKey(itemDto.getId())) {
            throw new ItemDuplicateException(
                    MessageFormat.format("Вещь с id: {0} создана ранее.", itemDto.getId()));
        }
        Item item = new Item(
                generateId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                new ArrayList<>()
        );
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto updateItemDto, Long idItem, Long idOwner) {
        Item item = items.get(idItem);
        if (item == null) {
            throw new ItemNotFoundException(
                    MessageFormat.format("Вещь для обновления с id: {0} не найдена.", idItem));
        }
        if (!item.getOwner().getId().equals(idOwner)) {
            throw new ItemOwnershipException(
                    MessageFormat.format("Вещь с id: {0} имеет id владельца отличного от id: {1}.", idItem, idOwner));
        }
        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null && updateItemDto.getAvailable() != item.getAvailable()) {
            item.setAvailable(updateItemDto.getAvailable());
        }
        items.put(idItem, item);
        return ItemMapper.toItemDto(item);
    }

    public void deleteItem() {
        //TODO
        // Пока по ТЗ операцию по удалению не просят. Добавлю в будущем.
    }

    private Long generateId() {
        return id++;
    }
}
