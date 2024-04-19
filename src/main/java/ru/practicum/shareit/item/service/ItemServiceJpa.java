package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnershipException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.util.List;

@Service("ItemServiceJpa")
public class ItemServiceJpa implements ItemService {
    @Autowired
    private ItemRepositoryJpa itemRepository;

    @Autowired
    private UserRepositoryJpa userRepository;

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(
                itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(
                        MessageFormat.format("Вещь с ID: {0} не найдена.", itemId))));
    }

    @Override
    public List<ItemDto> getAllItemsUserById(Long ownerId) {
        return ItemMapper.toListItemDto(itemRepository.findAllItemsByOwner(ownerId));
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
}
