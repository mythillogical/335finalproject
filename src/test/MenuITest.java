package test;
import model.Menu;
// import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class MenuITest {

	
	@Test
	void testProccecingFile() {
		Menu m = new Menu("Menu.csv");
		System.out.println(m);
		
	}

}
