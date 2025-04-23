/* src/model/RestaurantController.java */
package model;

import view.RestaurantView;
import java.util.ArrayList;

public class RestaurantController {

    private final RestaurantModel model;
    private final RestaurantView view;

    public RestaurantController(RestaurantModel model, RestaurantView view) {
        this.model = model;
        this.view = view;
    }

    public void handleAddServer(String name) {
        model.addServer(name);
    }

    public void handleAssignTable(int numTable, int numPeople, String serverName) {
        if (!model.assignTableToServer(numTable, numPeople, serverName))
            view.displayError("Could not assign table.");
        else
            view.displayTables(model.getTables().getTablesInfo());
    }

    public void handleAddOrder(int numTable, ArrayList<Item> items) {
        model.addOrderToTable(numTable, items);
        view.displayTables(model.getTables().getTablesInfo());
    }

    public void handleCloseTable(int tableNumber, double tip) {
        model.closeTable(tableNumber, tip);
        view.displayTables(model.getTables().getTablesInfo());
    }

    public RestaurantModel getModel() {
        return model;
    }
}
