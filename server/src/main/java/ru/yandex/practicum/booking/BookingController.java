package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;

import java.util.Collection;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDtoIn bookingDtoIn, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на создание бронирования для пользователя с ID {}", userId);
        return bookingService.createBooking(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос на подтверждение бронирования с ID {} для пользователя с ID {}", bookingId, userId);
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение бронирования с ID {} для пользователя с ID {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос на получение бронирований для пользователя с ID {}", userId);
        return bookingService.getBookingsOfUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос на получение бронирований для владельца с ID {}", userId);
        return bookingService.getBookingForOwner(userId, state, from, size);
    }
}