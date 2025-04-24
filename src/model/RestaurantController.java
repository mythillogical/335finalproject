package model;

import java.util.ArrayList;
//import java.util.Map;

/*
 * 
+handleAddServer(serverID: String, name: String): void
+handleAssignTable(tableNumber: int, serverID: String): void
+handleAddOrder(tableNumber: int, items: List~MenuItem~): void
+handleCloseOrder(tableNumber: int, tip: double): void
+handleShowSalesReport(): void
+getSalesByItem(): Map~MenuItem, Integer~
+getRevenueByItem(): Map~MenuItem, Double~
+getTopTippedServer(): Server
+sortSalesByFrequency(): List~MenuItem~
+sortSalesByRevenue(): List~MenuItem~
*/

public class RestaurantController {
    private RestaurantModel model;
    //private RestaurantView view;

    public RestaurantController(RestaurantModel model) {
        this.model = model;
        //this.view = view;
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
}
