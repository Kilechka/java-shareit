package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.dto.ItemUpdateDto;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import static ru.yandex.practicum.item.dto.ItemMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userStorage.getUser(userId);
        Item item = toItem(itemDto, user);
        return toItemDto(itemStorage.createItem(item, user));
    }

    public ItemDto updateItem(ItemUpdateDto itemDto, Long itemId, Long userId) {
        User user = userStorage.getUser(userId);
        isItemExist(itemId, user);
        Item oldItem = user.getItems().get(itemId);

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }

        return toItemDto(oldItem);
    }

    public ItemDto getItem(Long itemId) {
        return toItemDto(itemStorage.getItem(itemId));
    }

    public Collection<ItemDto> getAllUsersItems(Long userId) {
        User user = userStorage.getUser(userId);
        return itemStorage.getAllUsersItems(user).stream().map(i -> toItemDto(i)).collect(Collectors.toList());
    }

    public Collection<ItemDto> findItemForBooking(String text) {
        return itemStorage.findItemForBooking(text).stream().map(i -> toItemDto(i)).collect(Collectors.toList());
    }

    private void isItemExist(Long itemId, User user) {
        HashMap<Long, Item> items = user.getItems();
        if (!items.containsKey(itemId)) {
            log.warn("Вещь не найдена");
            throw new NotFoundException("Вещь не найдена");
        }
    }
}