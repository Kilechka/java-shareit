package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.BookingStorage;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoOut;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.dto.CommentInDto;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemStorageImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Item createItem(Item item, User user) {
        log.info("Создаем вещь");
        Item newItem = itemRepository.save(item);
        log.info("Создали" + newItem);

        return newItem;
    }

    @Override
    public Item updateItem(Item item) {
        log.info("Обновляем вещь");
        itemRepository.save(item);
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItem(Long itemId) {
        log.info("Получам вещь");
        isItemExist(itemId);
        Item item = itemRepository.findById(itemId).get();
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> getAllUsersItems(User user) {
        log.info("Получаем список вещей пользователя");
        return itemRepository.findByOwnerId(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Item findByIdAndOwner(Long itemId, User user) {
        return itemRepository.findByIdAndOwner(itemId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> findItemForBooking(String text) {
        List<Item> foundItems = (List<Item>) itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
        return foundItems.stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public Comment addComment(Comment comment) {
        log.info("Создаем отзыв");
        commentRepository.save(comment);
        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> wasInOwn(Long userId, Long itemId) {
        return bookingRepository.findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(userId, itemId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Comment> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    @Transactional(readOnly = true)
    private void isItemExist(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            log.warn("Вещь с id = {} не найдена", itemId);
            throw new NotFoundException("Такая вещь не найдена");
        }
    }
}