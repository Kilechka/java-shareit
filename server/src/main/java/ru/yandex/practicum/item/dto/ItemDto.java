package ru.yandex.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.booking.dto.BookingDtoOut;

import java.util.List;

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
    private Long owner;
    private Long requestId;
    private BookingDtoOut lastBooking;
    private BookingDtoOut nextBooking;
    private List<CommentOutDto> comments;
}