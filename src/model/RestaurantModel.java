package model;

import java.io.*;
import java.util.*;

/**
 * Central state holder + simple observer.
 * Servers and closed bills are persisted to binary files
 * (servers.dat / closedBills.dat) via Java serialisation.
 */
public class RestaurantModel {

    /* ---------- observer boilerplate ---------- */
    public interface ModelListener { void modelChanged(); }
    private final List<ModelListener> listeners = new ArrayList<>();
    private void fire()                 { listeners.forEach(ModelListener::modelChanged); }
    public  void addListener(ModelListener l){ listeners.add(l); }
    public  void removeListener(ModelListener l){ listeners.remove(l); }

    /* ---------- core data ---------- */
    private final Menu   menu   = new Menu("Menu.csv");
    private final Tables tables = new Tables("tables.txt");

    /* persisted collections */
    private final Map<String,Server> servers = new HashMap<>();
    private final List<Bill>         closed  = new ArrayList<>();

    /* serialised file names */
    private static final String FILE_SERVERS = "servers.dat";
    private static final String FILE_BILLS   = "closedBills.dat";

    public RestaurantModel() {
        loadServers();
        loadClosedBills();
    }

    /* ---------- MENU ---------- */
    public void addMenuItem(Item i){ menu.addItem(i); saveMenu(); fire(); }
    public void removeMenuItem(Item i){ menu.removeItem(i); saveMenu(); fire(); }

    private void saveMenu(){
        try(PrintWriter w = new PrintWriter("Menu.csv")){
            w.println("Category,Name,Cost,Mods");
            for(Item it: menu.getAllItems())
                w.printf("%s,%s,%.2f,%s%n",
                        it.getCategory(), it.getName(),
                        it.getCost(), it.modsToCsv());
        }catch(IOException ignored){}
    }

    /* ---------- SERVER CRUD ---------- */
    public void addServer(String n){
        servers.put(n, new Server(n));
        saveServers();
        fire();
    }
    public boolean removeServer(String n){
        boolean ok = servers.remove(n) != null;
        if(ok){
            saveServers();
            fire();
        }
        return ok;
    }

    /* ---------- TABLE & ORDER OPS ---------- */
    public boolean assignTableToServer(int id,int g,String srv){
        boolean ok = tables.assignTable(id,g,servers.get(srv));
        if(ok) fire();
        return ok;
    }
    public void addOrderToTable(int id,ArrayList<Item> ord){
        tables.addItemsOrderToTable(id, ord);
        fire();
    }

    /* ---------- CLOSE TABLE ---------- */
    public void closeTable(int id,double tip){
        Bill current = tables.getBillTable(id);
        tables.closeTable(id);

        if(current != null){
            Bill finalBill = new Bill(new ArrayList<>(current.getItems()),
                                      current.getPeople(),
                                      servers.get(current.getServer()),
                                      tip);
            closed.add(finalBill);
            servers.get(finalBill.getServer()).addTips(tip);

            saveServers();
            saveClosedBills();
        }
        fire();
    }

    /* ---------- persistence helpers ---------- */
    @SuppressWarnings("unchecked")
    private void loadServers(){
        File f = new File(FILE_SERVERS);
        if(!f.exists()) return;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))){
            servers.clear();
            servers.putAll((Map<String,Server>) in.readObject());
        }catch(Exception ex){
            System.out.println("Could not load servers — starting fresh.");
        }
    }
    private void saveServers(){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_SERVERS))){
            out.writeObject(servers);
        }catch(IOException ex){ ex.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadClosedBills(){
        File f = new File(FILE_BILLS);
        if(!f.exists()) return;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))){
            closed.clear();
            closed.addAll((List<Bill>) in.readObject());
        }catch(Exception ex){
            System.out.println("Could not load bills — starting fresh.");
        }
    }
    private void saveClosedBills(){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_BILLS))){
            out.writeObject(closed);
        }catch(IOException ex){ ex.printStackTrace(); }
    }

    /* ---------- getters ---------- */
    public Menu                 getMenu()        { return menu; }
    public Tables               getTables()      { return tables; }
    public Map<String,Server>   getServers()     { return servers; }
    public List<Bill>           getClosedTables(){ return closed; }
}
