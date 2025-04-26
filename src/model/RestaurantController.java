package model;

import view.RestaurantView;

import java.util.ArrayList;
import java.util.List;

/*
 * Controller for the restaurant management system. handles communication between the RestaurantModel (data)
 * and RestaurantView (UI). Receives user actions and updates the model accordingly. 
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 */
public class RestaurantController {

    private final RestaurantModel model;
    private final RestaurantView  view;

    /*
     * constructs a RestaurantController to link the given model and view
     */
    public RestaurantController(RestaurantModel m, RestaurantView v) {
        model = m;
        view  = v;
    }

    /*
     * adds a new server to the restaurant system
     */
    public void handleAddServer(String name)            { model.addServer(name); }
    
    /*
     * removes a new server to the restaurant system
     */
    public boolean handleRemoveServer(String name)      { return model.removeServer(name); }

    /*
     * assigns a table to a server and seats guests at the table
     */
    public boolean handleAssignTable(int id, int guests, String server) {
        boolean ok = model.assignTableToServer(id, guests, server);
        if (!ok)  view.displayError("Could not assign table (check capacity / availability)");
        else      view.displayTables(model.getTables().getTablesInfo());
        return ok;
    }

    /*
     * closes a table, processes the tip, and refreshes the table view
     */
    public void handleCloseTable(int id, double tip) {
        model.closeTable(id, tip);
        view.displayTables(model.getTables().getTablesInfo());
    }

    /*
     * adds an order (list of items) to a specific table
     */
    public void handleAddOrder(int id, ArrayList<Item> items) {
        model.addOrderToTable(id, items);
    }

    /*
     * checks if a given server currently owns at least one occupied table
     */
    public boolean checkActiveServer(String srv) {
        return model.getTables().getOccupiedTables()
                .stream()
                .anyMatch(t -> t.getServer() != null &&
                        t.getServer().getName().equals(srv));
    }

    /*
     * returns a list of available (non-occupied) tables, sorted by table ID
     */
    public List<Table> getAvailableTables() {
        List<Table> out = new ArrayList<>();
        model.getTables().getTablesInfo().forEach(ti -> {
            Table t = model.getTables().getTable(ti.getId());
            if (t != null && !t.isOccupied()) out.add(t);
        });
        out.sort((a, b) -> Integer.compare(a.getTableID(), b.getTableID()));
        return out;
    }

    /*
     * exposes the model for direct read only access where necessary
     */
    public RestaurantModel getModel() { return model; }
}
