package user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoTests {

    private final JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws IOException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("doe@example.com");
        userDto.setName("John Doe");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(userDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.email")
                .satisfies(email -> assertThat(email).isEqualTo(userDto.getEmail()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(userDto.getName()));
    }
}