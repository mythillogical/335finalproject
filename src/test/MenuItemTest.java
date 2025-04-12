package test;
import model.Item;
import model.MenuItem;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MenuItemTest {

	/*
	@Test
	void testProccecingFile() {
		MenuItem m = new MenuItem("Menu.csv");
		m.getMenuMap();
	}*/
	
	public static void main(String[] args) {
	    MenuItem m = new MenuItem("Menu.csv");
	    System.out.println(m);
	}

}
