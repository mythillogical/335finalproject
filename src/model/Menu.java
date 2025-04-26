package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/* the class represents the in-memory restaurant menu. stores items organized by category and
 * supports adding, removing, loading from a CSV file and saving back to CSV. Each menu item belongs to a category and
 * can have modifications. It does not implement Serializable; instead it relies on external CSV persistence. 
 * 
 *  @author: Michael B, Michael D, Asif R, Mohammed A
 */
public class Menu {

	private Map<String,List<Item>> map = new HashMap<>();
	private List<Item> all = new ArrayList<>();

	/*
	 * constructs the menu by loading items from a .csv file. 
	 */
	public Menu(String csv){ read(csv); }

	/*
	 * returns a list of all menu items across all categories.
	 */
	public List<Item> getAllItems(){ return all; }

	/*
	 * adds a new item to the menu
	 */
	public void addItem(Item it){
		map.computeIfAbsent(it.getCategory(),k->new ArrayList<>()).add(it);
		all.add(it);
	}
	
	/*
	 * removes an item from the menu
	 */
	public void removeItem(Item it){
		all.remove(it);
		List<Item> lst = map.get(it.getCategory());
		if(lst!=null){ lst.remove(it); if(lst.isEmpty()) map.remove(it.getCategory()); }
	}

	/*
	 * reads the menu items from a .csv file
	 * NB. Expected CSV format: Category, Name, Cost, Mods
	 */
	private void read(String path){
		try(BufferedReader br=new BufferedReader(new FileReader(path))){
			String ln=br.readLine();               // skip header
			while((ln=br.readLine())!=null){
				String[] p=ln.split(",",4);
				if(p.length<3) continue;
				String cat=p[0].trim(), name=p[1].trim();
				double cost=Double.parseDouble(p[2].trim());
				Item it=new Item(name,cat,cost);
				if(p.length==4) parseMods(p[3],it);
				addItem(it);
			}
		}catch(Exception ignored){
			// if loading fails, menu remains empty
		}
	}
	
	/*
	 * Parses the modification string and applies mods to an item.
	 * Expected format: "modName:modPrice;modName:modPrice;..."
	 */
	private void parseMods(String s,Item it){
		if(s==null||s.isBlank()) return;
		for(String part:s.split(";")){
			String[] x=part.split(":");
			if(x.length==2){
				it.addModification(
						new Modification(x[0].trim(),
								Double.parseDouble(x[1].trim())));
			}
		}
	}
}
