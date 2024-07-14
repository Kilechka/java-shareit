import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItApplication;
import ru.yandex.practicum.booking.BookingService;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;
import ru.yandex.practicum.item.ItemService;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItApplication.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
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
        Collection<BookingDto> bookings = bookingService.getBookingsOfUser("ALL", user1.getId());
        BookingDto retrievedBooking = bookingService.getBookingById(booking.getId(), user1.getId());

        assertNotNull(bookings);
        assertTrue(bookings.contains(retrievedBooking));
    }

    @Test
    void shouldGetBookingForOwnerTest() {
        Collection<BookingDto> bookings = bookingService.getBookingForOwner(user.getId(), "ALL", 0, 10);
        BookingDto retrievedBooking = bookingService.getBookingById(booking.getId(), user.getId());

        assertNotNull(bookings);
        assertTrue(bookings.contains(retrievedBooking));
    }
}