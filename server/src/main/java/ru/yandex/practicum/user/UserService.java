package ru.yandex.practicum.user;

import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService  {

    UserDto createUser(UserDto userDto);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserUpdateDto userDto);

    void deleteUserById(Long id);

    UserDto getUser(Long userId);
}