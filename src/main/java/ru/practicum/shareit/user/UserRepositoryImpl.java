package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import java.text.MessageFormat;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    /*
     * Ещё есть идея сделать мапу <email, user>, т.к. может потребоваться поиск по почте.
     * Но мне кажется, что делать хранилище с двумя мапами с разными ключами - плохая идея.
     * Так что оставлю пока HashSet.
     */
    private final Set<String> emailUsers = new HashSet<>();
    private Long id = 1L;

    @Override
    public Boolean findUserById(Long idUser) {
        return users.containsKey(idUser);
    }

    @Override
    public UserDto getUserById(Long idUser) {
        User user = users.get(idUser);
        if (user == null) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(new ArrayList<>(users.values()));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (findUserById(userDto.getId())) {
            throw new UserDuplicateException(
                    MessageFormat.format("Пользователь с id: {0} создан ранее.", userDto.getId()));
        }
        checkUserEmail(userDto.getEmail());
        userDto.setId(generateId());
        emailUsers.add(userDto.getEmail());
        users.put(userDto.getId(), UserMapper.toUser(userDto));
        return userDto;
    }

    @Override
    public UserDto updateUser(Long idUser, UserDto updateUserDto) {
        if (!findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        User user = users.get(idUser);
        if (updateUserDto.getEmail() != null && !user.getEmail().equalsIgnoreCase(updateUserDto.getEmail())) {
            checkUserEmail(updateUserDto.getEmail());
            emailUsers.remove(user.getEmail());
            user.setEmail(updateUserDto.getEmail());
            emailUsers.add(user.getEmail());
        }
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        users.put(idUser, user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long idUser) {
        if (!findUserById(idUser)) {
            throw new UserNotFoundException(
                    MessageFormat.format("Пользователь с id: {0} не найден.", idUser));
        }
        emailUsers.remove(users.get(idUser).getEmail());
        users.remove(idUser);
    }

    private Long generateId() {
        return id++;
    }

    /*
     * Не уверен, что это лучшее решение.
     * Но как по другому проверить валидность почты при обновлении я не нашел :(
     */
    private void checkUserEmail(@Email String email) {
        if (emailUsers.contains(email)) {
            throw new UserDuplicateException(
                    MessageFormat.format("Пользователь с email: {0} создан ранее.", email));
        }
    }
}
