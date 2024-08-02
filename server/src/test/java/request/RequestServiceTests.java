package request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.request.RequestController;
import ru.yandex.practicum.request.RequestServiceImpl;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestDtoOut;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTests {

    private final UserService userService;
    private final RequestServiceImpl RequestService;
    private UserDto user;
    private RequestDto request;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        user = userService.createUser(userDto);

        RequestDto requestDto = RequestDto.builder()
                .description("Test request description")
                .build();

        request = RequestService.createRequest(user.getId(), requestDto);
    }

    @Test
    void shouldCreateRequestTest() {
        RequestDto newRequestDto = RequestDto.builder()
                .description("New test request description")
                .build();

        RequestDto newCreatedRequest = RequestService.createRequest(user.getId(), newRequestDto);

        assertNotNull(newCreatedRequest.getId());
        assertEquals(newRequestDto.getDescription(), newCreatedRequest.getDescription());
        assertEquals(user.getId(), newCreatedRequest.getRequestor().getId());
    }

    @Test
    void shouldGetUserRequestsTest() {
        Collection<RequestDtoOut> userRequests = RequestService.getUserRequests(user.getId());

        assertNotNull(userRequests);
        assertFalse(userRequests.isEmpty());
        assertTrue(userRequests.stream().anyMatch(r -> r.getId().equals(request.getId())));
    }

    @Test
    void shouldGetAllRequestsTest() {
        Collection<RequestDto> allRequests = RequestService.getRequests(user.getId(), 0, 10);

        assertNotNull(allRequests);
        assertFalse(allRequests.isEmpty());
        assertTrue(allRequests.stream().anyMatch(r -> r.getId().equals(request.getId())));
    }

    @Test
    void shouldGetRequestByIdTest() {
        RequestDtoOut retrievedRequest = RequestService.getRequest(user.getId(), request.getId());

        assertNotNull(retrievedRequest);
        assertEquals(request.getId(), retrievedRequest.getId());
        assertEquals(request.getDescription(), retrievedRequest.getDescription());
        assertEquals(user.getId(), retrievedRequest.getRequestor().getId());
    }
}