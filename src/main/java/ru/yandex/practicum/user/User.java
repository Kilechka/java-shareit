package ru.yandex.practicum.user;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.item.Item;

import java.util.HashMap;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    @Builder.Default
    private HashMap<Long, Item> items = new HashMap<>();
}