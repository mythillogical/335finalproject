package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

	private Table table;
    private Server server;

    @BeforeEach
    void setup() {
        table = new Table(1, 4);
        server = new Server("Alex");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, table.getTableID());
        assertEquals(4, table.getCapacity());
        assertFalse(table.isOccupied());
        assertEquals(0, table.getNumSeated());
    }

    @Test
    void testCanSeatReturnsCorrectValue() {
        assertEquals(2, table.canSeat(2));  // enough seats
        table.seat(4, server);              // occupy
        assertEquals(-1, table.canSeat(1)); // already taken
    }

    @Test
    void testSeatAssignsServerAndOccupiesTable() {
        table.seat(3, server);
        assertTrue(table.isOccupied());
        assertEquals(server, table.getServer());
        assertEquals(3, table.getNumSeated());
        assertTrue(server.getTables().contains(table));
    }

    @Test
    void testAddItemsAndRemoveItem() {
        List<Item> order = new ArrayList<>();
        Item item1 = new Item("Pizza", "Entree", 9.99);
        Item item2 = new Item("Soda", "Drink", 1.99);
        order.add(item1);
        order.add(item2);

        table.seat(2, server);
        table.addItems(order);

        List<Item> tableItems = table.getItems();
        assertEquals(2, tableItems.size());
        assertTrue(tableItems.contains(item1));

        assertTrue(table.removeItem(item1));
        assertEquals(1, table.getItems().size());
    }

    @Test
    void testCloseResetsTableState() {
        table.seat(2, server);
        table.addItems(List.of(new Item("Soup", "Starter", 3.5)));

        table.close();

        assertFalse(table.isOccupied());
        assertEquals(0, table.getNumSeated());
        assertNull(table.getServer());
        assertTrue(table.getItems().isEmpty());
        assertFalse(server.getTables().contains(table));
    }

    @Test
    void testGetBillReturnsCorrectValues() {
        table.seat(2, server);
        Item item = new Item("Burger", "Main", 10.0);
        table.addItems(List.of(item));

        Bill bill = table.getBill();
        assertEquals(10.0, bill.getItemsCost());
        assertEquals(server, bill.getServer());
        assertEquals(2, bill.getPeople());
    }

    @Test
    void testGetItemsReturnsDefensiveCopy() {
        table.seat(2, server);
        table.addItems(List.of(new Item("Fries", "Side", 2.5)));

        List<Item> items = table.getItems();
        items.clear(); // should not affect original

        assertEquals(1, table.getItems().size());
    }

}
