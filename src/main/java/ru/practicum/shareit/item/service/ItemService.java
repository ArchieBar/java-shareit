package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    ItemWithBookingDto getItemById(Long itemId, Long userId);

    List<ItemWithBookingDto> getAllItemsUserById(Long userId, Integer from, Integer size);

    List<ItemDto> searchThingByText(String text, Integer from, Integer size);

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    Comment createComment(CommentDto commentDto, Long itemId, Long userId);
}
