import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItGateway;
import ru.yandex.practicum.request.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShareItGateway.class})
public class RequestDtoTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateRequestDtoEmailFail() {
        RequestDto requestDto = RequestDto.builder()
                .description("").build();

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);
        List<ConstraintViolation<RequestDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("description", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'description' не может быть пустым", violationList.get(0).getMessage());
    }
}