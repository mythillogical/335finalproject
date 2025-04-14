package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tables {
    private ArrayList<Table> tables;

    public Tables(String filePath) {
        // Add code to read from a tables.txt file with an integer per line indicating the table capacity
        int[] tableVals = new int[4];
        int i = 0;
        for (int table : tableVals) {
            tables.add(new Table(i++, table));
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

    public Bill closeTable(int id) {
        for (Table table : tables) {
            if (table.getId() == id) {
                return table.close();
            }
        }
        return null;
    }
}
