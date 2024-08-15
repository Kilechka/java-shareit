package ru.yandex.practicum.request;

import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestDtoOut;

import java.util.Collection;

public interface RequestService {

    RequestDto createRequest(Long userId, RequestDto itemRequestDto);

    Collection<RequestDtoOut> getUserRequests(Long userId);

    Collection<RequestDto> getRequests(Long userId, int from, int size);

    RequestDtoOut getRequest(Long userId, Long requestId);
}