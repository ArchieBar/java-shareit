package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithBooking;

import java.util.List;

public interface ItemService {
    ItemWithBooking getItemById(Long itemId, Long userId);

    List<ItemWithBooking> getAllItemsUserById(Long userId);

    List<ItemDto> searchThingByText(String text);

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    Comment createComment(CommentDto commentDto, Long itemId, Long userId);
}
