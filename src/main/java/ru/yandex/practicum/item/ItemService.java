package ru.yandex.practicum.item;

import ru.yandex.practicum.item.dto.CommentInDto;
import ru.yandex.practicum.item.dto.CommentOutDto;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemUpdateDto itemDto, Long itemId, Long userId);

    ItemDto getItem(Long itemId, Long userId);

    Collection<ItemDto> getAllUsersItems(Long userId);

    Collection<ItemDto> findItemForBooking(String text);

    CommentOutDto addComment(Long userId, Long itemId, CommentInDto commentDto);
}