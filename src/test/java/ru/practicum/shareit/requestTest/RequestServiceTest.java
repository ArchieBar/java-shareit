package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @InjectMocks
    private ItemRequestServiceImpl service;
    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepositoryJpa userRepository;

    @Test
    public void testGetAllRequestsByUserId() {
        Long ownerId = 1L;
        List<ItemRequest> requests = new ArrayList<>();
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByOwnerId(ownerId)).thenReturn(requests);

        assertEquals(requests.size(), service.getAllRequestsByUserId(ownerId).size());
    }

    @Test
    public void testGetAllRequests() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 10;

        List<ItemRequest> requests = new ArrayList<>();
        Page<ItemRequest> page = new PageImpl<>(requests);

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByOwnerIdNot(ownerId, PageRequest.of(from, size, Sort.by("created"))))
                .thenReturn(page);

        assertEquals(requests.size(), service.getAllRequests(ownerId, from, size).size());
    }

    @Test
    public void testGetRequestById() {
        Long requestId = 1L;
        Long ownerId = 1L;

        ItemRequest request = Mockito.mock(ItemRequest.class);

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId))
                .thenReturn(Optional.of(request));

        assertEquals(request.getId(), service.getRequestById(requestId, ownerId).getId());
    }

    @Test
    public void testCreateRequest() {
        Long ownerId = 1L;

        ItemRequestCreatedDto requestDto = new ItemRequestCreatedDto();
        User owner = new User();
        ItemRequest request = new ItemRequest(requestDto, owner);
        request.addItem(new Item());

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(request);

        assertEquals(request.getId(), service.createRequest(requestDto, ownerId).getId());
    }

    @Test
    public void testCreateRequestUserNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> service.createRequest(new ItemRequestCreatedDto(), 1L)
        );
    }

    @Test
    public void testGetRequestByIdNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                RequestNotFoundException.class,
                () -> service.getRequestById(1L, 1L)
        );
    }
}
