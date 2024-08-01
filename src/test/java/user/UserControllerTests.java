package user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItApplication;
import ru.yandex.practicum.user.UserController;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserUpdateDto;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItApplication.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTests {

    private final UserController userController;
    private UserDto userDto;
    private UserDto createdUser;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        createdUser = userController.createUser(userDto);

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Jane Doe");
    }

    @Test
    void shouldCreateUserTest() {
        UserDto newUserDto = UserDto.builder()
                .name("Alex Doe")
                .email("Alex@example.com")
                .build();

        UserDto newCreatedUser = userController.createUser(newUserDto);

        assertNotNull(createdUser.getId());
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(newCreatedUser.getName(), newUserDto.getName());
        assertEquals(newCreatedUser.getId(), 2);
    }

    @Test
    void shouldGetUsersTest() {
        Collection<UserDto> users = userController.getAllUsers();

        assertNotNull(users);
        assertTrue(users.contains(createdUser));
    }

    @Test
    void testUpdateUser() {
        UserDto updatedUser = userController.updateUser(createdUser.getId(), userUpdateDto);
        Assertions.assertEquals(userUpdateDto.getName(), updatedUser.getName());
    }

    @Test
    void testDeleteUserById() {
        userController.deleteUserById(createdUser.getId());
        Assertions.assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void testGetUser() {
        UserDto retrievedUser = userController.getUser(createdUser.getId());
        Assertions.assertEquals(createdUser.getId(), retrievedUser.getId());
        Assertions.assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }
}