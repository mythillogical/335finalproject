package test;

import model.Item;
import model.Server;
import model.Tables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * A JUnit testcase for Tables.java
 */
class TablesTest {

	private Tables tables;
    private Server server;

    @BeforeEach
    void setup() {
        tables = new Tables("tables.txt");  // must exist and contain valid layout
        server = new Server("Amy");
    }

    @Test
    void testAssignTableValid() {
        boolean assigned = tables.assignTable(1, 2, server);
        assertTrue(assigned);

        assertEquals(server, tables.getTable(1).getServer());
        assertTrue(tables.getTable(1).isOccupied());
    }

    @Test
    void testAssignTableInvalidId() {
        boolean result = tables.assignTable(999, 2, server);
        assertFalse(result);
    }

    @Test
    void testAssignTableTooManyGuests() {
        // assuming table 1 has capacity < 10
        assertFalse(tables.assignTable(1, 999, server));
    }

    @Test
    void testAddAndRemoveItemFromTable() {
        Item item = new Item("Steak", "Main", 12.5);
        tables.assignTable(1, 2, server);
        tables.addItemsOrderToTable(1, List.of(item));

        assertEquals(1, tables.getTable(1).getItems().size());

        boolean removed = tables.removeItemFromTable(1, item);
        assertTrue(removed);
        assertEquals(0, tables.getTable(1).getItems().size());
    }

    @Test
    void testCloseTable() {
        tables.assignTable(1, 2, server);
        tables.closeTable(1);

        assertFalse(tables.getTable(1).isOccupied());
        assertEquals(0, tables.getTable(1).getItems().size());
    }

    @Test
    void testGetAvailableReturnsCorrectTables() {
        List<?> available = tables.getAvailable(1);
        assertNotNull(available);
    }

    @Test
    void testGetOccupiedTables() {
        tables.assignTable(1, 2, server);
        var occupied = tables.getOccupiedTables();
        assertTrue(occupied.stream().anyMatch(t -> t.getTableID() == 1));
    }

    @Test
    void testGetTableById() {
        assertNotNull(tables.getTable(1));
        assertNull(tables.getTable(999));
    }

    @Test
    void testGetTablesInfoList() {
        var list = tables.getTablesInfo();
        assertFalse(list.isEmpty());
    }

    @Test
    void testGetAllTablesIsReadOnly() {
        var all = tables.getAllTables();
        assertThrows(UnsupportedOperationException.class, () -> all.clear());
    }

}
