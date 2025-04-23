package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Tables {

    private final List<Table> tables = new ArrayList<>();

    public Tables(String filePath) { readFile(filePath); }

    /* -------------------------------------------------------------- */

    private void readFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String ln;
            while ((ln = br.readLine()) != null) {
                String[] parts = ln.trim().split("\\s+");
                if (parts.length == 2) {
                    int id  = Integer.parseInt(parts[0]);
                    int cap = Integer.parseInt(parts[1]);
                    tables.add(new Table(id, cap));
                }
            }
            tables.sort(Comparator.comparingInt(Table::getTableID));
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    /* -------------------------------------------------------------- */

    /** Assigns *and* seats a server + guests if possible. */
    public boolean assignTable(int id, int guests, Server s) {
        Table t = getTable(id);
        if (t == null)         return false;
        if (t.canSeat(guests) < 0) return false;   // already taken
        if (t.canSeat(guests) < 0) return false;   // over capacity
        t.seat(guests, s);
        return true;
    }

    public void addItemsOrderToTable(int id, List<Item> order) {
        Table t = getTable(id);
        if (t != null && t.isOccupied()) t.addItems(order);
    }

    public boolean removeItemFromTable(int id, Item i) {
        Table t = getTable(id);
        return t != null && t.removeItem(i);
    }

    public void closeTable(int id) {
        Table t = getTable(id);
        if (t != null) t.close();
    }

    /* -------------------------------------------------------------- */

    public Table           getTable(int id)         { return tables.stream()
            .filter(t -> t.getTableID()==id)
            .findFirst().orElse(null); }

    public List<TableInfo> getTablesInfo()          {
        List<TableInfo> out = new ArrayList<>();
        tables.forEach(t -> out.add(new TableInfo(t.getTableID(),
                t.getCapacity(),
                t.getNumSeated())));
        return out;
    }

    public List<TableInfo> getAvailable(int guests){
        List<TableInfo> out = new ArrayList<>();
        tables.stream().filter(t -> t.canSeat(guests) >= 0)
                .forEach(t -> out.add(new TableInfo(t.getTableID(),
                        t.getCapacity(),
                        t.getNumSeated())));
        return out;
    }

    public List<Table> getOccupiedTables() {
        List<Table> out = new ArrayList<>();
        tables.stream().filter(Table::isOccupied).forEach(out::add);
        return out;
    }

    public Bill getBillTable(int id) {
        Table t = getTable(id);
        return t == null ? null : t.getBill();
    }
}
