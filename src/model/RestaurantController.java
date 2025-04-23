package model;

import view.RestaurantView;
import java.util.ArrayList;

public class RestaurantController {

    private final RestaurantModel model;
    private final RestaurantView view;

    public RestaurantController(RestaurantModel m, RestaurantView v) {
        // Set the model and view for this controller
        model = m;
        view = v;
    }

    // server operations: add or remove a server
    public void handleAddServer(String name) {
        model.addServer(name);
    }
    public boolean handleRemoveServer(String name) {
        return model.removeServer(name);
    }

    // seating operations: assign or close a table
    public boolean handleAssignTable(int id, int guests, String server) {
        boolean ok = model.assignTableToServer(id, guests, server);
        if (!ok) {
            view.displayError("Could not assign table (check capacity / availability)");
        } else {
            view.displayTables(model.getTables().getTablesInfo());
        }
        return ok;
    }

    public void handleCloseTable(int id, double tip) {
        model.closeTable(id, tip);
        view.displayTables(model.getTables().getTablesInfo());
    }

    // order operations: add items to a table order
    public void handleAddOrder(int id, ArrayList<Item> items) {
        model.addOrderToTable(id, items);
    }

    // expose the model for view access or testing
    public RestaurantModel getModel() {
        return model;
    }
}