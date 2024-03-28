package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemDuplicateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.item.model.Item;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepository implements ItemRepositoryInterface {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public ItemDto getItemById(Long idItem) {
        Item item = items.get(idItem);
        if (item == null) {
            throw new ItemNotFoundException(
                    MessageFormat.format("Вещь с id: {0} не найдена.", id));
        }
        return makeItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsUserById(Long idUser) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(idUser))
                .map(this::makeItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchThingByText(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .map(this::makeItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(Item item, Long idOwner) {
        if (items.containsKey(item.getId())) {
            throw new ItemDuplicateException(
                    MessageFormat.format("Вещь с id: {0} создана ранее.", item.getId()));
        }
        item.setId(generateId());
        item.setOwner(idOwner);
        items.put(item.getId(), item);
        return makeItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item updateItem, Long idItem, Long idOwner) {
        if (!items.containsKey(idItem)) {
            throw new ItemNotFoundException(
                    MessageFormat.format("Вещь для обновления с id: {0} не найдена.", idItem));
        }
        if (!items.get(idItem).getOwner().equals(idOwner)) {
            throw new ItemOwnershipException(
                    MessageFormat.format("Вещь с id: {0} имеет id владельца отличного от id: {1}.", idItem, idOwner));
        }
        Item item = items.get(idItem);
        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null && updateItem.getAvailable() != item.getAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }
        items.put(idItem, item);
        return makeItemDto(item);
    }

    public void deleteItem() {
        //TODO
        // Пока по ТЗ операцию по удалению не просят. Добавлю в будущем.
    }

    private Long generateId() {
        return id++;
    }

    /*
     * Может лучше перенести этот метод в класс Item?
     */
    private ItemDto makeItemDto(Item item) {
        //FIXME
        // Скорее всего класс ItemDto должен выглядеть иначе, пометки есть в самом классе.
        return new ItemDto(
                item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }
}
