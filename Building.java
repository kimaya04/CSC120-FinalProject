import java.util.ArrayList;
import java.util.HashMap;

public class Building extends Location{

    // Attributes
    private int floors;
    private String address;
    private ArrayList<Room> rooms;
    private HashMap<String, Room> roomMap;

    /* Constructor with name */
    public Building(String name) {
        super(name);
        this.floors = 1;
        this.address = "Unknown";
        this.rooms = new ArrayList<>();
        this.roomMap = new HashMap<>();
    }

    /* Constructor with name and address */
    public Building(String name, String address) {
        super(name);
        this.floors = 1;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.roomMap = new HashMap<>();
    }

    /* Constructor with all parameters */
    public Building(String name, int floors, String address) {
        super(name);
        this.floors = floors;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.roomMap = new java.util.HashMap<>();
    }

    /* Getter
     * 
     * Returns the number of floors in the building.
     * @return floors the number of floors in the building. 
     */
    public int getFloors() {
        return floors;
    }

    /* Getter
     * 
     * Returns the address of the building.
     * @return address the address of the building.
     */
    public String getAddress() {
        return address;
    }

    /* 
     * Adds a room to the building.
     * @param room the room to be added.
     */
    public void addRoom(Room room) {
        room.setBuilding(this);
        rooms.add(room);
        roomMap.put(room.getName().toLowerCase(), room);
    }
    
    /*
     * Returns the list of rooms in the building.
     * @return rooms the list of rooms in the building.
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Room getRoom(String name) {
        return roomMap.get(name.toLowerCase());
    }


    public HashMap<String, Room> getRoomMap() {
        return roomMap;
    }
}