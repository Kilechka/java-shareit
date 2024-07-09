package ru.yandex.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.*;

@Repository
@Slf4j
public class UserStorageInMemory implements UserStorage {

    HashMap<Long, User> users = new HashMap<>();
    Long id = 0L;

    public User createUser(User user) {
        log.info("Создаем пользователя");
        isUserWithEmailExist(user.getId(), user.getEmail());
        id = makeId();
        user.setId(id);
        users.put(id, user);
        log.info("Создали" + user);

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получаем пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(Long userId, UserUpdateDto userDto) {
        log.info("Обновляем пользователя");
        isUserExist(userId);
        User oldUser = users.get(userId);
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
            log.info("Присвоили поле name");
        }
        if (userDto.getEmail() != null) {
            String newEmail = userDto.getEmail();
            isUserWithEmailExist(userId, newEmail);
            oldUser.setEmail(newEmail);
            log.info("Присвоили поле email");
        }
        return oldUser;
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