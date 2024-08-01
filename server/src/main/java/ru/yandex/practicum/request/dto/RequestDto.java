package ru.yandex.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}