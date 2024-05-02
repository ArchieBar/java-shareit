package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

/*
 * Оставил на всякий случай.
 */
public interface UserRepository {
    Boolean findUserById(Long idUser);

    UserDto getUserById(Long idUser);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long idUser, UserDto userDto);

    void deleteUserById(Long idUser);
}
