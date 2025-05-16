public class House extends Building {

    private int nResidents;

    public House(String name) {
        super(name);
        this.nResidents = 0;
    }

    public House(String name, String address) {
        super(name, address);
        this.nResidents = 0;
    }

    public House(String name, int floors, String address) {
        super(name, floors, address);
        this.nResidents = 0;
    }

    public House(String name, int floors, String address, int nResidents) {
        super(name, floors, address);
        this.nResidents = nResidents;
    }

    public int countResidents() {
        return nResidents;
    }

    public boolean hasDining() {
        for (Room room: this.getRooms()) {
            if (room.getName().contains("Dining")) {
                return true;
            }
        }
        return false;
    }

    public Room getDiningHall() {
        for (Room room : this.getRooms()) {
            if (room.getName().contains("Dining")) {
                return room;
            }
        }
        return null;
    }
    
    
}
