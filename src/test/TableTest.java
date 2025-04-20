package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.*;

class TableTest {

	@Test
	void test() {
		Table table = new Table(1, 10);
		Server server = new Server("name1");
		
		Bill tableClose = table.close();
		System.out.println(tableClose.getTotalCost());
		System.out.println(tableClose.getServer());
	}
	
	@Test
	void testTales() {
		Tables tables = new Tables("tables.txt");
		System.out.println(tables.getAvailable(2));
	}

}
