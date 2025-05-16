import java.util.ArrayList;

public class Room extends Location {

    private ArrayList<Item> items;
    private ArrayList<Person> people;
    private Building building;

    public Room(String name, String description) {
        super(name, description);
        this.building = null;
        this.items = new ArrayList<>();
        this.people = new ArrayList<>();
    }

    public Room(String name, String description, Building building) {
        super(name, description);
        this.building = building;
        this.people = new ArrayList<>();
    }
    
    public String getName() {
        return super.getName();
    }

    public String getDescription() {
        return super.getDescription();
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    
    public Building getBuilding() {
        return building;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void enter(Person person) {
        people.add(person);
        person.setRoom(this);
    }

    public void leave(Person person) {
        people.remove(person);
        person.setRoom(null);
    }
}
