package model;

import java.io.FileWriter;
import java.util.*;
import java.io.*;


/*
 * the central data model the restaurant management system. manages the restaurant's menu, tables, 
 * servers, and closed bills. supports observer pattern with listeners to notify the view of changes.
 * provides methods for menu management. server management, table assignment, order managements, billing,
 * and data persistence. 
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 */
public class RestaurantModel {

	/*
	 * interface for objects interested in model in change events (Observer Pattern)
	 */
	public interface ModelListener{ void modelChanged(); }
	
	
	private final List<ModelListener> listeners=new ArrayList<>();
	
	/*
	 * notifies all registered listeners that the model has changed
	 */
	private void fire(){ listeners.forEach(ModelListener::modelChanged); }
	
	/*
	 * adds a listener to the model
	 */
	public void addListener(ModelListener l){ listeners.add(l); }
	
	/*
	 * removes a listener from the model
	 */
	public void removeListener(ModelListener l){ listeners.remove(l); }

	private final Menu   menu   = new Menu("Menu.csv");
	private final Tables tables = new Tables("tables.txt");
	private final Map<String,Server> servers=new HashMap<>();
	private final List<Bill> closed=new ArrayList<>();

	/*
	 * adds a menu item, saves the menu, and notifies listeners
	 */
	public void addMenuItem(Item i){ menu.addItem(i);; saveMenu(); fire(); }
	
	/*
	 * removes a menu item, saves the menu, and notifies listeners 
	 */
	public void removeMenuItem(Item i){ menu.removeItem(i);; saveMenu(); fire(); }

	/*
	 * saves the current menu states to "Menu.csv"
	 */
	private void saveMenu(){
		try(FileWriter w=new FileWriter("Menu.csv")){
			w.write("Category,Name,Cost,Mods\n");
			for(Item it:menu.getAllItems()){
				w.write(String.format("%s,%s,%.2f,%s\n",
						it.getCategory(), it.getName(), it.getCost(), it.modsToCsv()));
			}
		}catch(Exception ignored){}
	}

	/*
	 * adds a new server to the system and notifies listeners
	 */
	public void addServer(String n){ servers.put(n,new Server(n)); fire(); }
	
	/*
	 * removes a server from the system and notifies listeners
	 */
	public boolean removeServer(String n){
		boolean ok=servers.remove(n)!=null; if(ok) fire(); return ok;
	}

	/*
	 * assigns a table to a server and seats guests
	 */
	public boolean assignTableToServer(int id,int g,String s){
		boolean ok=tables.assignTable(id,g,servers.get(s)); if(ok) fire(); return ok;
	}
	
	/*
	 * adds an order to a table and notifies listeners
	 */
	public void addOrderToTable(int id,ArrayList<Item> ord){
		tables.addItemsOrderToTable(id,ord); fire();
	}
	
	/*
	 * closes a table, generates a bill with tip, updates server tips, saves the bill, and notifies listeners
	 */
	public void closeTable(int id,double tip){
		Bill b=tables.getBillTable(id); tables.closeTable(id);
		if(b!=null){
			Bill bt=new Bill(new ArrayList<>(b.getItems()),
					b.getPeople(),b.getServer(),tip);
			closed.add(bt); servers.get(bt.getServer().getName()).addTips(tip);
		}
		fire();
	}

	/* getters */
	public Menu           getMenu(){ return menu; }
	public Tables         getTables(){ return tables; }
	public Map<String,Server> getServers(){ return servers; }
	public List<Bill>     getClosedTables(){ return closed; }
	
	/*
	 * saves the servers map to "servers.dat" file
	 */
	@SuppressWarnings("unchecked")
	public void saveServers() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("servers.dat"))) {
			out.writeObject(servers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * loads the servers map from "servers.dat" file
	 */
	public void loadServers() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("servers.dat"))) {
			Map<String, Server> loaded = (Map<String, Server>) in.readObject();
			servers.clear();
			servers.putAll(loaded);
			fire();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Could not load servers. Starting fresh.");
		}
	}

	/*
	 * saves the closed bills list to "closedBills.dat" file
	 */
	public void saveClosedBills() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("closedBills.dat"))) {
			out.writeObject(closed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * loads the closed bills list from "closedBills.dat" file
	 */
	public void loadClosedBills() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("closedBills.dat"))) {
			List<Bill> loaded = (List<Bill>) in.readObject();
			closed.clear();
			closed.addAll(loaded);
			fire();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Could not load bills. Starting fresh.");
		}
	}

	
}
