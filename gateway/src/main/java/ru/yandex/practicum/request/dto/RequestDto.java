package ru.yandex.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestDto {
    @NotBlank(message = "Поле 'description' не может быть пустым")
    private String description;
}