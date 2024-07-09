package ru.yandex.practicum.item;

import ru.yandex.practicum.user.User;

import java.util.Collection;

public interface ItemStorage {

    public Item createItem(Item item, User user);

    public Item updateItem(Item oldItem);

    public Item getItem(Long itemId);

    public Collection<Item> getAllUsersItems(User user);

    public Collection<Item> findItemForBooking(String text);
}