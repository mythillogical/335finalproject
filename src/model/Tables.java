package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tables {
    private ArrayList<Table> tables;

    public Tables(String filePath) {
        tables = new ArrayList<>();
        readFile(filePath);
    }
    
    private void readFile(String filePath) {
    	try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    int numTab = Integer.parseInt(parts[0]);
                    int capacity = Integer.parseInt(parts[1]);
                    tables.add(new Table(numTab, capacity));
                }
            }
            
            Collections.sort(tables, Comparator.comparingInt(table -> table.getTableId()));
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TableInfo> getAvailable(int people) {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.canSeat(people) >= 0) {
                available.add(table);
            }
        }

        // Collections.sort(available, Comparator.comparing(table -> table.canSeat(people)));
        ArrayList<TableInfo> tablesInfo = new ArrayList<>();

        for (Table table : available) {
            tablesInfo.add(table.getTableInfo());
        }
        return tablesInfo;
    }

    public ArrayList<TableInfo> getTablesInfo() {
        ArrayList<TableInfo> tablesInfo = new ArrayList<>();
        for (Table table : tables) {
            tablesInfo.add(table.getTableInfo());
        }
        return tablesInfo;
    }

    public Bill closeTable(int numTable) {
        for (Table table : tables) {
            if (table.getTableId() == numTable) {
                return table.close();
            }
        }
        return null;
    }
    
    public ArrayList<Table> getOccuipiedTables() {
    	ArrayList<Table> tables = new ArrayList<>();
    	for (Table table : this.tables) {
            if (table.getIsOccupied()) {
                tables.add(table);
            }
        }
    	return tables;
    }
    
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
    
}
