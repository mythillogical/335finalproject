package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/* full in-memory menu with add / remove support */
public class Menu {

	private Map<String,List<Item>> map = new HashMap<>();
	private List<Item> all = new ArrayList<>();

	public Menu(String csv){ read(csv); }

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
