package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private ItemRequestRepository repository;

    @Autowired
    private UserRepositoryJpa userRepository;

    private User user;

    private User userSec;

    private ItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("user");
        user.setEmail("user@email.email");
        user = userRepository.save(user);

        userSec = new User();
        userSec.setName("userSec");
        userSec.setEmail("userSec@email.email");
        userSec = userRepository.save(userSec);

        itemRequest = new ItemRequest();
        itemRequest.setOwner(user);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setItems(new ArrayList<>());
        itemRequest = repository.save(itemRequest);
    }

    @Test
    public void testFindAllByOwnerId() {
        ItemRequest result = repository.findAllByOwnerId(user.getId()).get(0);

        assertThat(result, equalTo(itemRequest));
    }

    @Test
    public void testFindAllByOwnerIdNot() {
        Pageable pageable = PageRequest.of(0, 10);

        ItemRequest result = repository.findAllByOwnerIdNot(userSec.getId(), pageable).getContent().get(0);

        assertThat(result, equalTo(itemRequest));
    }
}
