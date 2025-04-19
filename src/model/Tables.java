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
            
            Collections.sort(tables, Comparator.comparingInt(table -> table.getId()));
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Table> getAvailable(int people) {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.canSeat(people)) {
                available.add(table);
            }
        }
        return available;
    }
    
    public ArrayList<Table> getTables() {
    	return tables;
    }

    public Table closeTable(int id) {
        for (Table table : tables) {
            if (table.getId() == id) {
                return table.close();
            }
        }
        return null;
    }
}
