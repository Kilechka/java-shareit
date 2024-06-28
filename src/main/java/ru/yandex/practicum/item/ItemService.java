package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.item.dto.ItemDto;

import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        return itemStorage.createItem(itemDto, userId);
    }

    public ItemDto updateItem(Map<String, Object> updates, Long itemId, Long userId) {
        return itemStorage.updateItem(updates, itemId, userId);
    }


    public ItemDto getItem(Long itemId) {
        return itemStorage.getItem(itemId);
    }

    public Collection<ItemDto> getAllUsersItems(Long userId) {
        return itemStorage.getAllUsersItems(userId);
    }


    public Collection<ItemDto> findItemForBooking(String text) {
        return itemStorage.findItemForBooking(text);
    }
}