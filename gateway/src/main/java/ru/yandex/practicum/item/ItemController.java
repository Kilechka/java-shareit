package ru.yandex.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.item.dto.CommentInDto;
import ru.yandex.practicum.item.dto.ItemRequestDto;
import ru.yandex.practicum.item.dto.ItemUpdateDto;

import java.util.Collections;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemRequestDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemUpdateDto itemDto, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllUsersItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemForBooking(@RequestParam("text") String text) {
        if (text == null || text.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.findItemForBooking(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody @Valid CommentInDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}