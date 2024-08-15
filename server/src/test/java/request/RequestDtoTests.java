package request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoTests {

    private final JacksonTester<RequestDto> json;

    @Test
    void testSerialize() throws IOException {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("John Doe");
        requestor.setEmail("doe@example.com");

        RequestDto requestDto = RequestDto.builder()
                .id(1L)
                .description("Test description")
                .requestor(requestor)
                .created(LocalDateTime.of(2023, 10, 1, 12, 0, 0))
                .build();

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(requestDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(requestDto.getDescription()));
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(requestDto.getRequestor().getId()));
        assertThat(result).extractingJsonPathStringValue("$.requestor.name")
                .satisfies(name -> assertThat(name).isEqualTo(requestDto.getRequestor().getName()));
        assertThat(result).extractingJsonPathStringValue("$.requestor.email")
                .satisfies(email -> assertThat(email).isEqualTo(requestDto.getRequestor().getEmail()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isEqualTo("2023-10-01T12:00:00"));
    }
}