package ru.practicum.shareit.request.model;

import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static ItemRequestWithItemsDto toRequestWithItemsDto(ItemRequest itemRequest) {
        return new ItemRequestWithItemsDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                ItemMapper.toListItemDto(itemRequest.getItems())
        );
    }

    public static List<ItemRequestWithItemsDto> toRequestWithItemsDtoList(List<ItemRequest> itemRequestList) {
        return itemRequestList.stream()
                .map(RequestMapper::toRequestWithItemsDto)
                .collect(Collectors.toList());
    }
}
