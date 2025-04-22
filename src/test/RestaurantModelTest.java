package test;

import static org.junit.jupiter.api.Assertions.*;
import model.*;

import org.junit.jupiter.api.Test;

class RestaurantModelTest {

	@Test
	void TestaddServer() {
		RestaurantModel model = new RestaurantModel();
		model.addServer("name1");
		model.addServer("name2");
		model.addServer("name3");
		assertEquals(model.getServers().get(0).getName(), "name1");
		assertEquals(model.getServers().get(1).getName(), "name2");
		assertEquals(model.getServers().get(2).getName(), "name3");
		
		assertEquals(model.getServers().size(), 3);
	}
	
	@Test
	void TestRemoveServer() {
		RestaurantModel model = new RestaurantModel();
		model.addServer("name1");
		model.addServer("name2");
		model.addServer("name3");
		assertEquals(model.getServers().get(0).getName(), "name1");
		assertEquals(model.getServers().get(1).getName(), "name2");
		assertEquals(model.getServers().get(2).getName(), "name3");
		
		assertTrue(model.removeServer("name1"));
		assertFalse(model.removeServer("Wong Name"));
		
		assertEquals(model.getServers().size(), 2);
		
		
	}

}
