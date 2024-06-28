package ru.yandex.practicum.user;

import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    public UserDto createUser(User user);

    public Collection<UserDto> getAllUsers();

    public UserDto updateUser(Long userId, Map<String, Object> updates);

    public void deleteUserById(Long id);

    public User getUser(Long userId);
}