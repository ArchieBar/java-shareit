package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;
import ru.practicum.shareit.user.service.UserServiceJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceJpa service;

    @Mock
    UserRepositoryJpa userRepository;


    @Test
    public void testGetUserById() {
        Long userId = 1L;

        User user = mock(User.class);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertEquals(userDto, service.getUserById(userId));
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertEquals(users.size(), service.getAllUsers().size());
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        User user = new User();

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        assertEquals(userDto, service.createUser(userDto));
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;

        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mailmail");
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        assertEquals(userDto, service.updateUser(userId, userDto));
    }

    @Test
    public void getUserByIdNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> service.getUserById(1L));
    }

    @Test
    public void updateUserNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> service.updateUser(1L, new UserDto()));
    }
}
