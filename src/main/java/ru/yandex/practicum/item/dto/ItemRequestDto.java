package ru.yandex.practicum.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemRequestDto {
    private Long id;
    private String name;
    private Long ownerId;
}