package ru.yandex.practicum.item.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Поле 'name' не может быть пустым")
    private String name;
    @NotBlank(message = "Поле 'description' не может быть пустым")
    private String description;
    @NotNull(message = "Поле 'available' не может быть пустым")
    private Boolean available;
    @JsonIgnore
    private Long owner;
    @JsonIgnore
    private Long requestId;
}