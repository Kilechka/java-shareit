package booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTests {

    private final JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws IOException {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .owner(1L)
                .build();

        User booker = new User();
        booker.setId(1L);
        booker.setName("John Doe");
        booker.setEmail("doe@example.com");

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 10, 1, 12, 0, 0))
                .end(LocalDateTime.of(2023, 10, 2, 12, 0, 0))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isEqualTo("2023-10-01T12:00:00"));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isEqualTo("2023-10-02T12:00:00"));
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getItem().getId()));
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .satisfies(name -> assertThat(name).isEqualTo(bookingDto.getItem().getName()));
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .satisfies(description -> assertThat(description).isEqualTo(bookingDto.getItem().getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .satisfies(available -> assertThat(available).isEqualTo(bookingDto.getItem().getAvailable()));
        assertThat(result).extractingJsonPathNumberValue("$.item.owner")
                .satisfies(owner -> assertThat(owner.longValue()).isEqualTo(bookingDto.getItem().getOwner()));
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getBooker().getId()));
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .satisfies(name -> assertThat(name).isEqualTo(bookingDto.getBooker().getName()));
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .satisfies(email -> assertThat(email).isEqualTo(bookingDto.getBooker().getEmail()));
        assertThat(result).extractingJsonPathStringValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(bookingDto.getStatus().name()));
    }
}