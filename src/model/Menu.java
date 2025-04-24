package model;

import java.io.BufferedReader;
import java.io.FileReader;
<<<<<<< Updated upstream
import java.util.*;
=======
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
>>>>>>> Stashed changes

/* full in-memory menu with add / remove support */
public class Menu {
<<<<<<< Updated upstream

	private final Map<String,List<Item>> map = new HashMap<>();
	private final List<Item> all = new ArrayList<>();

	public Menu(String csv){ read(csv); }
=======
	private Map<String, ArrayList<Item>> menuMap;
	private ArrayList<String> catigories;
	private ArrayList<Item> allItems; // to store all the items
	
	public Menu(String filePath) {
		menuMap = new HashMap<>();
		catigories = new ArrayList<>();
		allItems = new ArrayList<>();
		readFile(filePath);
	}
	
	private void readFile (String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // to skip the first line

            while ((line = br.readLine()) != null) {
            	
                String[] values = line.split(",");
                if (values.length >= 3) {
                	String category = values[0];
	                String name = values[1];
	                double cost = Double.parseDouble(values[2]);
	                
	                if (!menuMap.containsKey(category)) {
	                	menuMap.put(category, new ArrayList<>());
	                	catigories.add(category);
	                }
	                
	                Item item = new Item(name, category, cost);
	                if (values.length > 3) {
	                	String mod = values[3];
	                	item = addModifications(mod, item);
	                }
	                
	                menuMap.get(category).add(item);
	                allItems.add(item);
                }
            }
>>>>>>> Stashed changes

	/* public api */
	public List<Item> getAllItems(){ return all; }

	public void addItem(Item it){
		map.computeIfAbsent(it.getCategory(),k->new ArrayList<>()).add(it);
		all.add(it);
	}
	public void removeItem(Item it){
		all.remove(it);
		List<Item> lst = map.get(it.getCategory());
		if(lst!=null){ lst.remove(it); if(lst.isEmpty()) map.remove(it.getCategory()); }
	}

	/* parsing */
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
		}catch(Exception ignored){}
	}
<<<<<<< Updated upstream
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
=======
	
	public ArrayList<Item> getAllItems() {
		return allItems;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (String catigory : catigories) { 
            str += catigory + ":" + "\n";
            for (Item item : getItemsByCategory(catigory)) {
            	str += item.toString() + "\n";
            }
            str += "\n";
        }
		return str;
>>>>>>> Stashed changes
	}
}
