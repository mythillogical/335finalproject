package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuItem {
	private Map<String, ArrayList<Item>> menuMap;
	
	public MenuItem(String filePath) {
		menuMap = new HashMap<>();
		readFile(filePath);
	}
	
	public void readFile (String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // to skip the first line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String category = values[0];
                String name = values[1];
                double cost = Double.parseDouble(values[2]);
                
                if (!menuMap.containsKey(category)) {
                	menuMap.put(category, new ArrayList<>());
                }
                
                Item item = new Item(name, category, cost);
                menuMap.get(category).add(item);
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Map<String, ArrayList<Item>> getMenuMap() {
		return menuMap;
	}
	
	public ArrayList<Item> getItemsByCategory(String category) {
		return menuMap.get(category);
	}
	
	@Override
	public String toString() {
		String str = "";
		for (Map.Entry<String, ArrayList<Item>> m : menuMap.entrySet()) {
            String catigory = m.getKey();
            ArrayList<Item> items = m.getValue();
            
            str += catigory + ":" + "\n";
            for (Item item : items) {
            	str += item.getName() + " = ";
            	str += Double.toString(item.getCost()) + "\n";
            }
            str += "\n";
        }
		return str;
	}
	
}