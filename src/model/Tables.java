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
        // Add code to read from a tables.txt file with an integer per line indicating the table capacity
        tables = new ArrayList<>();
        readFile(filePath);
    }
    
    private void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        	String line;
        	int ID = 1;
            while ((line = br.readLine()) != null) {
                int capacity = Integer.parseInt(line.trim());
                tables.add(new Table(ID, capacity));
                ID += 1;
            }
        	
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

        Collections.sort(available, Comparator.comparing(table -> table.canSeat(people)));
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
    
    public ArrayList<Table> getTables() {
    	return tables;
    }

    public Bill closeTable(int id) {
        for (Table table : tables) {
            if (table.getId() == id) {
                return table.close();
            }
        }
        return null;
    }
}
