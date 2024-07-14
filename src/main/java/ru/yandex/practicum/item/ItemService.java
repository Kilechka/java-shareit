package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingStorage;
import ru.yandex.practicum.booking.dto.BookingDtoOut;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.dto.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.item.dto.CommentMapper.toComment;
import static ru.yandex.practicum.item.dto.CommentMapper.toCommentDto;
import static ru.yandex.practicum.item.dto.ItemMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userStorage.getUser(userId);
        Item item = toItem(itemDto, user);
        return toItemDto(itemStorage.createItem(item, user));
    }

    public ItemDto updateItem(ItemUpdateDto itemDto, Long itemId, Long userId) {
        User user = userStorage.getUser(userId);
        isItemExist(itemId, user);
        Item oldItem = itemStorage.getItem(itemId);

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        itemStorage.updateItem(oldItem);
        return toItemDto(oldItem);
    }

    @Transactional(readOnly = true)
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = itemStorage.getItem(itemId);
        ItemDto itemDto = toItemDto(item);

        List<CommentOutDto> comments = itemStorage.getCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(comments);

        if (item.getOwner().getId().equals(userId)) {
            Booking nextBooking = bookingStorage.getNextBookingForItem(itemId);
            Booking lastBooking = bookingStorage.getLastBookingForItem(itemId);

            if (lastBooking != null) {
                itemDto.setLastBooking(new BookingDtoOut(lastBooking.getId(), lastBooking.getBooker().getId()));
            }
            if (nextBooking != null) {
                itemDto.setNextBooking(new BookingDtoOut(nextBooking.getId(), nextBooking.getBooker().getId()));
            }
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    public Collection<ItemDto> getAllUsersItems(Long userId) {
        User user = userStorage.getUser(userId);
        List<ItemDto> items = itemStorage.getAllUsersItems(user).stream().map(i -> toItemDto(i)).collect(Collectors.toList());

        return items.stream()
                .map(item -> {
                    Booking lastBooking = bookingStorage.getLastBookingForItem(item.getId());
                    Booking nextBooking = bookingStorage.getNextBookingForItem(item.getId());
                    List<CommentOutDto> comments = itemStorage.getCommentsByItemId(item.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());
                    if (lastBooking != null) {
                        item.setLastBooking(new BookingDtoOut(lastBooking.getId(), lastBooking.getBooker().getId()));
                    }
                    if (nextBooking != null) {
                        item.setNextBooking(new BookingDtoOut(nextBooking.getId(), nextBooking.getBooker().getId()));
                    }
                    if (!comments.isEmpty()) {
                        item.setComments(comments);
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<ItemDto> findItemForBooking(String text) {
        return itemStorage.findItemForBooking(text).stream().map(i -> toItemDto(i)).collect(Collectors.toList());
    }

    public CommentOutDto addComment(Long userId, Long itemId, CommentInDto commentDto) {
        Optional<Booking> booking = itemStorage.wasInOwn(userId, itemId);
        if (!booking.isPresent()) {
            throw new BookingException("Вы не брали в аренду данную вещь");
        }
        User user = userStorage.getUser(userId);
        Item item = itemStorage.getItem(itemId);
        Comment comment = toComment(commentDto, item, user);
        return toCommentDto(itemStorage.addComment(comment));
    }

    @Transactional(readOnly = true)
    private void isItemExist(Long itemId, User user) {
        Item item = itemStorage.findByIdAndOwner(itemId, user);
        if (item == null) {
            log.warn("Вещь не найдена");
            throw new NotFoundException("Вещь не найдена");
        }
    }
}