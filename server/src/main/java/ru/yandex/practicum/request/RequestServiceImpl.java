package ru.yandex.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.item.dto.ItemMapper;
import ru.yandex.practicum.item.dto.ItemRequestDto;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestDtoOut;
import ru.yandex.practicum.request.dto.RequestMapper;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.request.dto.RequestMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RequestDto createRequest(Long userId, RequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Request request = toRequest(itemRequestDto, user);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);
        log.info("Создаем запрос");
        return toRequestDto(request);
    }

    public Collection<RequestDtoOut> getUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        log.info("Получаем запросы пользователя");

        List<RequestDtoOut> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toRequestOut)
                .collect(Collectors.toList());
        List<Long> requestsIds = requests.stream().map(RequestDtoOut::getId).collect(Collectors.toList());

        List<Item> items = itemRepository.findByRequestIdIn(requestsIds);
        Map<Long, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream()
                .map(request -> {
                    List<ItemRequestDto> itemRequestDtos = itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList()).stream()
                            .map(ItemMapper::toItemRequest)
                            .collect(Collectors.toList());
                    return request.setItemsInfo(itemRequestDtos);
                })
                .collect(Collectors.toList());
    }

    public Collection<RequestDto> getRequests(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);

        return requestRepository.findAllByOrderByCreatedDesc(pageable).stream()
                .map(RequestMapper::toRequestDto).toList();
    }

    public RequestDtoOut getRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<ItemRequestDto> items = itemRepository.findByRequestId(request.getId()).stream()
                .map(ItemMapper::toItemRequest).toList();

        return toRequestOut(request).setItemsInfo(items);
    }
}