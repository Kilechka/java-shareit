package ru.yandex.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(userDto);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получен запрос на получение пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UserUpdateDto userDto) {
        log.info("Получен запрос на обновление пользователя");
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(value = "userId") Long userId) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable(value = "userId") Long userId) {
        log.info("Получен запрос на получение пользователя");
        return userService.getUser(userId);
    }
}