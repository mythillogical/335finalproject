package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tables {
    private ArrayList<Table> tables;

    public Tables(ArrayList<Integer> tableVals) {
        int i = 0;
        for (int table : tableVals) {
            tables.add(new Table(i++, table));
        }
    }

    public ArrayList<Table> getAvailable(int people) {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.canSeat(people) >= 0) {
                available.add(table);
            }
        }
        Collections.sort(available, Comparator.comparing(table -> table.canSeat(people)));
        return available;
    }
}
