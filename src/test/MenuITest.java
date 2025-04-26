package test;
import model.Item;
import model.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * A JUnit testcase for Menu.java
 */
class MenuITest {

	
	private Menu menu;

    @BeforeEach
    void setup() {
        // Ensure Menu.csv is in your working directory or mock this part in future
        menu = new Menu("Menu.csv");
    }

    @Test
    void testLoadMenuItemsFromCSV() {
        List<Item> allItems = menu.getAllItems();
        assertNotNull(allItems);
        assertFalse(allItems.isEmpty(), "Menu should load at least one item");
    }

    @Test
    void testAddItemIncreasesListSize() {
        int originalSize = menu.getAllItems().size();
        Item newItem = new Item("Test Dish", "Test Category", 4.99);
        menu.addItem(newItem);

        assertEquals(originalSize + 1, menu.getAllItems().size());
        assertTrue(menu.getAllItems().contains(newItem));
    }

    @Test
    void testRemoveItemDecreasesListSize() {
        Item item = new Item("Temp Item", "Temp", 2.99);
        menu.addItem(item);
        int sizeAfterAdd = menu.getAllItems().size();

        menu.removeItem(item);
        int sizeAfterRemove = menu.getAllItems().size();

        assertEquals(sizeAfterAdd - 1, sizeAfterRemove);
        assertFalse(menu.getAllItems().contains(item));
    }

    @Test
    void testItemModificationsLoadedCorrectly() {
        boolean foundItemWithMods = menu.getAllItems().stream()
                .anyMatch(i -> !i.getModifications().isEmpty());

        assertTrue(foundItemWithMods, "At least one item should have modifications loaded");
    }

    @Test
    void testAddItemSameCategory() {
        Item item1 = new Item("Dish A", "Shared Cat", 5.0);
        Item item2 = new Item("Dish B", "Shared Cat", 6.0);
        
        menu.addItem(item1);
        menu.addItem(item2);

        List<Item> allItems = menu.getAllItems();
        long count = allItems.stream()
                .filter(i -> i.getCategory().equals("Shared Cat"))
                .count();

        assertTrue(count >= 2);
    }

    @Test
    void testRemoveNonexistentItem() {
        Item fake = new Item("Ghost", "NoCat", 0.0);
        assertDoesNotThrow(() -> menu.removeItem(fake));
    }

    @Test
    void testAddItemWithModification() {
        Item item = new Item("Soup", "Starter", 3.5);
        item.addModification(new model.Modification("Extra Salt", 0.2));

        menu.addItem(item);
        Item fromMenu = menu.getAllItems().stream()
                .filter(i -> i.getName().equals("Soup"))
                .findFirst().orElse(null);

        assertNotNull(fromMenu);
        assertFalse(fromMenu.getModifications().isEmpty());
    }

    @Test
    void testMenuHandlesBadCsvLineGracefully() {
        // This test is more integration-focused and assumes you simulate a malformed file.
        // In production, consider mocking the readFile() method.
        assertDoesNotThrow(() -> new Menu("badformat.csv"));
    }

    @Test
    void testNoUnintendedDuplicates() {
        Item item = new Item("Tea", "Drinks", 1.5);
        menu.addItem(item);
        menu.addItem(item);  // added twice intentionally

        long count = menu.getAllItems().stream()
                .filter(i -> i.getName().equals("Tea"))
                .count();

        assertTrue(count >= 2, "Duplicates are allowed; track if not intended");
    }

    
}
