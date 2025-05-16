import java.util.ArrayList;

public class OutsideLocation extends Location {

    private String weather;
    private String timeOfDay;
    private ArrayList<Item> items;

    public OutsideLocation(String name, String description) {
        super(name, description);
        this.weather = "Unknown";
        this.timeOfDay = "Unknown";
        this.items = new ArrayList<>();
    }

    public OutsideLocation(String name, String description, String weather, String timeOfDay) {
        super(name, description);
        this.weather = weather;
        this.timeOfDay = timeOfDay;
        this.items = new ArrayList<>();
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
    
}
