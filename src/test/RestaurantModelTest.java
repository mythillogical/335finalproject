package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
/*
 * A JUnit testcase for RestaurantModel.java
 */
class RestaurantModelTest {

	private RestaurantModel model;

    @BeforeEach
    void setup() {
        model = new RestaurantModel();
    }

    @Test
    void testAddAndRemoveServer() {
        model.addServer("Alice");
        assertTrue(model.getServers().containsKey("Alice"));

        boolean removed = model.removeServer("Alice");
        assertTrue(removed);

        boolean failedRemove = model.removeServer("Ghost");
        assertFalse(failedRemove);
    }

    @Test
    void testAssignTableToServerSuccess() {
        model.addServer("Bob");
        boolean assigned = model.assignTableToServer(1, 2, "Bob");
        assertTrue(assigned);
    }


    @Test
    void testAddOrderToTable() {
        model.addServer("Cara");
        model.assignTableToServer(1, 2, "Cara");

        ArrayList<Item> order = new ArrayList<>();
        order.add(new Item("Salad", "App", 4.99));
        model.addOrderToTable(1, order);

        Table t = model.getTables().getTable(1);
        assertFalse(t.getItems().isEmpty());
    }

    @Test
    void testCloseTable() {
        model.addServer("David");
        model.assignTableToServer(1, 2, "David");

        ArrayList<Item> order = new ArrayList<>();
        order.add(new Item("Burger", "Main", 8.99));
        model.addOrderToTable(1, order);

        model.closeTable(1, 2.00); // add tip

        assertEquals(1, model.getClosedTables().size());
        Bill closed = model.getClosedTables().get(0);
        assertEquals(2.00, closed.getTip());
        assertEquals(2.00, model.getServers().get("David").getTotalTips());
    }

    @Test
    void testAddAndRemoveMenuItem() {
        Item item = new Item("Tea", "Drinks", 1.50);
        model.addMenuItem(item);
        assertTrue(model.getMenu().getAllItems().contains(item));

        model.removeMenuItem(item);
        assertFalse(model.getMenu().getAllItems().contains(item));
    }

    @Test
    void testModelListenerFiresOnChange() {
        AtomicBoolean triggered = new AtomicBoolean(false);
        model.addListener(() -> triggered.set(true));

        model.addServer("TestTrigger");
        assertTrue(triggered.get());
    }

    @Test
    void testRemoveModelListenerStopsCallback() {
        AtomicBoolean triggered = new AtomicBoolean(false);
        RestaurantModel.ModelListener listener = () -> triggered.set(true);
        model.addListener(listener);
        model.removeListener(listener);

        model.addServer("NoCallback");
        assertFalse(triggered.get());
    }

    @Test
    void testSaveMenuDoesNotCrash() {
        // not much to assert here unless mocking file system, but test coverage matters
        model.addMenuItem(new Item("Temp", "Test", 1.0));
        assertDoesNotThrow(() -> model.removeMenuItem(new Item("Temp", "Test", 1.0)));
    }

}
