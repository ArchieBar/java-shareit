package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepositoryJpa userRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository requestRepository, UserRepositoryJpa userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllRequestsByUserId(Long ownerId) {
        findUserById(ownerId);

        List<ItemRequest> requests = requestRepository.findAllByOwnerId(ownerId);

        return RequestMapper.toRequestWithItemsDtoList(requests);
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllRequests(Long ownerId, Integer from, Integer size) {
        findUserById(ownerId);

        Pageable pageable = PageRequest.of(from, size, Sort.by("created"));

        Page<ItemRequest> requests = requestRepository.findAllByOwnerIdNot(ownerId, pageable);

        return RequestMapper.toRequestWithItemsDtoList(requests.getContent());
    }

    @Override
    public ItemRequestWithItemsDto getRequestById(Long requestId, Long ownerId) {
        findUserById(ownerId);

        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException(requestId));


        return RequestMapper.toRequestWithItemsDto(request);
    }

    @Override
    public ItemRequestWithItemsDto createRequest(ItemRequestCreatedDto requestDto, Long ownerId) {
        User owner = findUserById(ownerId);
        ItemRequest request = new ItemRequest(requestDto, owner);
        return RequestMapper.toRequestWithItemsDto(requestRepository.save(request));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", userId)));
    }
}
