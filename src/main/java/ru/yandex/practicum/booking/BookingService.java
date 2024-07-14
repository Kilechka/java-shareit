package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemStorage;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.booking.dto.BookingMapper.toBooking;
import static ru.yandex.practicum.booking.dto.BookingMapper.toBookingDto;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public BookingDto createBooking(BookingDtoIn bookingDtoIn, Long userId) {
        log.info("Создаем бронирование для пользователя с ID {}", userId);
        User user = userStorage.getUser(userId);
        Item item = itemStorage.getItem(bookingDtoIn.getItemId());

        bookingDtoIn.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDtoIn, user, item);

        bookingValidation(user, item, booking);

        return toBookingDto(bookingStorage.createBooking(booking, user));
    }

    public BookingDto confirmBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Подтверждаем бронирование с ID {} для пользователя с ID {}", bookingId, userId);
        Booking booking = getBookingByIdForOwner(bookingId, userId);
        if (booking.getStatus() == Status.APPROVED) {
            throw new BookingException("Статус уже был изменён на 'подтверждён'");
        } else if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingException("Нет прав на изменение статуса");
        } else {
            return toBookingDto(bookingStorage.confirmBooking(booking, approved));
        }
    }

    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId, Long userId) {
        log.info("Получаем бронирование с ID {} для пользователя с ID {}", bookingId, userId);
        return toBookingDto(bookingStorage.getBookingById(bookingId, userId));
    }

    @Transactional(readOnly = true)
    public Collection<BookingDto> getBookingsOfUser(String state, Long userId) {
        log.info("Получаем бронирования для пользователя с ID {}", userId);
        User user = userStorage.getUser(userId);
        Collection<BookingDto> bookingDto = bookingStorage.getBookingsOfUser(state, userId).stream()
                .map(booking -> toBookingDto(booking))
                .collect(Collectors.toList());
        return bookingDto;
    }

    @Transactional(readOnly = true)
    public Collection<BookingDto> getBookingForOwner(Long userId, String state, int from, int size) {
        log.info("Получаем бронирования для владельца с ID {}", userId);
        User user = userStorage.getUser(userId);
        Collection<BookingDto> bookingDto = bookingStorage.getBookingForOwner(userId, state, from, size).stream()
                .map(booking -> toBookingDto(booking))
                .collect(Collectors.toList());
        return bookingDto;
    }

    @Transactional(readOnly = true)
    private Booking getBookingByIdForOwner(Long bookingId, Long userId) {
        log.info("Получаем бронирование с ID {} для владельца с ID {}", bookingId, userId);
        Booking booking = bookingStorage.getBookingByIdForOwner(bookingId, userId);
        return booking;
    }

    @Transactional(readOnly = true)
    private void bookingValidation(User user, Item item, Booking booking) {
        log.info("Валидация бронирования для пользователя с ID {}", user.getId());
        if (!item.getAvailable()) {
            throw new BookingException("Данная вещь недоступна для бронирования");
        } else if (item.getOwner() == user) {
            throw new NotFoundException("Вы не можете забронировать свою же вещь");
        } else if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingException("Дата начала бронирования не может быть в прошлом");
        } else if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingException("Дата конца бронирования не может быть в прошлом");
        } else if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingException("Дата конца бронирования не может быть раньше, чем дата начала бронирования");
        } else if (booking.getEnd().isEqual(booking.getStart())) {
            throw new BookingException("Дата конца бронирования не может равняться дате начала бронирования");
        }
    }
}