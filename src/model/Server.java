package model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a waiter / waitress.
 * Keeps name, total tips, and the *live* set of assigned tables.
 * The table set is marked {@code transient} so active UI state is
 * not persisted to disk.
 */
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private double tips = 0.0;

    /* live tables are NOT persisted */
    private transient final Set<Table> tables = new HashSet<>();

    public Server(String name) { this.name = name; }

    /* hooks used by Table */
    void addTable(Table t)    { tables.add(t); }
    void removeTable(Table t) { tables.remove(t); }

    /* ---------- getters ---------- */
    public String getName()      { return name; }
    public double getTotalTips() { return tips; }
    public int    getNumTables() { return tables.size(); }
    public Set<Table> getTables(){ return Collections.unmodifiableSet(tables); }

    /* ---------- tips ---------- */
    public void addTips(double amt){ tips += amt; }

    @Override public String toString(){
        return String.format("%s · %d tbl · $%.2f", name, getNumTables(), tips);
    }
}
