package booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.booking.BookingServiceImpl;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.item.ItemController;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.user.UserController;
import ru.yandex.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {

    private final UserController userService;
    private final ItemController itemService;
    private final BookingServiceImpl bookingService;
    private UserDto user;
    private UserDto user1;
    private ItemDto item;
    private BookingDto booking;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        user = userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .name("John Doe1")
                .email("doe@example1.com")
                .build();

        user1 = userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .build();

        item = itemService.createItem(itemDto, user.getId());


        BookingDtoIn bookingDto = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        bookingDto.setItemId(item.getId());

        booking = bookingService.createBooking(bookingDto, user1.getId());
    }

    @Test
    void shouldCreateBookingTest() {
        BookingDtoIn newBookingDto = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4))
                .build();

        newBookingDto.setItemId(item.getId());

        BookingDto newCreatedBooking = bookingService.createBooking(newBookingDto, user1.getId());

        assertNotNull(booking.getId());
        assertEquals(user1.getId(), booking.getBooker().getId());
        assertEquals(newBookingDto.getStart(), newCreatedBooking.getStart());
        assertEquals(newBookingDto.getEnd(), newCreatedBooking.getEnd());
        assertEquals(newBookingDto.getItemId(), newCreatedBooking.getItem().getId());
        assertEquals(newCreatedBooking.getId(), 2);
    }

    @Test
    void shouldConfirmBookingTest() {
        bookingService.confirmBooking(user.getId(), booking.getId(), true);
        BookingDto confirmedBooking = bookingService.getBookingById(booking.getId(), user.getId());
        assertEquals(Status.APPROVED, confirmedBooking.getStatus());
    }

    @Test
    void shouldGetBookingsOfUserTest() {
        List<BookingDto> bookings = (List<BookingDto>) bookingService.getBookingsOfUser(user1.getId(), "ALL", 0, 10);
        BookingDto retrievedBooking = bookingService.getBookingById(booking.getId(), user1.getId());

        assertNotNull(bookings);
        assertEquals(bookings.get(0).getId(), retrievedBooking.getId());
    }

    @Test
    void shouldGetBookingForOwnerTest() {
        List<BookingDto> bookings = (List<BookingDto>) bookingService.getBookingForOwner(user.getId(), "ALL", 0, 10);
        BookingDto retrievedBooking = bookingService.getBookingById(booking.getId(), user.getId());

        assertNotNull(bookings);
        assertEquals(bookings.get(0).getId(), retrievedBooking.getId());
    }

    @Test
    void shouldThrowExceptionForOverlappingBookingTest() {
        BookingDtoIn overlappingBookingDto1 = BookingDtoIn.builder()
                .start(booking.getStart().plusHours(1))
                .end(booking.getEnd().minusHours(1))
                .build();
        overlappingBookingDto1.setItemId(item.getId());

        BookingDtoIn overlappingBookingDto2 = BookingDtoIn.builder()
                .start(booking.getStart().minusHours(1))
                .end(booking.getEnd().plusHours(1))
                .build();
        overlappingBookingDto2.setItemId(item.getId());

        BookingDtoIn overlappingBookingDto3 = BookingDtoIn.builder()
                .start(booking.getStart().minusHours(1))
                .end(booking.getEnd().minusHours(1))
                .build();
        overlappingBookingDto3.setItemId(item.getId());

        BookingDtoIn overlappingBookingDto4 = BookingDtoIn.builder()
                .start(booking.getStart().plusHours(1))
                .end(booking.getEnd().plusHours(1))
                .build();
        overlappingBookingDto4.setItemId(item.getId());


        assertThrows(BookingException.class, () -> {
            bookingService.createBooking(overlappingBookingDto1, user1.getId());
        });

        assertThrows(BookingException.class, () -> {
            bookingService.createBooking(overlappingBookingDto2, user1.getId());
        });

        assertThrows(BookingException.class, () -> {
            bookingService.createBooking(overlappingBookingDto3, user1.getId());
        });

        assertThrows(BookingException.class, () -> {
            bookingService.createBooking(overlappingBookingDto4, user1.getId());
        });
    }
}