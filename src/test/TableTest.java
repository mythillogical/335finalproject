package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.*;

class TableTest {

	@Test
	void test() {
		Table table = new Table(1, 10);
		Server server = new Server("name1");
		server.addTips(10.0);
		table.assign(0, server);
		Item item1 = new Item("name1", "catigory1", 20);
		Item item2 = new Item("name2", "catigory2", 20);
		table.addItem(item2);
		table.addItem(item1);
		
		Table tableClose = table.close();
		System.out.println(tableClose.getTotalCost());
		System.out.println(tableClose.getServer());
	}
	
	@Test
	void testTales() {
		Tables tables = new Tables("tables.txt");
		System.out.println(tables.getAvailable(2));
	}

}
