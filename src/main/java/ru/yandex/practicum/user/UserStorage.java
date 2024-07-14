package ru.yandex.practicum.user;

import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    Collection<User> getAllUsers();

    User updateUser(Long userId, UserUpdateDto userDto);

    void deleteUserById(Long id);

    User getUser(Long userId);
}