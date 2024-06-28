package ru.yandex.practicum.request;

import lombok.Data;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}