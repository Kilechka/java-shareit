package ru.yandex.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {
        log.info("Получен запрос на получение пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable (value = "userId") Long userId, @Valid @RequestBody Map<String, Object> updates) {
        log.info("Получен запрос на обновление пользователя");
        return userService.updateUser(userId, updates);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable (value = "userId") Long userId) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable (value = "userId") Long userId) {
        log.info("Получен запрос на получение пользователя");
        return userService.getUser(userId);
    }
}