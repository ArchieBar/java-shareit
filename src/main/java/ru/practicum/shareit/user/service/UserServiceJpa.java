package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.text.MessageFormat;
import java.util.List;

@Service("UserServiceJpa")
public class UserServiceJpa implements UserService {
    @Autowired
    private UserRepositoryJpa userRepository;

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format("Пользователь с ID: {0} не найден.", userId))));
    }

    //FIXME
    // Тут лучше вернуть Page<UserDto>, но тесты просят список
    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(userRepository.findAll());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(
                userRepository.saveAndFlush(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                MessageFormat.format("Пользователь с ID: {0} не найден.", userId)));
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        return UserMapper.toUserDto(userRepository.saveAndFlush(user));
    }

    //FIXME
    // Не уверен, нужно ли проверять на существование пользователя по ID перед удалением
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
