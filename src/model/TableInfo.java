package model;

/*
 * Lightweight summary of a Table object for display purposes. Contains basic information: table ID,
 * capacity, occupancy status, and seated guest count
 * 
 * author: Michael B, Michael D, Asif R, Mohammed A
 */
public class TableInfo {
    private final int id;
    private final int capacity;
    private final int seated;

    /*
     * constructs a TableInfo snapshot
     */
    public TableInfo(int id, int capacity, int seated) {
        this.id = id;
        this.capacity = capacity;
        this.seated = seated;
    }

    /*
     * basic getters
     */
    public int getId() { return id; }

    public int getCapacity() { return capacity; }

    public int getSeated() { return seated; }
    
    public String toString() {
    	return Integer.toString(id) + " " + Integer.toString(capacity);
    }

}
