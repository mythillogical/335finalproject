package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Tables {
    // list of all tables
    private final List<Table> tables = new ArrayList<>();

    // load table definitions from file
    public Tables(String filePath) {
        readFile(filePath);
    }

    // read each line of the file and create table objects
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // try to seat a party at a table under a server
    public boolean assignTable(int id, int guests, Server s) {
        Table t = getTable(id);
        if (t == null) return false;
        int space = t.canSeat(guests);
        if (space < 0) return false;
        t.seat(guests, s);
        return true;
    }

    // add items to a table's current order
    public void addItemsOrderToTable(int id, List<Item> order) {
        Table t = getTable(id);
        if (t != null && t.isOccupied()) {
            t.addItems(order);
        }
    }

    // remove a specific item from a table
    public boolean removeItemFromTable(int id, Item i) {
        Table t = getTable(id);
        return t != null && t.removeItem(i);
    }

    // clear a table and free it up
    public void closeTable(int id) {
        Table t = getTable(id);
        if (t != null) {
            t.close();
        }
    }

    // find a table by its id
    public Table getTable(int id) {
        for (Table t : tables) {
            if (t.getTableID() == id) return t;
        }
        return null;
    }

    // get basic info for all tables
    public List<TableInfo> getTablesInfo() {
        List<TableInfo> out = new ArrayList<>();
        for (Table t : tables) {
            out.add(new TableInfo(t.getTableID(), t.getCapacity(), t.getNumSeated()));
        }
        return out;
    }

    // get info for tables that can seat the given party
    public List<TableInfo> getAvailable(int guests) {
        List<TableInfo> out = new ArrayList<>();
        for (Table t : tables) {
            if (t.canSeat(guests) >= 0) {
                out.add(new TableInfo(t.getTableID(), t.getCapacity(), t.getNumSeated()));
            }
        }
        return out;
    }

    // list all currently occupied tables
    public List<Table> getOccupiedTables() {
        List<Table> out = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied()) out.add(t);
        }
        return out;
    }

    // get the bill snapshot for a table
    public Bill getBillTable(int id) {
        Table t = getTable(id);
        return (t != null) ? t.getBill() : null;
    }
}
