import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItApplication;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.UserStorageInMemory;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItApplication.class})
public class UserServiceTests {

    private static UserStorageInMemory userStorageInMemory = new UserStorageInMemory();
    private static UserService userService = new UserService(userStorageInMemory);
    private static UserDto userDto;
    private static Map<String, Object> updates;
    static UserDto createdUser;

    @BeforeAll
    static void setUp() {
        userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        createdUser = userService.createUser(userDto);

        updates = new HashMap<>();
        updates.put("name", "Jane Doe");
    }

    @Test
    void shouldCreateUserTest() {
        UserDto newUserDto = UserDto.builder()
                .name("Alex Doe")
                .email("Alex@example.com")
                .build();

        UserDto newCreatedUser = userService.createUser(newUserDto);

        assertNotNull(createdUser.getId());
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(newCreatedUser.getName(), newUserDto.getName());
        assertEquals(newCreatedUser.getId(), 2);
    }

    @Test
    void shouldGetUsersTest() {
        UserDto userDto = new UserDto(1L, "John", "john@example.com");

        Collection<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertTrue(users.contains(createdUser));
    }
    @Test
    void testUpdateUser() {
        UserDto updatedUser = userService.updateUser(createdUser.getId(), updates);
        Assertions.assertEquals(updates.get("name"), updatedUser.getName());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(createdUser.getId());
        Assertions.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void testGetUser() {
        UserDto retrievedUser = userService.getUser(createdUser.getId());
        Assertions.assertEquals(createdUser.getId(), retrievedUser.getId());
        Assertions.assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }
}