package model;

import view.RestaurantView;

import java.util.ArrayList;
import java.util.List;

/**
 * glue between model and (multiple) views
 * – core behaviour unchanged; a few convenience
 *   helpers were added for the new gui panels
 */
public class RestaurantController {

    /* mvc handles */
    private final RestaurantModel model;
    private final RestaurantView  view;

    public RestaurantController(RestaurantModel m, RestaurantView v) {
        model = m;
        view  = v;
    }

    /* server ops */
    public void handleAddServer(String name)            { model.addServer(name); }
    public boolean handleRemoveServer(String name)      { return model.removeServer(name); }

    /* seating ops */
    public boolean handleAssignTable(int id, int guests, String server) {
        boolean ok = model.assignTableToServer(id, guests, server);
        if (!ok)  view.displayError("Could not assign table (check capacity / availability)");
        else      view.displayTables(model.getTables().getTablesInfo());
        return ok;
    }

    public void handleCloseTable(int id, double tip) {
        model.closeTable(id, tip);
        view.displayTables(model.getTables().getTablesInfo());
    }

    /* order ops */
    public void handleAddOrder(int id, ArrayList<Item> items) {
        model.addOrderToTable(id, items);
    }

    /* helpers for gui panels */

    /** does this server currently own at least one occupied table? */
    public boolean checkActiveServer(String srv) {
        return model.getTables().getOccupiedTables()
                .stream()
                .anyMatch(t -> t.getServer() != null &&
                        t.getServer().getName().equals(srv));
    }

    /** convenience: list of *non*-occupied tables, already sorted by id */
    public List<Table> getAvailableTables() {
        List<Table> out = new ArrayList<>();
        model.getTables().getTablesInfo().forEach(ti -> {
            Table t = model.getTables().getTable(ti.getId());
            if (t != null && !t.isOccupied()) out.add(t);
        });
        out.sort((a, b) -> Integer.compare(a.getTableID(), b.getTableID()));
        return out;
    }

    /* expose model for direct read-only access where necessary */
    public RestaurantModel getModel() { return model; }
}
