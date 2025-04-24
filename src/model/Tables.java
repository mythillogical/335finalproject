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
<<<<<<< Updated upstream
            tables.sort(Comparator.comparingInt(Table::getTableID));
        } catch (IOException ex) {
            ex.printStackTrace();
=======
            
            Collections.sort(tables, Comparator.comparingInt(table -> table.getTableId()));
        	
        } catch (IOException e) {
            e.printStackTrace();
>>>>>>> Stashed changes
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
    
    public ArrayList<Table> getNotOqubiedTable() {
    	ArrayList<Table> tempTables = new ArrayList<>();
    	for (Table table : tables) {
    		if (!table.getIsOccupied()) {
    			tempTables.add(table);
    		}
    	}
    	return tempTables;
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

<<<<<<< Updated upstream
    // get info for tables that can seat the given party
    public List<TableInfo> getAvailable(int guests) {
        List<TableInfo> out = new ArrayList<>();
        for (Table t : tables) {
            if (t.canSeat(guests) >= 0) {
                out.add(new TableInfo(t.getTableID(), t.getCapacity(), t.getNumSeated()));
=======
    public Bill closeTable(int numTable) {
        for (Table table : tables) {
            if (table.getTableId() == numTable) {
                return table.close();
>>>>>>> Stashed changes
            }
        }
        return out;
    }
<<<<<<< Updated upstream

    // list all currently occupied tables
    public List<Table> getOccupiedTables() {
        List<Table> out = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied()) out.add(t);
=======
    
    public ArrayList<Table> getOccuipiedTables() {
    	ArrayList<Table> tables = new ArrayList<>();
    	for (Table table : this.tables) {
            if (table.getIsOccupied()) {
                tables.add(table);
            }
>>>>>>> Stashed changes
        }
        return out;
    }

    // get the bill snapshot for a table
    public Bill getBillTable(int id) {
        Table t = getTable(id);
        return (t != null) ? t.getBill() : null;
    }
<<<<<<< Updated upstream
=======
    
    public Table getTable(int numTable) {
    	for (Table table : tables) {
    		if (table.getTableId() == numTable) {
    			return table;
    		}
    	}
    	return null;
    }
    
    public boolean assignTable(int numTable, int numPeople, Server server) {
    	for (Table table : tables) {
    		if (table.getTableId() == numTable) {
    			table.seat(numPeople, server);
    			return true;
    		}
    	}
    	return false;
    }
    
    public void addItemsOrderToTable(int numTable, ArrayList<Item> items) {
    	for (Table table : tables) {
    		if (table.getTableId() == numTable) {
    			table.addItems(items);
    		}
    	}
    }
    
    public boolean removeItemFromTable(int numTable, Item item) {
    	for (Table table : tables) {
    		if (table.getTableId() == numTable) {
    			return table.removeItem(item.getName());
    		}
    	}
    	return true;
    }
    
>>>>>>> Stashed changes
}
