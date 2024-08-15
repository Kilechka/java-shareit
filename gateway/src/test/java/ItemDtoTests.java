import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItGateway;
import ru.yandex.practicum.item.dto.ItemRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShareItGateway.class})
public class ItemDtoTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateItemDtoNameFail() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .name("")
                .description("description")
                .available(true)
                .build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        List<ConstraintViolation<ItemRequestDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("name", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'name' не может быть пустым", violationList.get(0).getMessage());
    }

    @Test
    void validateItemDtoAvailableFail() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .name("name")
                .description("description")
                .build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        List<ConstraintViolation<ItemRequestDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("available", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'available' не может быть пустым", violationList.get(0).getMessage());
    }
}