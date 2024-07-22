package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;
import ru.yandex.practicum.booking.dto.BookingMapper;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.booking.dto.BookingMapper.toBooking;
import static ru.yandex.practicum.booking.dto.BookingMapper.toBookingDto;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDto createBooking(BookingDtoIn bookingDtoIn, Long userId) {
        log.info("Создаем бронирование для пользователя с ID {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        bookingDtoIn.setStatus(Status.WAITING);
        Booking booking = toBooking(bookingDtoIn, user, item);

        bookingValidation(user, item, booking);

        return toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto confirmBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Подтверждаем бронирование с ID {} для пользователя с ID {}", bookingId, userId);
        Booking booking = getBookingByIdForOwner(bookingId, userId);
        if (booking.getStatus() == Status.APPROVED) {
            throw new BookingException("Статус уже был изменён на 'подтверждён'");
        } else if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingException("Нет прав на изменение статуса");
        } else {
            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
            return toBookingDto(bookingRepository.save(booking));
        }
    }

    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId, Long userId) {
        log.info("Получаем бронирование с ID {} для пользователя с ID {}", bookingId, userId);
        Booking booking = bookingRepository.findByIdAndBookerIdOrIdAndItemOwnerId(bookingId, userId, userId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        return toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    public Collection<BookingDto> getBookingsOfUser(String state, Long userId) {
        log.info("Получаем бронирования для пользователя с ID {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return switch (state) {
            case "ALL" -> bookingRepository.findAllBookingsByUser(userId).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "WAITING" -> bookingRepository.findBookingsByUserAndStatus(userId, Status.WAITING).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "REJECTED" -> bookingRepository.findBookingsByUserAndStatus(userId, Status.REJECTED).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "CURRENT" -> bookingRepository.findCurrentBookingsByUser(userId, LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "PAST" -> bookingRepository.findPastBookingsByUser(userId, LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "FUTURE" -> bookingRepository.findFutureBookingsByUser(userId, LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            default -> throw new BookingException("Unknown state: " + state);
        };
    }

    @Transactional(readOnly = true)
    public Collection<BookingDto> getBookingForOwner(Long userId, String state, int from, int size) {
        log.info("Получаем бронирования для владельца с ID {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);

        return switch (state) {
            case "ALL" -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable).getContent().stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "WAITING" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable).getContent().stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            case "REJECTED" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable).getContent().stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            case "CURRENT" ->
                    bookingRepository.findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), pageable).getContent().stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            case "PAST" ->
                    bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable).getContent().stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            case "FUTURE" ->
                    bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable).getContent().stream()
                            .map(BookingMapper::toBookingDto)
                            .collect(Collectors.toList());
            default -> throw new BookingException("Unknown state: " + state);
        };
    }

    private Booking getBookingByIdForOwner(Long bookingId, Long userId) {
        log.info("Получаем бронирование с ID {} для владельца с ID {}", bookingId, userId);
        return bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено, либо вам не принадлежит"));
    }

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

        List<Booking> overlappingBookings = bookingRepository.findByItemIdAndStartOrEndBetween(item.getId(), booking.getStart(), booking.getEnd());
        List<Booking> overlappingEntireBooking = bookingRepository.findByItemIdAndStartBeforeAndEndAfter(item.getId(), booking.getEnd(), booking.getStart());

        if (!overlappingBookings.isEmpty() || !overlappingEntireBooking.isEmpty()) {
            throw new BookingException("Выбранное время бронирования пересекается с уже существующими бронированиями");
        }
    }
}