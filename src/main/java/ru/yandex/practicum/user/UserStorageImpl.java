package ru.yandex.practicum.user;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserStorageImpl implements UserStorage {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        log.info("Создаем пользователя");

        User newUser = userRepository.save(user);
        log.info("Создали" + newUser);

        return newUser;
    }

    @Override
    @Transactional
    public Collection<User> getAllUsers() {
        log.info("Получаем пользователей");
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UserUpdateDto userDto) {
        log.info("Обновляем пользователя");
        isUserExist(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        User oldUser = optionalUser.get();
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
            log.info("Присвоили поле name");
        }
        if (userDto.getEmail() != null) {
            String newEmail = userDto.getEmail();
          //  isUserWithEmailExist(userId, newEmail);
            oldUser.setEmail(newEmail);
            log.info("Присвоили поле email");
        }
        return oldUser;
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаляем пользователя");
        isUserExist(id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User getUser(Long userId) {
        log.info("Получаем пользователя");
        isUserExist(userId);
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    @Transactional
    private void isUserExist(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("Пользователь с данным id не найден");
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }
}