package ru.yandex.practicum.item;

import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.user.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Item createItem(Item item, User user);

    Item updateItem(Item oldItem);

    Item getItem(Long itemId);

    Collection<Item> getAllUsersItems(User user);

    Collection<Item> findItemForBooking(String text);

    Item findByIdAndOwner(Long itemId, User user);

    Comment addComment(Comment comment);

    Collection<Comment> getCommentsByItemId(Long itemId);

    Optional<Booking> wasInOwn(Long userId, Long itemId);
}