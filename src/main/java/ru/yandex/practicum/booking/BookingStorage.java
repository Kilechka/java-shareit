package ru.yandex.practicum.booking;

import ru.yandex.practicum.user.User;

import java.util.Collection;

public interface BookingStorage {

    Booking createBooking(Booking booking, User user);

    Booking confirmBooking(Booking booking, Boolean approved);

    Booking getBookingById(Long bookingId, Long userId);

    Collection<Booking> getBookingsOfUser(String state, Long userId);

    Booking getBookingByIdForOwner(Long bookingId, Long userId);

    Collection<Booking> getBookingForOwner(Long userId, String state, int from, int size);

    Booking getLastBookingForItem(Long itemId);

    public Booking getNextBookingForItem(Long itemId);
}