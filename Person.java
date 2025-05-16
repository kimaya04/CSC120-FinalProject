public class Person {

    // Attributes
    private String name;
    private String description;
    private Building currentBuilding;
    private Location currentRoom;


    // Default constructor
    public Person(String name, String description) {
        this.name = name;
        this.description = description;
        this.currentBuilding = null; 
        this.currentRoom = null;
    }

    public Person(String name, String description, Location place) {
        this.name = name;
        this.description = description;
        this.currentBuilding = null; 
        this.currentRoom = place;
    }

    /* Getter
     * 
     * Returns the name of the person.
     * @return name the name of the person. 
     */
    public String getName() {
        return name;
    }

    /* Getter
     * 
     * Returns the description of the person.
     * @return description the description of the person. 
     */

    /* Setter
     * 
     * Sets the name of the person.
     * @param name the name of the person. 
     */
    public void setName(String name) {
        this.name = name;
    }

    /* Setter
     * 
     * Sets the description of the person.
     * @param description the description of the person. 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* Getter
     * 
     * Returns the description of the person.
     * @return description the description of the person. 
     */
    public String getDescription() {
        return description;
    }

    /* Getter
     * 
     * Returns the current room of the person.
     * @return currentRoom the current room of the person. 
     */
    public Location getCurrentRoom() {
        return currentRoom;
    }

    /* Getter
     * 
     * Returns the current building of the person.
     * @return currentBuilding the current building of the person. 
     */
    public Building getCurrentBuilding() {
        return currentBuilding;
    }

    /* Setter
     * 
     * Sets the current location of the person.
     * @param currentBuilding the current building of the person.
     * @param currentRoom the current room of the person.
     */
    public void setLocation(Building currentBuilding, Room currentRoom) {
        this.currentBuilding = currentBuilding;
        this.currentRoom = currentRoom;
    }

    /* Setter
     * 
     * Sets the current building of the person.
     * @param currentBuilding the current building of the person. 
     */
    public void setBuilding(Building currentBuilding) {
        this.currentBuilding = currentBuilding;
    }

    /* Setter
     * 
     * Sets the current room of the person.
     * @param currentRoom the current room of the person. 
     */
    public void setRoom(Location room) {
        this.currentRoom = room;
        if (room instanceof Room) {
            this.currentBuilding = ((Room)room).getBuilding();
        } else {
            this.currentBuilding = null;
        }
    }

}
