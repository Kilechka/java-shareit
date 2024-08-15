package ru.yandex.practicum.item.dto;

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
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
    private BookingDtoOut lastBooking;
    private BookingDtoOut nextBooking;
    private List<CommentOutDto> comments;
}