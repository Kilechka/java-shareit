package exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;


import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(classes = ShareItServer.class)
@AutoConfigureTestDatabase
public class ExceptionTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldNotSaveDublicateUser() {
        User user = new User(1L, "Lil", "example@yandex.ru");
        userRepository.save(user);
        User otherUser = new User(2L, "Lil", "example@yandex.ru");
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(otherUser));
    }
}