package test;
import model.MenuItem;
// import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class MenuItemTest {

	
	@Test
	void testProccecingFile() {
		MenuItem m = new MenuItem("Menu.csv");
		m.getMenuMap();
	}

}
