package ru.yandex.practicum.booking;

import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(BookingDtoIn bookingDtoIn, Long userId);

    BookingDto confirmBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsOfUser(Long userId, String state, int from, int size);

    Collection<BookingDto> getBookingForOwner(Long userId, String state, int from, int size);
}