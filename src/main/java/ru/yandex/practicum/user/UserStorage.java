package ru.yandex.practicum.user;

import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserStorage {

    public User createUser(User user);

    public Collection<User> getAllUsers();

    public User updateUser(Long userId, UserUpdateDto userDto);

    public void deleteUserById(Long id);

    public User getUser(Long userId);
}