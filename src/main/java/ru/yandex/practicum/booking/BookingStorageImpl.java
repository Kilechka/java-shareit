package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingStorageImpl implements BookingStorage {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking, User user) {
        log.info("Создаем бронирование");
        Booking newBooking = bookingRepository.save(booking);
        return newBooking;
    }

    @Override
    public Booking confirmBooking(Booking booking, Boolean approved) {
        log.info("Меняем статус");
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(Long bookingId, Long userId) {
        log.info("Получаем бронирование по айди");
        Optional<Booking> booking = bookingRepository.findByIdAndBookerIdOrIdAndItemOwnerId(bookingId, userId, userId);
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingByIdForOwner(Long bookingId, Long userId) {
        log.info("Получаем бронирование по айди для владельца");
        Optional<Booking> booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId);
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new NotFoundException("Бронирование не найдено, либо вам не пренадлежит");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> getBookingsOfUser(String status, Long userId) {
        log.info("Получаем бронирования пользователя");
        return switch (status) {
            case "ALL" -> bookingRepository.findAllBookingsByUser(userId);
            case "WAITING" -> bookingRepository.findBookingsByUserAndStatus(userId, Status.WAITING);
            case "REJECTED" -> bookingRepository.findBookingsByUserAndStatus(userId, Status.REJECTED);
            case "CURRENT" -> bookingRepository.findCurrentBookingsByUser(userId, LocalDateTime.now());
            case "PAST" -> bookingRepository.findPastBookingsByUser(userId, LocalDateTime.now());
            case "FUTURE" -> bookingRepository.findFutureBookingsByUser(userId, LocalDateTime.now());
            default -> throw new ValidationException("Unknown state: " + status);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> getBookingForOwner(Long userId, String status, int from, int size) {
        log.info("Получаем бронирования вещей пользователя");
        Pageable pageable = PageRequest.of(from / size, size);
        return switch (status) {
            case "ALL" -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable).getContent();
            case "WAITING" -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable).getContent();
            case "REJECTED" -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable).getContent();
            case "CURRENT" -> bookingRepository.findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), pageable).getContent();
            case "PAST" -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable).getContent();
            case "FUTURE" -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable).getContent();
            default -> throw new ValidationException("Unknown state: " + status);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getLastBookingForItem(Long itemId) {
        log.info("Получаем последнее бронирование вещи");
        LocalDateTime now = LocalDateTime.now().withNano(0);
        return bookingRepository.findFirstBookingByItemIdAndStartIsBeforeAndStatusNotOrderByStartDesc(itemId, now, Status.REJECTED).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getNextBookingForItem(Long itemId) {
        log.info("Получаем следующее бронирование вещи");
        LocalDateTime now = LocalDateTime.now().withNano(0);
        return bookingRepository.findFirstBookingByItemIdAndStartIsAfterAndStatusNotOrderByStartAsc(itemId, now, Status.REJECTED).orElse(null);
    }
}