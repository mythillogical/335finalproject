package model;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;
import java.util.List;
import java.util.Map;

/*
 * 
+handleAddServer(serverID: String, name: String): void
+handleAssignTable(tableNumber: int, serverID: String): void
+handleAddOrder(tableNumber: int, items: List~MenuItem~): void
+handleCloseOrder(tableNumber: int, tip: double): void
+handleShowSalesReport(): void
+getTopTippedServer(): Server
*/

public class RestaurantController {
    private RestaurantModel model;

    public RestaurantController(RestaurantModel model) {
        this.model = model;
    }
    
    public void handleAddServer(String name) {
    	model.addServer(name);
    }
    
    public boolean handleRemoveServer(String name) {
    	return model.removeServer(name);
    }
    
    public boolean handleAssignTable(int numTable, int numPeople, String serverName) {
    	return model.assignTableToServer(numTable, numPeople, serverName);
    }
    
    public void handleAddOrder(int numTable, ArrayList<Item> items) {
    	model.addOrderToTable(numTable, items);
    }
    
    public void handleCloseTable(int tableNumber, double tip) {
    	model.closeTable(tableNumber, tip);
    }
    
    public boolean checkAtiveServer(String serverName) {
    	return model.checkForServerTable(serverName);
    }
    
    public RestaurantModel getModel() {
    	return model;
    }
    
    public ArrayList<Table> getAvalibleTables() {
    	return model.getAvalbleTables();
    }
    
    public List<Map.Entry<String, Integer>> getSalesByItemSorted() {
        HashMap<String, Integer> salesItem = new HashMap<>();
        ArrayList<Bill> orders = model.getClosedTables();

        // Count items sold
        for (Bill order : orders) {
            for (Item item : order.getItems()) {
                String itemName = item.getName();
                salesItem.put(itemName, salesItem.getOrDefault(itemName, 0) + 1);
            }
        }

        // Convert to list and sort by item count (ascending)
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(salesItem.entrySet());
        sortedList.sort(Map.Entry.comparingByValue()); // .reversed() for descending

        return sortedList;
    }
    
    public List<Map.Entry<String, Double>> getRevenueByItem() {
    	HashMap<String, Double> salesItem = new HashMap<>();
        ArrayList<Bill> orders = model.getClosedTables();

        // Count items sold
        for (Bill order : orders) {
            for (Item item : order.getItems()) {
                String itemName = item.getName();
                double price = item.getCost();
                salesItem.put(itemName, salesItem.getOrDefault(itemName, 0.0) + price);
            }
        }

        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(salesItem.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        return sortedList;
    }
}
