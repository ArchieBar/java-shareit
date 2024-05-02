package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemWithBookingDto getItemById(Long idItem, Long userId) {
        return null;
    }

    @Override
    public List<ItemWithBookingDto> getAllItemsUserById(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public List<ItemDto> searchThingByText(String text, Integer from, Integer size) {
        return null;
    }

    @Override
    public Comment createComment(CommentDto commentDto, Long itemId, Long userId) {
        return null;
    }

    public List<ItemWithBookingDto> getAllItemsUserById(Long idUser) {
        verifyExistenceOfUser(idUser);
        itemRepository.getAllItemsUserById(idUser);
        return null;
    }

    public List<ItemDto> searchThingByText(String text) {
        return itemRepository.searchThingByText(text);
    }

    public ItemDto createItem(ItemDto itemDto, Long idOwner) {
        return itemRepository.createItem(itemDto, UserMapper.toUser(userRepository.getUserById(idOwner)));
    }

    public ItemDto updateItem(ItemDto updateItemDto, Long idItem, Long idOwner) {
        verifyExistenceOfUser(idOwner);
        return itemRepository.updateItem(updateItemDto, idItem, idOwner);
    }

    private void verifyExistenceOfUser(Long idUser) {
        if (!userRepository.findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
    }
}
