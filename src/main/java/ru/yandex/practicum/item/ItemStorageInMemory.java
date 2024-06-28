package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.item.dto.ItemMapper.toItem;
import static ru.yandex.practicum.item.dto.ItemMapper.toItemDto;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {

    private final UserStorage userStorage;
    private HashMap<Long, Item> allItems = new HashMap<>();
    private Long id = 0L;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userStorage.getUser(userId);
        Item item = toItem(itemDto, user);
        item.setId(makeId());

        user.getItems().put(item.getId(), item);

        allItems.put(item.getId(), item);

        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Map<String, Object> updates, Long itemId, Long userId) {
        User user = userStorage.getUser(userId);
        isItemExist(itemId, user);
        Item oldItem = user.getItems().get(itemId);

        if (updates.containsKey("name")) {
            oldItem.setName((String) updates.get("name"));
            log.info("Присвоили поле name");
        }
        if (updates.containsKey("description")) {
            oldItem.setDescription((String) updates.get("description"));
            log.info("Присвоили поле description");
        }
        if (updates.containsKey("available")) {
            oldItem.setAvailable((Boolean) updates.get("available"));
            log.info("Присвоили поле available");
        }


        allItems.remove(itemId);
        allItems.put(itemId, oldItem);

        return toItemDto(oldItem);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        isItemExist(itemId);
        return toItemDto(allItems.get(itemId));
    }

    @Override
    public Collection<ItemDto> getAllUsersItems(Long userId) {
        User user = userStorage.getUser(userId);
        return user.getItems().values().stream().map(i -> toItemDto(i)).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findItemForBooking(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> foundItems = allItems.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable() == true)
                .collect(Collectors.toList());

        return foundItems.stream()
                .map(i -> toItemDto(i))
                .collect(Collectors.toList());
    }

    private void isItemExist(Long itemId, User user) {
        HashMap<Long, Item> items = user.getItems();
        if (!items.containsKey(itemId)) {
            log.warn("Вещь не найдена");
            throw new NotFoundException("Вещь не найдена");
        }
    }

    private void isItemExist(Long itemId) {
        if (!allItems.containsKey(itemId)) {
            log.warn("Вещь с id = {} не найдена", itemId);
            throw new NotFoundException("Такая вещь не найдена");
        }

    }

    private Long makeId() {
        return ++id;
    }
}