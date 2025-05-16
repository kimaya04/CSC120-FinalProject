import java.util.ArrayList;

public class Student extends Person{

    private String major;
    private double gpa;
    private double sanity;
    private double health;
    private ArrayList<String> classes;
    private House assignedHouse;
    private ArrayList<Item> inventory;
    

    /* Default Constructor */
    public Student(String name, String description) {
        super(name, description);
        this.major = "Undeclared";
        this.gpa = 3.0;
        this.sanity = 80;
        this.health = 50;
        this.classes = new ArrayList<>();
        this.inventory = new ArrayList<>();
        
    }

    /* Full Constructor */
    public Student(String name, String description, int studentID, String major, int gradYear) {
        super(name, description);
        this.major = major;
        this.gpa = 3.0;
        this.sanity = 80;
        this.health = 50; 
        this.classes = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    /* Setter 
     * 
     * Sets the house of the student.
     * @param house the house of the student.
    */
    public void setHouse(House house) {
        this.assignedHouse = house;
    }

    /* Getter 
     * 
     * Returns the house of the student.
     * @return assignedHouse the house of the student.
    */
    public House getHouse() {
        return assignedHouse;
    }

    /* 
     * Adds a class to the student's roster.
     * @param className the name of the class to be added
     */
    public void addClass(String className) {
        classes.add(className);
    }


    /* Setter
     * 
     * Sets the major of the student.
     * @param major the major of the student.
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /* Getter
     * 
     * Returns the major of the student.
     * @return major the major of the student.
     */
    public String getMajor() {
        return major;
    }

    /* Getter
     * 
     * Returns the GPA of the student.
     * @return gpa the GPA of the student.
     */
    public double getGPA() {
        return gpa;
    }

    /* Getter
     * 
     * Returns the sanity of the student.
     * @return sanity the sanity of the student.
     */
    public double getSanity() {
        return sanity;
    }

    /* Getter
     * 
     * Returns the health of the student.
     * @return health the health of the student.
     */
    public double getHealth() {
        return health;
    }

    /*
     * Updates the GPA of the student.
     * @param add the amount to add to the GPA.
     * If the GPA exceeds 4.0, it is set to 4.0.
     */
    public void updateGPA(double add) {
        gpa += add;
        if (gpa > 4.0) {
            gpa = 4.0;
        } 
    }

    /*
     * Updates the sanity of the student.
     * @param add the amount to add to the sanity.
     * If the sanity exceeds 100, it is set to 100.
     */
    public void updateSanity(double add) {
        sanity += add;
        if (sanity > 80) {
            sanity = 80;
        }
    }


    public void updateHealth(double amount) {
        health += amount;
        if (health > 50) health = 50;
        if (health < 0) health = 0;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void dropItem(Item item) {
        inventory.remove(item);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

}
