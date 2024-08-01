package item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.ShareItApplication;
import ru.yandex.practicum.item.ItemController;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.dto.ItemUpdateDto;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItApplication.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTests {

    private final UserService userService;
    private final ItemController itemController;
    private UserDto userDto;
    private ItemDto createdItem;
    private UserDto user;
    private ItemUpdateDto itemUpdateDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("John Doe")
                .email("doe@example.com")
                .build();

        user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .build();

        createdItem = itemController.createItem(itemDto, user.getId());

        itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Updated Item 1");
    }

    @Test
    void shouldCreateItemTest() {
        ItemDto newItemDto = ItemDto.builder()
                .name("Item 2")
                .description("Description 2")
                .available(false)
                .build();

        ItemDto newCreatedItem = itemController.createItem(newItemDto, user.getId());

        assertNotNull(createdItem.getId());
        assertEquals(userDto.getId(), createdItem.getOwner());
        assertEquals(newItemDto.getName(), newCreatedItem.getName());
        assertEquals(newItemDto.getDescription(), newCreatedItem.getDescription());
        assertEquals(newItemDto.getAvailable(), newCreatedItem.getAvailable());
        assertEquals(newCreatedItem.getId(), 2);
    }

    @Test
    void shouldGetItemsTest() {
        Collection<ItemDto> items = itemController.getAllUsersItems(user.getId());

        assertNotNull(items);
        assertTrue(items.contains(createdItem));
    }

    @Test
    void testUpdateItem() {
        ItemDto updatedItem = itemController.updateItem(itemUpdateDto, createdItem.getId(), user.getId());
        assertEquals(itemUpdateDto.getName(), updatedItem.getName());
    }

    @Test
    void testGetItem() {
        ItemDto retrievedItem = itemController.getItem(createdItem.getId(), user.getId());
        assertEquals(createdItem.getId(), retrievedItem.getId());
        assertEquals(createdItem.getOwner(), retrievedItem.getOwner());
        assertEquals(createdItem.getDescription(), retrievedItem.getDescription());
        assertEquals(createdItem.getAvailable(), retrievedItem.getAvailable());
    }

    @Test
    void testFindItemForBooking() {
        Collection<ItemDto> foundItems = itemController.findItemForBooking("Item 1");
        assertEquals(1, foundItems.size());
        assertEquals(createdItem.getId(), foundItems.iterator().next().getId());
    }
}