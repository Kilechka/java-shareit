package ru.yandex.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.user.dto.UserMapper.toUser;
import static ru.yandex.practicum.user.dto.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto createUser(UserDto userDto) {
        log.info("В сервисе создаем пользователя");
        User user = toUser(userDto);
        return toUserDto(userStorage.createUser(user));
    }

    public Collection<UserDto> getAllUsers() {
        log.info("В сервисе получаем пользователей");
        Collection<UserDto> usersDto = userStorage.getAllUsers().stream()
                .map(user -> toUserDto((User) user))
                .collect(Collectors.toList());
        return usersDto;
    }

    public UserDto updateUser(Long userId, UserUpdateDto userDto) {
        log.info("В сервисе обновляем пользователя");
        return toUserDto(userStorage.updateUser(userId, userDto));
    }

    public void deleteUserById(Long id) {
        log.info("В сервисе удаляем пользователя");
        userStorage.deleteUserById(id);
    }

    public UserDto getUser(Long userId) {
        log.info("В сервисе получаем пользователя");
        UserDto user = toUserDto(userStorage.getUser(userId));
        return user;
    }
}