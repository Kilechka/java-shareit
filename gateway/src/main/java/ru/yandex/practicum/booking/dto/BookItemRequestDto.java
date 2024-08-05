package ru.yandex.practicum.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    @NotNull(message = "Поле 'itemId' не может быть пустым")
    private long itemId;
    @FutureOrPresent
    @NotNull(message = "Поле 'start' не может быть пустым")
    private LocalDateTime start;
    @Future
    @NotNull(message = "Поле 'end' не может быть пустым")
    private LocalDateTime end;
}