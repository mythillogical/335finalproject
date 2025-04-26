package test;

import model.Modification;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModificationTest {

	@Test
    void testConstructorAndGetters() {
        Modification mod = new Modification("Extra Sauce", 0.75);
        assertEquals("Extra Sauce", mod.getDescription());
        assertEquals(0.75, mod.getPrice());
    }

    @Test
    void testZeroPriceModification() {
        Modification mod = new Modification("No Ice", 0.0);
        assertEquals("No Ice", mod.getDescription());
        assertEquals(0.0, mod.getPrice());
    }

    @Test
    void testNegativePriceModification() {
        Modification mod = new Modification("Discount", -1.0);
        assertEquals(-1.0, mod.getPrice(), "Should allow negative price if needed");
    }

    @Test
    void testToStringFormat() {
        Modification mod = new Modification("Hot Sauce", 0.5);
        String expected = "\t- Hot Sauce = 0.5";
        assertEquals(expected, mod.toString());
    }

    @Test
    void testToStringWithZeroCost() {
        Modification mod = new Modification("Plain", 0.0);
        assertEquals("\t- Plain = 0.0", mod.toString());
    }

    @Test
    void testToStringWithLongDecimal() {
        Modification mod = new Modification("Cheese", 0.12345);
        assertTrue(mod.toString().contains("Cheese"));
        assertTrue(mod.toString().contains("0.12345"));
    }
    
    @Test
    void testNullDescription() {
        Modification mod = new Modification(null, 1.0);
        assertNull(mod.getDescription());
    }

    @Test
    void testDescriptionWhitespacePreserved() {
        Modification mod = new Modification("  Extra  ", 0.5);
        assertEquals("  Extra  ", mod.getDescription());
    }

    @Test
    void testNegativeZeroIsZero() {
        Modification mod = new Modification("Weird Mod", -0.0);
        assertEquals(0.0, mod.getPrice());
    }

    @Test
    void testLargePriceValue() {
        Modification mod = new Modification("Gold Flakes", 99999.99);
        assertEquals(99999.99, mod.getPrice());
    }

    @Test
    void testToStringIsNotNullOrEmpty() {
        Modification mod = new Modification("Plain", 0.0);
        String str = mod.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }

    
}
