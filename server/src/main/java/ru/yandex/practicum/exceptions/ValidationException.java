package ru.yandex.practicum.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(final String message) {
        super(message);
    }
}