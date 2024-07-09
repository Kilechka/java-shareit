package ru.yandex.practicum.item;

import lombok.Data;
import ru.yandex.practicum.request.ItemRequest;
import ru.yandex.practicum.user.User;

@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
}