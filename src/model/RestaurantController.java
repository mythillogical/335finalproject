package model;

import view.RestaurantView;
import java.util.ArrayList;

public class RestaurantController {

    private final RestaurantModel model;
    private final RestaurantView  view;

    public RestaurantController(RestaurantModel m, RestaurantView v) {
        model = m; view = v;
    }

    /* ------------------------ server ------------------------ */

    public void handleAddServer(String name)          { model.addServer(name); }
    public boolean handleRemoveServer(String name)    { return model.removeServer(name); }

    /* ------------------------ seating ----------------------- */

    public boolean handleAssignTable(int id, int guests, String server) {
        boolean ok = model.assignTableToServer(id, guests, server);
        if (!ok)   view.displayError("Could not assign table (check capacity / availability)");
        else       view.displayTables(model.getTables().getTablesInfo());
        return ok;
    }

    public void handleCloseTable(int id, double tip) {
        model.closeTable(id, tip);
        view.displayTables(model.getTables().getTablesInfo());
    }

    /* ------------------------ orders ------------------------ */

    public void handleAddOrder(int id, ArrayList<Item> items) {
        model.addOrderToTable(id, items);
    }

    /* -------------------------------------------------------- */
    public RestaurantModel getModel() { return model; }
}
