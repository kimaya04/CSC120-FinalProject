import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class Location {

    private String name;
    private String description;
    private HashMap<String, Location> exits;
    private ArrayList<Item> items;

    public Location(String name) {
        this.name = name;
        this.description = "No description available.";
        this.exits = new java.util.HashMap<>();
        this.items = new ArrayList<>();
    }

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new java.util.HashMap<>();
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setExits(String dir, Location location) {
        exits.put(dir.toLowerCase(), location);
    }

    public Location getExit(String dir) {
        return exits.get(dir.toLowerCase());
    }

    public Map<String, Location> getExits() {
        return exits;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    
    @Override
    public String toString() {
        return name + ": " + description;
    }

}
