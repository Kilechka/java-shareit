package ru.yandex.practicum.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BookingDtoIn {
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Long itemId;
    private User booker;
    private Status status;
}