package test;

import model.Bill;
import model.Item;
import model.Server;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {

	@Test
    void testConstructorAndGetters() {
        Server server = new Server("John");
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Pasta", "Main", 8.0));
        items.add(new Item("Drink", "Beverage", 2.0));

        Bill bill = new Bill(items, 2, server, 3.0);

        assertEquals(2, bill.getPeople());
        assertEquals(server, bill.getServer());
        assertEquals(3.0, bill.getTip());
        assertEquals(10.0, bill.getItemsCost());
        assertEquals(13.0, bill.getTotalCost());
        assertEquals(6.5, bill.getCostSplitEvenly());
    }

    @Test
    void testBillWithNoItems() {
        Server server = new Server("Empty");
        Bill bill = new Bill(new ArrayList<>(), 1, server, 0.0);
        assertEquals(0.0, bill.getItemsCost());
        assertEquals(0.0, bill.getTotalCost());
        assertEquals(0.0, bill.getCostSplitEvenly());
    }

    @Test
    void testZeroPeopleSplit() {
        Server server = new Server("Test");
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Burger", "Main", 5.0));

        Bill bill = new Bill(items, 0, server, 1.0);
        assertEquals(6.0, bill.getTotalCost());
        assertEquals(0.0, bill.getCostSplitEvenly());  // Avoid division by zero
    }

    @Test
    void testNullServerAllowed() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Fries", "Side", 3.0));
        Bill bill = new Bill(items, 1, null, 1.0);
        assertNull(bill.getServer());
    }

    @Test
    void testGetItemsReturnsDefensiveCopy() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Cake", "Dessert", 3.0));

        Bill bill = new Bill(items, 1, new Server("Sam"), 1.0);
        List<Item> copy = bill.getItems();
        copy.clear();  // This should NOT affect the original

        assertEquals(1, bill.getItems().size());
    }

    @Test
    void testGetItemsCostWithModifications() {
        Item item = new Item("Burger", "Main", 5.0);
        item.addModification(new model.Modification("Extra Cheese", 1.5));
        item.addModification(new model.Modification("Spicy", 0.5));

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        Bill bill = new Bill(items, 1, new Server("Max"), 0.0);
        assertEquals(7.0, bill.getItemsCost());  // 5.0 + 1.5 + 0.5
    }

    @Test
    void testTotalCostZeroGuestsMultipleItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Wings", "Appetizer", 6.0));
        items.add(new Item("Beer", "Drink", 4.0));

        Bill bill = new Bill(items, 0, new Server("Lily"), 2.0);
        assertEquals(12.0, bill.getTotalCost());
        assertEquals(0.0, bill.getCostSplitEvenly());
    }

    @Test
    void testTipGetter() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Pizza", "Main", 9.0));

        Bill bill = new Bill(items, 2, new Server("Kai"), 3.5);
        assertEquals(3.5, bill.getTip());
    }

    @Test
    void testCostSplitEvenlyWithMultiplePeople() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Sushi Roll", "Entree", 10.0));
        items.add(new Item("Green Tea", "Drink", 2.0));

        Bill bill = new Bill(items, 4, new Server("Maya"), 4.0); // total = 16
        assertEquals(4.0, bill.getCostSplitEvenly());
    }

    @Test
    void testFloatingPointPrecision() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Coffee", "Drink", 2.333));
        items.add(new Item("Bagel", "Breakfast", 3.777));

        Bill bill = new Bill(items, 2, new Server("Noah"), 1.89);
        assertEquals(8.0, bill.getTotalCost(), 0.001);  // Allow slight float variance
    }

    @Test
    void testCostSplitEvenlyWhenPeopleIsZero() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Ice Cream", "Dessert", 4.0));
        Bill bill = new Bill(items, 0, new Server("Leo"), 1.0);

        assertEquals(5.0, bill.getTotalCost());
        assertEquals(0.0, bill.getCostSplitEvenly());  // should avoid division by zero
    }

    @Test
    void testNegativeTipAccepted() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Noodles", "Main", 6.0));

        Bill bill = new Bill(items, 1, new Server("Jess"), -1.0);
        assertEquals(5.0, bill.getTotalCost());  // 6.0 - 1.0
        assertEquals(-1.0, bill.getTip());
    }

    @Test
    void testBillWithNullServer() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Taco", "Main", 3.0));

        Bill bill = new Bill(items, 1, null, 1.0);
        assertNull(bill.getServer());
    }

    @Test
    void testEmptyItemList() {
        Bill bill = new Bill(new ArrayList<>(), 2, new Server("Quinn"), 0.0);
        assertEquals(0.0, bill.getItemsCost());
        assertEquals(0.0, bill.getTotalCost());
        assertEquals(0.0, bill.getCostSplitEvenly());
    }
    
    @Test
    void testGetItemsDefensiveCopy() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Coffee", "Drink", 2.5));
        Bill bill = new Bill(items, 1, new Server("Sage"), 0.0);

        List<Item> copy = bill.getItems();
        copy.clear();  // Try to mutate

        assertEquals(1, bill.getItems().size());  // Original should be unchanged
    }
    
}
