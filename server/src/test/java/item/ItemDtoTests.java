package item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.booking.dto.BookingDtoOut;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoTests {

    private final JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws IOException {
        BookingDtoOut lastBooking = BookingDtoOut.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        BookingDtoOut nextBooking = BookingDtoOut.builder()
                .id(2L)
                .bookerId(2L)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(itemDto.getName()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemDto.getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(available -> assertThat(available).isEqualTo(itemDto.getAvailable()));
        assertThat(result).extractingJsonPathNumberValue("$.owner")
                .satisfies(owner -> assertThat(owner.longValue()).isEqualTo(itemDto.getOwner()));
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .satisfies(requestId -> assertThat(requestId.longValue()).isEqualTo(itemDto.getRequestId()));
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDto.getLastBooking().getId()));
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id");
    }
}