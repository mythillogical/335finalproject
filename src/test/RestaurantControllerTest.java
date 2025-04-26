package test;

import static org.junit.jupiter.api.Assertions.*;

import controller.RestaurantController;
import org.junit.jupiter.api.Test;

import model.*;
import view.RestaurantView;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

class MockRestaurantView extends RestaurantView {
    boolean errorShown = false;

    public MockRestaurantView() {
        super(new JFrame());  // just a dummy frame to satisfy constructor
    }

    @Override
    public void displayError(String message) {
        errorShown = true;  // simple flag to check if error was triggered
    }

    // You can stub out other methods if needed:
    @Override public void displayTables(List<TableInfo> tables) {}
    @Override public void displayMenu(List<Item> menu) {}
    @Override public void displayBill(int tableNumber, Bill bill) {}
}


class RestaurantControllerTest {

	private RestaurantModel model;
    private MockRestaurantView view;
    private RestaurantController controller;

    @BeforeEach
    void setup() {
        model = new RestaurantModel();
        view = new MockRestaurantView();
        controller = new RestaurantController(model, view);
    }
    
    @Test
    void testAddServer() {
        controller.handleAddServer("Alice");
        assertTrue(model.getServers().containsKey("Alice"));
    }

    @Test
    void testRemoveServerSuccess() {
        controller.handleAddServer("Bob");
        boolean removed = controller.handleRemoveServer("Bob");
        assertTrue(removed);
    }

    @Test
    void testHandleAddOrder() {
        controller.handleAddServer("John");
        model.assignTableToServer(1, 2, "John");

        ArrayList<Item> order = new ArrayList<>();
        order.add(new Item("Burger", "Entree", 7.99));
        controller.handleAddOrder(1, order);

        List<Item> items = model.getTables().getTable(1).getItems();
        assertFalse(items.isEmpty());
        assertEquals("Burger", items.get(0).getName());
    }

    @Test
    void testHandleCloseTable() {
        controller.handleAddServer("Dana");
        model.assignTableToServer(1, 2, "Dana");

        ArrayList<Item> order = new ArrayList<>();
        order.add(new Item("Fries", "Side", 3.49));
        controller.handleAddOrder(1, order);

        controller.handleCloseTable(1, 5.00);

        assertEquals(1, model.getClosedTables().size());
        assertEquals(5.00, model.getServers().get("Dana").getTotalTips());
    }

    @Test
    void testCheckActiveServerReturnsFalseWhenInactive() {
        controller.handleAddServer("InactiveSteve");
        assertFalse(controller.checkActiveServer("InactiveSteve"));
    }

    @Test
    void testCheckActiveServerReturnsTrueWhenActive() {
        controller.handleAddServer("ActiveAnna");
        model.assignTableToServer(1, 2, "ActiveAnna");

        assertTrue(controller.checkActiveServer("ActiveAnna"));
    }

    @Test
    void testGetAvailableTablesSorted() {
        controller.handleAddServer("SortTest");

        // table 1 is occupied
        model.assignTableToServer(1, 2, "SortTest");

        List<Table> availableTables = controller.getAvailableTables();

        for (int i = 1; i < availableTables.size(); i++) {
            assertTrue(availableTables.get(i - 1).getTableID() <= availableTables.get(i).getTableID());
        }

        // ensure no occupied tables returned
        for (Table t : availableTables) {
            assertFalse(t.isOccupied());
        }
    }

    @Test
    void testAddAndRemoveMultipleServers() {
        controller.handleAddServer("A");
        controller.handleAddServer("B");
        controller.handleAddServer("C");

        assertEquals(3, model.getServers().size());

        controller.handleRemoveServer("A");
        controller.handleRemoveServer("B");

        assertEquals(1, model.getServers().size());
        assertTrue(model.getServers().containsKey("C"));
    }

    @Test
    void testGetModelReturnsCorrectInstance() {
        assertEquals(model, controller.getModel());
    }


}
