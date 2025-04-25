package model;

import java.io.*;
import java.util.*;

/** Central state holder + simple observer. Now persists servers & closed bills. */
public class RestaurantModel {

	/* ------------- observer boilerplate ------------- */
	public interface ModelListener { void modelChanged(); }
	private final List<ModelListener> listeners = new ArrayList<>();
	private void fire(){ listeners.forEach(ModelListener::modelChanged); }
	public void addListener(ModelListener l){ listeners.add(l); }
	public void removeListener(ModelListener l){ listeners.remove(l); }

	/* ------------- core data ------------- */
	private final Menu   menu   = new Menu("Menu.csv");
	private final Tables tables = new Tables("tables.txt");

	/* servers & bills are now persisted */
	private final Map<String,Server> servers = new HashMap<>();
	private final List<Bill>         closed  = new ArrayList<>();

	/* file paths */
	private static final String FILE_SERVERS = "servers.csv";
	private static final String FILE_BILLS   = "closedBills.csv";

	/* ctor: read persisted data */
	public RestaurantModel() {
		loadServers();
		loadClosedBills();
	}

	/* ------------- MENU ------------- */
	public void addMenuItem(Item i){ menu.addItem(i); saveMenu(); fire(); }
	public void removeMenuItem(Item i){ menu.removeItem(i); saveMenu(); fire(); }

	private void saveMenu(){
		try(PrintWriter w = new PrintWriter("Menu.csv")){
			w.println("Category,Name,Cost,Mods");
			for(Item it: menu.getAllItems())
				w.printf("%s,%s,%.2f,%s%n",
						it.getCategory(), it.getName(),
						it.getCost(), it.modsToCsv());
		}catch(Exception ignored){}
	}

	/* ------------- SERVERS ------------- */
	public void addServer(String n){
		servers.put(n, new Server(n)); saveServers(); fire();
	}
	public boolean removeServer(String n){
		boolean ok = servers.remove(n) != null;
		if(ok){ saveServers(); fire(); }
		return ok;
	}

	private void loadServers(){
		File f = new File(FILE_SERVERS);
		if(!f.exists()) return;
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			br.readLine();                       // skip header
			String ln;
			while((ln = br.readLine()) != null){
				String[] p = ln.split(",",2);
				if(p.length==2){
					Server s = new Server(p[0]);
					s.addTips(Double.parseDouble(p[1]));
					servers.put(s.getName(), s);
				}
			}
		}catch(Exception ignored){}
	}
	private void saveServers(){
		try(PrintWriter w = new PrintWriter(FILE_SERVERS)){
			w.print(Server.header());
			servers.values().forEach(s -> w.print(s.toCsv()));
		}catch(Exception ignored){}
	}

	/* ------------- TABLE SEATING & ORDERS ------------- */
	public boolean assignTableToServer(int id,int g,String srv){
		boolean ok = tables.assignTable(id,g,servers.get(srv));
		if(ok) fire();
		return ok;
	}
	public void addOrderToTable(int id,ArrayList<Item> ord){
		tables.addItemsOrderToTable(id, ord); fire();
	}

	/* ------------- CLOSE TABLE ------------- */
	public void closeTable(int id,double tip){
		Bill b = tables.getBillTable(id);
		tables.closeTable(id);
		if(b != null){
			Bill bt = new Bill(new ArrayList<>(b.getItems()),
					b.getPeople(), servers.get(b.getServer()), tip);
			closed.add(bt);
			servers.get(bt.getServer()).addTips(tip);
			saveServers();
			appendClosedBill(bt);
		}
		fire();
	}

	/* append single bill row – cheaper than rewriting whole file each time */
	private void appendClosedBill(Bill b){
		try(PrintWriter w = new PrintWriter(new FileWriter(FILE_BILLS,true))){
			if(new File(FILE_BILLS).length()==0)
				w.println("Server,People,Tip,Items");     // header
			w.println(b.toCsv());
		}catch(Exception ignored){}
	}
	private void loadClosedBills(){
		File f = new File(FILE_BILLS);
		if(!f.exists()) return;
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			br.readLine();                       // skip header
			String ln;
			while((ln = br.readLine()) != null){
				Bill b = Bill.fromCsv(ln,this);
				if(b != null) closed.add(b);
			}
		}catch(Exception ignored){}
	}

	/* ------------- getters ------------- */
	public Menu            getMenu()  { return menu; }
	public Tables          getTables(){ return tables; }
	public Map<String,Server> getServers(){ return servers; }
	public List<Bill>      getClosedTables(){ return closed; }
}
