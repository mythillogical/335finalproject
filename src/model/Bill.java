package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Snapshot of a bill when a table closes. */
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Item> items;
    private final int people;
    private final String serverName;   // store only the name (simpler for csv)
    private final double tip;

    /* create bill with no tip */
    public Bill(ArrayList<Item> items, int people, Server server) {
        this(items, people, server, 0.0);
    }

    /* create bill including tip */
    public Bill(ArrayList<Item> items, int people, Server server, double tip) {
        this.items      = new ArrayList<>(items);
        this.people     = people;
        this.serverName = server != null ? server.getName() : "";
        this.tip        = tip;
    }

    /* ---------- helpers ---------- */
    public double getItemsCost() { return Item.getItemsCost(new ArrayList<>(items)); }
    public double getTotalCost() { return getItemsCost() + tip; }
    public double getCostSplitEvenly() { return people == 0 ? 0 : getTotalCost() / people; }

    /* ---------- getters ---------- */
    public List<Item> getItems()   { return new ArrayList<>(items); }
    public int        getPeople()  { return people; }
    public String     getServer()  { return serverName; }
    public double     getTip()     { return tip; }

    /* ---------- csv helpers (for RestaurantModel) ---------- */
    /** one-line csv: server,people,tip,item1&mods;item2&mods;…  */
    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(serverName).append(',')
                .append(people).append(',')
                .append(String.format("%.2f", tip)).append(',');
        for (Item it : items) {
            sb.append(it.getName()).append('^')          // ^ separates name vs mods
                    .append(it.modsToCsv().replace(';', '|'))  // avoid nested “;” inside item list
                    .append(';');                              // ; separates items
        }
        if (!items.isEmpty()) sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /** recreate from csv row created by {@link #toCsv()} */
    public static Bill fromCsv(String row, RestaurantModel rm) {
        String[] p = row.split(",",4);
        if (p.length < 4) return null;

        String srv  = p[0];
        int    ppl  = Integer.parseInt(p[1]);
        double tip  = Double.parseDouble(p[2]);

        ArrayList<Item> itList = new ArrayList<>();
        for (String tok : p[3].split(";")) {                    // per item
            String[] ip = tok.split("\\^",2);
            String name = ip[0];
            Item menuIt = rm.getMenu().getAllItems().stream()
                    .filter(i -> i.getName().equals(name)).findFirst().orElse(null);
            if (menuIt == null) continue;                       // skip unknown items
            if (ip.length == 2 && !ip[1].isBlank()) {           // restore chosen mods
                List<Modification> chosen = new ArrayList<>();
                for (String mTok : ip[1].split("\\|"))
                    if (!mTok.isBlank()) {
                        String[] mp = mTok.split(":");
                        chosen.add(new Modification(mp[0], Double.parseDouble(mp[1])));
                    }
                itList.add(menuIt.withModifications(chosen));
            } else itList.add(menuIt);
        }
        return new Bill(itList, ppl, new Server(srv), tip);
    }
}
