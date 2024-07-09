package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {

    private HashMap<Long, Item> allItems = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item createItem(Item item, User user) {
        item.setId(makeId());
        user.getItems().put(item.getId(), item);

        allItems.put(item.getId(), item);

        return item;
    }

    @Override
    public Item updateItem(Item item) {
        allItems.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItem(Long itemId) {
        isItemExist(itemId);
        return allItems.get(itemId);
    }

    @Override
    public Collection<Item> getAllUsersItems(User user) {
        return new ArrayList<>(user.getItems().values());
    }

    @Override
    public Collection<Item> findItemForBooking(String text) {
        List<Item> foundItems = allItems.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable() == true)
                .collect(Collectors.toList());

        return foundItems;
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