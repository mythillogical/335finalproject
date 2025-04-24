package model;

import java.io.FileWriter;
import java.util.*;

/* central state holder with simple observer */
public class RestaurantModel {

	public interface ModelListener{ void modelChanged(); }
	private final List<ModelListener> listeners=new ArrayList<>();
	private void fire(){ listeners.forEach(ModelListener::modelChanged); }
	public void addListener(ModelListener l){ listeners.add(l); }
	public void removeListener(ModelListener l){ listeners.remove(l); }

	private final Menu   menu   = new Menu("Menu.csv");
	private final Tables tables = new Tables("tables.txt");
	private final Map<String,Server> servers=new HashMap<>();
	private final List<Bill> closed=new ArrayList<>();

	/* menu persistence */
	public void addMenuItem(Item i){ menu.addItem(i); saveMenu(); fire(); }
	public void removeMenuItem(Item i){ menu.removeItem(i); saveMenu(); fire(); }

	private void saveMenu(){
		try(FileWriter w=new FileWriter("Menu.csv")){
			w.write("Category,Name,Cost,Mods\n");
			for(Item it:menu.getAllItems()){
				w.write(String.format("%s,%s,%.2f,%s\n",
						it.getCategory(),it.getName(),it.getCost(),it.modsToCsv()));
			}
		}catch(Exception ignored){}
	}

	/* server ops */
	public void addServer(String n){ servers.put(n,new Server(n)); fire(); }
	public boolean removeServer(String n){
		boolean ok=servers.remove(n)!=null; if(ok) fire(); return ok;
	}

	/* seating / orders */
	public boolean assignTableToServer(int id,int g,String s){
		boolean ok=tables.assignTable(id,g,servers.get(s)); if(ok) fire(); return ok;
	}
	public void addOrderToTable(int id,ArrayList<Item> ord){
		tables.addItemsOrderToTable(id,ord); fire();
	}
	public void closeTable(int id,double tip){
		Bill b=tables.getBillTable(id); tables.closeTable(id);
		if(b!=null){
			Bill bt=new Bill(new ArrayList<>(b.getItems()),
					b.getPeople(),b.getServer(),tip);
			closed.add(bt); servers.get(bt.getServer().getName()).addTips(tip);
		}
	}

	/* getters */
	public Menu           getMenu(){ return menu; }
	public Tables         getTables(){ return tables; }
	public Map<String,Server> getServers(){ return servers; }
	public List<Bill>     getClosedTables(){ return closed; }
}
