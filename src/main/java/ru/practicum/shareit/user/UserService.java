package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepositoryInterface userRepository;

    @Autowired
    public UserService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long idUser) {
        return userRepository.getUserById(idUser);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User updateUser(Long idUser, User user) {
        return userRepository.updateUser(idUser, user);
    }

    public void deleteUserById(Long idUser) {
        userRepository.deleteUserById(idUser);
    }
}
