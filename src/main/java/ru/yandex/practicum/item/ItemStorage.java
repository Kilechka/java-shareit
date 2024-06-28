package ru.yandex.practicum.item;

import ru.yandex.practicum.item.dto.ItemDto;

import java.util.Collection;
import java.util.Map;

public interface ItemStorage {

    public ItemDto createItem(ItemDto itemDto, Long userId);

    public ItemDto updateItem(Map<String, Object> updates, Long itemId, Long userId);

    public ItemDto getItem(Long itemId);

    public Collection<ItemDto> getAllUsersItems(Long userId);

    public Collection<ItemDto> findItemForBooking(String text);
}