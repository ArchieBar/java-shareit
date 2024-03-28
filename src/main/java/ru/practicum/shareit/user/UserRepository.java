package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository implements UserRepositoryInterface {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public Boolean findUserById(Long idUser) {
        return users.containsKey(idUser);
    }

    @Override
    public User getUserById(Long idUser) {
        User user = users.get(idUser);
        if (user == null) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /*
     * Пока плохо понимаю необходимость DTO классов, нужно ли тут возвращать UserDTO или можно оставить так?
     */
    @Override
    public User createUser(User user) {
        if (findUserById(user.getId())) {
            throw new UserDuplicateException(
                    MessageFormat.format("Пользователь с id: {0} создан ранее.", user.getId()));
        }
        emailValidation(user.getEmail());
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long idUser, User updateUser) {
        if (!findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        User user = users.get(idUser);
        if (updateUser.getEmail() != null && !user.getEmail().equalsIgnoreCase(updateUser.getEmail())) {
            emailValidation(updateUser.getEmail());
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        users.put(idUser, user);
        return user;
    }

    @Override
    public void deleteUserById(Long idUser) {
        if (!findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        users.remove(idUser);
    }

    private Long generateId() {
        return id++;
    }

    private void emailValidation(String email) {
        String emailUser = email.toLowerCase();
        long countUsers = users.values().stream()
                .filter(thisUser -> thisUser.getEmail().toLowerCase().contains(emailUser))
                .count();
        if (countUsers != 0) {
            throw new UserDuplicateException(
                    MessageFormat.format("Пользователь с email: {0} создан ранее.", email));
        }
    }
}
