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

@Service("userServiceJpa")
public class UserServiceJpa implements UserService {
    private final UserRepositoryJpa userRepository;

    @Autowired
    public UserServiceJpa(UserRepositoryJpa userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format("Пользователь с ID: {0} не найден.", userId))));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(userRepository.findAll());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.saveAndFlush(user));
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

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
