package ru.yandex.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    @NotBlank(message = "Поле 'name' не может быть пустым")
    private String name;
    @NotBlank(message = "Поле 'description' не может быть пустым")
    private String description;
    @NotNull(message = "Поле 'available' не может быть пустым")
    private Boolean available;
    private Long requestId;
}