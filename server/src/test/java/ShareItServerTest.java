import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.booking.BookingController;
import ru.yandex.practicum.item.ItemController;
import ru.yandex.practicum.request.RequestController;
import ru.yandex.practicum.user.UserController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ShareItServer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShareItServerTest {

    @Test
    void contextLoads() {
    }
}