package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a waiter / waitress.
 * Keeps name, total tips, and the *live* set of assigned tables.
 * The table set is marked {@code transient} so active UI state is
 * not persisted to disk; after deserialisation we re-initialise it.
 */
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private double tips = 0.0;

    /* live tables are NOT persisted */
    private transient Set<Table> tables = new HashSet<>();

    public Server(String name) { this.name = name; }

    /* hooks used by Table – kept public for tests */
    public void addTable(Table t){ tables.add(t); }
    public void removeTable(Table t){ tables.remove(t); }

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

    /* ---------- deserialisation hook ---------- */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();           // restore non-transient state
        tables = new HashSet<>();         // ensure non-null after load
    }
}
