package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestWithItemsDto> getAllRequestsByUserId(Long ownerId);

    List<ItemRequestWithItemsDto> getAllRequests(Long ownerId, Integer from, Integer size);

    ItemRequestWithItemsDto getRequestById(Long requestId, Long ownerId);

    ItemRequestWithItemsDto createRequest(ItemRequestCreatedDto requestDto, Long ownerId);
}
