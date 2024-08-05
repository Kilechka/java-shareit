package ru.yandex.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Поле 'name' не может быть пустым")
    private String name;
    @Email(message = "Указан неверный формат email")
    @NotBlank(message = "Поле 'email' не может быть пустым")
    private String email;
}