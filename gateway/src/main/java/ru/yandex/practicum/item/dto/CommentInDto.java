package ru.yandex.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentInDto {
    @NotBlank(message = "Поле 'text' не может быть пустым")
    private String text;
}