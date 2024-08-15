package ru.yandex.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.user.User;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByOwnerId(Long userId);

    Collection<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Item findByIdAndOwner(Long itemId, User user);

    List<Item> findByRequestIdIn(List<Long> requestIds);

    List<Item> findByRequestId(Long requestId);
}