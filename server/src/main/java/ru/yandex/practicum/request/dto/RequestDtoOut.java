package ru.yandex.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.item.dto.ItemRequestDto;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestDtoOut {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemRequestDto> items;

    public RequestDtoOut setItemsInfo(List<ItemRequestDto> itemsInfo) {
        this.items = itemsInfo;
        return this;
    }
}