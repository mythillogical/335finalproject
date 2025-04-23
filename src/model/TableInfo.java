package model;

public class TableInfo {
    private final int id;
    private final int capacity;
    private final int seated;

    public TableInfo(int id, int capacity, int seated) {
        this.id = id;
        this.capacity = capacity;
        this.seated = seated;
    }

    public int getId() { return id; }

    public int getCapacity() { return capacity; }

    public int getSeated() { return seated; }
    
    public String toString() {
    	return Integer.toString(id) + " " + Integer.toString(capacity);
    }

}
