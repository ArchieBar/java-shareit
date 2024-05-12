package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        log.info("Вызов GET-операции \"getUserById\"");
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Вызов GET-операции \"getAllUsers\"");
        return userClient.getAllUsers();
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Вызов POST-операции \"createUser\"");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long userId,
                                             @RequestBody @Valid UserDto userDto) {
        log.info("Вызов PATCH-операции \"updateUser\"");
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") Long userId) {
        log.info("Вызов DELETE-операции \"deleteUserById\"");
        return userClient.deleteUserById(userId);
    }
}
