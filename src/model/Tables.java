package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/* manages all restaurant tables */
public class Tables {

    /* full list kept in the order file declared it */
    private final List<Table> tables = new ArrayList<>();

    public Tables(String filePath) { readFile(filePath); }

    /* file loading */
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

    /* seating helpers */
    public boolean assignTable(int id, int guests, Server s) {
        Table t = getTable(id);
        if (t == null) return false;
        int space = t.canSeat(guests);
        if (space < 0) return false;
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

    /* query helpers */
    public Table getTable(int id) {
        for (Table t : tables)
            if (t.getTableID() == id) return t;
        return null;
    }

    public List<TableInfo> getTablesInfo() {
        List<TableInfo> out = new ArrayList<>();
        for (Table t : tables)
            out.add(new TableInfo(t.getTableID(), t.getCapacity(), t.getNumSeated()));
        return out;
    }

    public List<TableInfo> getAvailable(int guests) {
        List<TableInfo> out = new ArrayList<>();
        for (Table t : tables)
            if (t.canSeat(guests) >= 0)
                out.add(new TableInfo(t.getTableID(), t.getCapacity(), t.getNumSeated()));
        return out;
    }

    public List<Table> getOccupiedTables() {
        List<Table> out = new ArrayList<>();
        for (Table t : tables)
            if (t.isOccupied()) out.add(t);
        return out;
    }

    public Bill getBillTable(int id) {
        Table t = getTable(id);
        return (t != null) ? t.getBill() : null;
    }

    /* new: read-only access to *all* tables */
    public List<Table> getAllTables() {
        /* unmodifiable so callers can’t mutate internal list */
        return Collections.unmodifiableList(tables);
    }
}
