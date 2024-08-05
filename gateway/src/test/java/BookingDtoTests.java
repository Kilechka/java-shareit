import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItGateway;
import ru.yandex.practicum.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShareItGateway.class})

public class BookingDtoTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateBookingDtoStartFail() {
        BookItemRequestDto itemRequestDto = new BookItemRequestDto();
        itemRequestDto.setItemId(1L);
        itemRequestDto.setEnd(LocalDateTime.of(2025, 10, 1, 12, 0, 0));

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(itemRequestDto);
        List<ConstraintViolation<BookItemRequestDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("start", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'start' не может быть пустым", violationList.get(0).getMessage());
    }

    @Test
    void validateBookingDtoEndFail() {
        BookItemRequestDto itemRequestDto = new BookItemRequestDto();
        itemRequestDto.setItemId(1L);
        itemRequestDto.setStart(LocalDateTime.of(2025, 10, 1, 12, 0, 0));

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(itemRequestDto);
        List<ConstraintViolation<BookItemRequestDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("end", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'end' не может быть пустым", violationList.get(0).getMessage());
    }
}