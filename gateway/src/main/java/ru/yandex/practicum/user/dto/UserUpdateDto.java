package ru.yandex.practicum.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserUpdateDto {
    private Long id;
    private String name;
    @Email(message = "Указан неверный формат email")
    private String email;
}