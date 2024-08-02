package ru.yandex.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestDtoOut;

import java.util.Collection;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody RequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса для пользователя с ID {}", userId);
        return requestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<RequestDtoOut> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение своих запросов для пользователя с ID {}", userId);
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос на получение всех запросов для пользователя с ID {}", userId);
        return requestService.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDtoOut getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long requestId) {
        log.info("Получен запрос на получение запроса для пользователя с ID {}", userId);
        return requestService.getRequest(userId, requestId);
    }
}