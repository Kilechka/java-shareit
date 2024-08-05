package ru.yandex.practicum.exceptions;

public class BookingException extends RuntimeException {

    public BookingException(final String message) {
        super(message);
    }
}