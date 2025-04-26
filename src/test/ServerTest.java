package test;

import model.Server;
import model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
 * A JUnit testcase for Server.java
 */
class ServerTest {

	private Server server;

    @BeforeEach
    void setup() {
        server = new Server("Emily");
    }

    @Test
    void testConstructorAndGetName() {
        assertEquals("Emily", server.getName());
        assertEquals(0.0, server.getTotalTips());
        assertEquals(0, server.getNumTables());
    }

    @Test
    void testAddTableIncreasesCount() {
        Table table = new Table(1, 4);
        server.addTable(table);

        assertEquals(1, server.getNumTables());
        assertTrue(server.getTables().contains(table));
    }

    @Test
    void testRemoveTableDecreasesCount() {
        Table table = new Table(1, 4);
        server.addTable(table);
        server.removeTable(table);

        assertEquals(0, server.getNumTables());
        assertFalse(server.getTables().contains(table));
    }

    @Test
    void testAddTipsAccumulatesCorrectly() {
        server.addTips(3.5);
        server.addTips(2.0);
        assertEquals(5.5, server.getTotalTips());
    }

    @Test
    void testGetTablesIsUnmodifiable() {
        Table table = new Table(2, 2);
        server.addTable(table);

        Set<Table> tables = server.getTables();
        assertThrows(UnsupportedOperationException.class, () -> tables.clear());
    }

    @Test
    void testToStringFormat() {
        server.addTips(7.25);
        Table table = new Table(3, 4);
        server.addTable(table);

        String output = server.toString();
        assertTrue(output.contains("Emily"));
        assertTrue(output.contains("1 tbl"));
        assertTrue(output.contains("7.25"));
    }

}
