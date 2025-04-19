package model;

import java.util.ArrayList;
import java.util.Map;

public class RestaurantController {
	private RestaurantModel model;
	
	public RestaurantController() {
		model = new RestaurantModel();
	}
	
	public void handleAddServer(String serverID, String name) {
		model.addServer(name);
	}
	
	public void handleAssignTable(int tableNum, int numPeople, String serverName) {
		
	}
	
	public void handleAddOrder(int tableNum, ArrayList<Item> items) {
		for (Table table : model.getTables()) {
			if (table.getId() == tableNum) {
				table.addItems(items);
			}
		}
	}
	
	public void handleCloseOrder(int tableNum, double tip) {
		model.closeTable(tableNum, tip);
	}
	
	public void handleShowSalesReport() {
		//Map<Item, Integer> sales = getSalesByItem();
		//Map<Item, Double> ravenue = getRevenueByItem();
		//Server topTipped = getTopTippedServer();
	}
	
	//public Map<Item, Integer> getSalesByItem() {
	//	return model.getSalesByItem();
	//}
	
	
}
