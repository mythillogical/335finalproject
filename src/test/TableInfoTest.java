package test;

import model.TableInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * A JUnit testcase for TableInfo.java
 */
class TableInfoTest {

	@Test
    void testConstructorAndGetters() {
        TableInfo info = new TableInfo(5, 6, 4);
        assertEquals(5, info.getId());
        assertEquals(6, info.getCapacity());
        assertEquals(4, info.getSeated());
    }

    @Test
    void testToStringFormat() {
        TableInfo info = new TableInfo(3, 4, 2);
        assertEquals("3 4", info.toString());  // Format: ID Capacity
    }

}
