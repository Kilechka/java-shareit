package ru.yandex.practicum.booking.dto;

import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import static ru.yandex.practicum.item.dto.ItemMapper.toItemDto;

public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(toItemDto(booking.getItem()))
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();

        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto, User user, Item item) {
        Booking booking = Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(bookingDto.getStatus())
                .build();
        return booking;
    }

    public static Booking toBooking(BookingDtoIn bookingDto, User user, Item item) {
        Booking booking = Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(bookingDto.getStatus())
                .build();
        return booking;
    }
}