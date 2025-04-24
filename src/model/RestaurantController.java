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
