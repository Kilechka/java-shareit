package ru.yandex.practicum.booking.dto;

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
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private User booker;
    private Status status;
}