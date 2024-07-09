import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareItApplication;
import ru.yandex.practicum.item.ItemService;
import ru.yandex.practicum.item.ItemStorageInMemory;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.dto.ItemUpdateDto;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.UserStorageInMemory;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItApplication.class})
public class ItemServiceTests {

    private static UserStorageInMemory userStorageInMemory = new UserStorageInMemory();
    private static UserService userService = new UserService(userStorageInMemory);
    private static ItemStorageInMemory itemStorageInMemory = new ItemStorageInMemory();
    private static ItemService itemService = new ItemService(itemStorageInMemory, userStorageInMemory);
    private static UserDto userDto;
    private static ItemDto createdItem;
    private static UserDto user;
    private static ItemUpdateDto itemUpdateDto;

    @BeforeAll
    static void setUp() {
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

        createdItem = itemService.createItem(itemDto, user.getId());

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

        ItemDto newCreatedItem = itemService.createItem(newItemDto, user.getId());

        assertNotNull(createdItem.getId());
        assertEquals(userDto.getId(), createdItem.getOwner());
        assertEquals(newItemDto.getName(), newCreatedItem.getName());
        assertEquals(newItemDto.getDescription(), newCreatedItem.getDescription());
        assertEquals(newItemDto.getAvailable(), newCreatedItem.getAvailable());
        assertEquals(newCreatedItem.getId(), 2);
    }

    @Test
    void shouldGetItemsTest() {
        Collection<ItemDto> items = itemService.getAllUsersItems(user.getId());
        ItemDto item = itemService.getItem(1L);

        assertNotNull(items);
        assertTrue(items.contains(item));
    }

    @Test
    void testUpdateItem() {
        ItemDto updatedItem = itemService.updateItem(itemUpdateDto, createdItem.getId(), user.getId());
        assertEquals(itemUpdateDto.getName(), updatedItem.getName());
    }

    @Test
    void testGetItem() {
        ItemDto retrievedItem = itemService.getItem(createdItem.getId());
        assertEquals(createdItem.getId(), retrievedItem.getId());
        assertEquals(createdItem.getOwner(), retrievedItem.getOwner());
        assertEquals(createdItem.getDescription(), retrievedItem.getDescription());
        assertEquals(createdItem.getAvailable(), retrievedItem.getAvailable());
    }

    @Test
    void testFindItemForBooking() {
        Collection<ItemDto> foundItems = itemService.findItemForBooking("Item 1");
        assertEquals(1, foundItems.size());
        assertEquals(createdItem.getId(), foundItems.iterator().next().getId());
    }
}