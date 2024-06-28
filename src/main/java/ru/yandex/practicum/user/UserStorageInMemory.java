package ru.yandex.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.user.dto.UserMapper.toUserDto;

@Repository
@Slf4j
public class UserStorageInMemory implements UserStorage {

    HashMap<Long, User> users = new HashMap<>();
    Long id = 0L;

    public UserDto createUser(User user) {
        log.info("Создаем пользователя");
        isUserWithEmailExist(user.getId(), user.getEmail());
        id = makeId();
        user.setId(id);
        users.put(id, user);
        log.info("Создали" + user);

        return toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        log.info("Получаем пользователей");
        Collection<UserDto> usersDto = users.values().stream()
                .map(user -> toUserDto((User) user))
                .collect(Collectors.toList());
        return usersDto;
    }

    @Override
    public UserDto updateUser(Long userId, Map<String, Object> updates) {
        log.info("Обновляем пользователя");
        isUserExist(userId);
        User oldUser = users.get(userId);
        if (updates.containsKey("name")) {
            oldUser.setName((String) updates.get("name"));
            log.info("Присвоили поле name");
        }
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            isUserWithEmailExist(userId, newEmail);
            oldUser.setEmail(newEmail);
            log.info("Присвоили поле email");
        }
        return toUserDto(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Удаляем пользователя");
        isUserExist(id);
        users.remove(id);
    }

    public User getUser(Long userId) {
        log.info("Получаем пользователя");
        isUserExist(userId);
        return users.get(userId);
    }

    private Long makeId() {
        log.info("Создаем айди");
        return ++id;
    }

    private void isUserExist(Long userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователь с данным id не найден");
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    private void isUserWithEmailExist(Long userId, String email) {
        Optional<User> userWithEmail = users.values().stream()
                .filter(u -> u.getId() != userId && u.getEmail().equals(email))
                .findFirst();
        if (userWithEmail.isPresent()) {
            log.warn("Пользователь с таким email уже существует");
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
    }
}