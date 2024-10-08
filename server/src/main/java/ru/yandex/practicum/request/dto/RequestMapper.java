package ru.yandex.practicum.request.dto;

import ru.yandex.practicum.request.Request;
import ru.yandex.practicum.user.User;

import java.util.ArrayList;

public class RequestMapper {

    private RequestMapper() {
    }

    public static RequestDto toRequestDto(Request itemRequest) {
        return RequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
    }

    public static Request toRequest(RequestDto itemRequestDto, User user) {
        return Request.builder()
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .build();
    }

    public static RequestDtoOut toRequestOut(Request itemRequest) {
        return RequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .items(new ArrayList<>())
                .created(itemRequest.getCreated())
                .build();
    }
}