package ru.yandex.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserMapper;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.user.dto.UserMapper.toUser;
import static ru.yandex.practicum.user.dto.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("В сервисе создаем пользователя");
        User user = toUser(userDto);
        return toUserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        log.info("В сервисе получаем пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long userId, UserUpdateDto userDto) {
        log.info("В сервисе обновляем пользователя");
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
            log.info("Присвоили поле name");
        }
        if (userDto.getEmail() != null) {
            String newEmail = userDto.getEmail();
            oldUser.setEmail(newEmail);
            log.info("Присвоили поле email");
        }
        return toUserDto(userRepository.save(oldUser));
    }

    public void deleteUserById(Long id) {
        log.info("В сервисе удаляем пользователя");
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с данным id не найден");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        log.info("В сервисе получаем пользователя");
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return toUserDto(user);
    }
}