package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.booking.dto.BookingDtoOut;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.item.dto.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.item.dto.CommentMapper.toComment;
import static ru.yandex.practicum.item.dto.CommentMapper.toCommentDto;
import static ru.yandex.practicum.item.dto.ItemMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = toItem(itemDto, user);
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemDto, Long itemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item oldItem = itemRepository.findByIdAndOwner(itemId, user);
        if (oldItem == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        return toItemDto(itemRepository.save(oldItem));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        ItemDto itemDto = toItemDto(item);

        List<CommentOutDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(comments);

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            Booking nextBooking = bookingRepository.findFirstBookingByItemIdAndStartIsAfterAndStatusNotOrderByStartAsc(itemId, now, Status.REJECTED).orElse(null);
            Booking lastBooking = bookingRepository.findFirstBookingByItemIdAndStartIsBeforeAndStatusNotOrderByStartDesc(itemId, now, Status.REJECTED).orElse(null);

            if (lastBooking != null) {
                itemDto.setLastBooking(new BookingDtoOut(lastBooking.getId(), lastBooking.getBooker().getId()));
            }
            if (nextBooking != null) {
                itemDto.setNextBooking(new BookingDtoOut(nextBooking.getId(), nextBooking.getBooker().getId()));
            }
        }
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> getAllUsersItems(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Item> items = (List<Item>) itemRepository.findByOwnerId(userId);
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findByItemIdInAndStatusNot(itemIds, Status.REJECTED);
        Map<Long, List<Booking>> bookingsByItemId = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        List<Comment> comments = commentRepository.findByItemIdIn(itemIds);
        Map<Long, List<Comment>> commentsByItemId = comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));

        return items.stream()
                .map(item -> {
                    ItemDto itemDto = toItemDto(item);
                    LocalDateTime now = LocalDateTime.now();

                    List<Booking> itemBookings = bookingsByItemId.getOrDefault(item.getId(), Collections.emptyList());
                    Booking lastBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isBefore(now))
                            .max(Comparator.comparing(Booking::getStart))
                            .orElse(null);
                    Booking nextBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isAfter(now))
                            .min(Comparator.comparing(Booking::getStart))
                            .orElse(null);

                    if (lastBooking != null) {
                        itemDto.setLastBooking(new BookingDtoOut(lastBooking.getId(), lastBooking.getBooker().getId()));
                    }
                    if (nextBooking != null) {
                        itemDto.setNextBooking(new BookingDtoOut(nextBooking.getId(), nextBooking.getBooker().getId()));
                    }

                    List<CommentOutDto> itemComments = commentsByItemId.getOrDefault(item.getId(), Collections.emptyList()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());
                    if (!itemComments.isEmpty()) {
                        itemDto.setComments(itemComments);
                    }

                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<ItemDto> findItemForBooking(String text) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public CommentOutDto addComment(Long userId, Long itemId, CommentInDto commentDto) {
        Optional<Booking> booking = bookingRepository.findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(userId, itemId, LocalDateTime.now());
        if (!booking.isPresent()) {
            throw new BookingException("Вы не брали в аренду данную вещь, либо бронирование ещё не завершилось");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        Comment comment = toComment(commentDto, item, user);
        return toCommentDto(commentRepository.save(comment));
    }
}