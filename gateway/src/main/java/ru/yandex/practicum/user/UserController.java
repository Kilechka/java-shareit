package ru.yandex.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userClient.createUser(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен запрос на получение пользователей");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UserUpdateDto userDto) {
        log.info("Получен запрос на обновление пользователя");
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "userId") Long userId) {
        log.info("Получен запрос на удаление пользователя");
        return userClient.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable(value = "userId") Long userId) {
        log.info("Получен запрос на получение пользователя");
        return userClient.getUser(userId);
    }
}