package item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.item.dto.CommentInDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentDtoTests {
    private final JacksonTester<CommentInDto> json;

    @Test
    void testSerialize() throws IOException {
        CommentInDto comment = CommentInDto.builder()
                .text("text")
                .build();

        JsonContent<CommentInDto> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(text -> assertThat(text).isEqualTo(comment.getText()));
    }
}