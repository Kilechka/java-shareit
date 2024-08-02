package user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.user.UserServiceImpl;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {

    private final UserServiceImpl userService;
    private UserDto userDto;
    private UserDto createdUser;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        createdUser = userService.createUser(userDto);

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Jane Doe");
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
        Collection<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertTrue(users.contains(createdUser));
    }

    @Test
    void testUpdateUser() {
        UserDto updatedUser = userService.updateUser(createdUser.getId(), userUpdateDto);
        Assertions.assertEquals(userUpdateDto.getName(), updatedUser.getName());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(createdUser.getId());
        Assertions.assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    void testGetUser() {
        UserDto retrievedUser = userService.getUser(createdUser.getId());
        Assertions.assertEquals(createdUser.getId(), retrievedUser.getId());
        Assertions.assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void shouldNotGiveNotFoundUser() {
        assertThrows(NotFoundException.class, () -> userService.getUser(30L));
    }
}