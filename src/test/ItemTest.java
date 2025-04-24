package test;
import model.Item;
import model.Modification;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ItemTest {

	@Test
    void testGettersAndTotalCostWithoutMods() {
        Item item = new Item("Burger", "Entree", 5.0);
        assertEquals("Burger", item.getName());
        assertEquals("Entree", item.getCategory());
        assertEquals(5.0, item.getCost());
        assertEquals(5.0, item.getTotalCost());
    }

    @Test
    void testTotalCostWithMods() {
        List<Modification> mods = new ArrayList<>();
        mods.add(new Modification("Extra Cheese", 1.0));
        mods.add(new Modification("Spicy", 0.5));

        Item item = new Item("Burger", "Entree", 5.0, mods);
        assertEquals(6.5, item.getTotalCost());
    }

    @Test
    void testModsToCsv() {
        Item item = new Item("Burger", "Entree", 5.0);
        item.addModification(new Modification("Mayo", 0.25));
        item.addModification(new Modification("Ketchup", 0.15));

        String csv = item.modsToCsv();
        assertEquals("Mayo:0.25;Ketchup:0.15", csv);
    }

    @Test
    void testCopyConstructor() {
        Item original = new Item("Salad", "Appetizer", 4.0);
        original.addModification(new Modification("Dressing", 0.50));
        Item copy = new Item(original);

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getModifications().size(), copy.getModifications().size());
        assertNotSame(original, copy);
    }
    
    @Test
    void testEmptyModificationsListIsUnmodifiable() {
        Item item = new Item("Water", "Drinks", 1.0);
        List<Modification> mods = item.getModifications();
        assertThrows(UnsupportedOperationException.class, () -> mods.add(new Modification("Ice", 0.0)));
    }

    @Test
    void testAddModificationAffectsTotalCost() {
        Item item = new Item("Pizza", "Entree", 10.0);
        item.addModification(new Modification("Extra Cheese", 2.0));
        item.addModification(new Modification("Pepperoni", 1.5));
        assertEquals(13.5, item.getTotalCost());
    }

    @Test
    void testModsToCsvEmpty() {
        Item item = new Item("Soda", "Drinks", 1.0);
        assertEquals("", item.modsToCsv());
    }

    @Test
    void testToStringOutput() {
        Item item = new Item("Fries", "Side", 2.5);
        assertEquals("Fries $2.5", item.toString());
    }

    @Test
    void testGetItemsCostStaticMethod() {
        ArrayList<Item> items = new ArrayList<>();
        Item item1 = new Item("Pasta", "Main", 8.0);
        item1.addModification(new Modification("Extra Sauce", 1.0));

        Item item2 = new Item("Garlic Bread", "Side", 3.0);
        items.add(item1);
        items.add(item2);

        double total = Item.getItemsCost(items);
        assertEquals(12.0, total); // 8 + 1 + 3
    }

}
