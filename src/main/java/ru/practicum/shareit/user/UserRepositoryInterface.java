package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepositoryInterface {
    Boolean findUserById(Long idUser);

    User getUserById(Long idUser);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(Long idUser, User user);

    void deleteUserById(Long idUser);
}
