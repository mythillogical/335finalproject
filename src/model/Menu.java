package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private Item addModifications(String mod, Item item) {
		if (mod != null && !mod.trim().isEmpty()) {
            for (String part : mod.split(";")) {
                String[] pieces = part.split(":");
                if (pieces.length == 2) {
                    String modName = pieces[0].trim();
                    double modCost = Double.parseDouble(pieces[1].trim());
                    item.addModification(new Modification(modName, modCost));
                }
            }
        }
		return item;
    }
	
	public Map<String, ArrayList<Item>> getMenuMap() {
		return menuMap;
	}
	
	public ArrayList<String> getCatigories() {
		return catigories;
	}
	
	public ArrayList<Item> getItemsByCategory(String category) {
		return menuMap.get(category);
	}
	
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
	}
	
}