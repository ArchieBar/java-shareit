package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long idUser) {
        return userRepository.getUserById(idUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserDto createUser(UserDto userDto) {
        return userRepository.createUser(userDto);
    }

    public UserDto updateUser(Long idUser, UserDto userDto) {
        return userRepository.updateUser(idUser, userDto);
    }

    public void deleteUserById(Long idUser) {
        userRepository.deleteUserById(idUser);
    }
}
