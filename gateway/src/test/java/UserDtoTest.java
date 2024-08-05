import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItGateway;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShareItGateway.class})
class UserDtoTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateUserDtoEmailFail() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalidEmail");
        userDto.setName("Test User");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        List<ConstraintViolation<UserDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("email", violationList.get(0).getPropertyPath().toString());
        assertEquals("Указан неверный формат email", violationList.get(0).getMessage());
    }

    @Test
    void validateUserDtoEmailBlank() {
        UserDto userDto = new UserDto();
        userDto.setEmail("");
        userDto.setName("Test User");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        List<ConstraintViolation<UserDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("email", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'email' не может быть пустым", violationList.get(0).getMessage());
    }

    @Test
    void validateUserDtoNameBlank() {
        UserDto userDto = new UserDto();
        userDto.setEmail("email@email.com");
        userDto.setName("");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        List<ConstraintViolation<UserDto>> violationList = new ArrayList<>(violations);

        assertEquals(1, violationList.size());
        assertEquals("name", violationList.get(0).getPropertyPath().toString());
        assertEquals("Поле 'name' не может быть пустым", violationList.get(0).getMessage());
    }
}