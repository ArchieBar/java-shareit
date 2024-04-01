package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    Boolean findUserById(Long idUser);

    UserDto getUserById(Long idUser);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long idUser, UserDto userDto);

    void deleteUserById(Long idUser);
}
