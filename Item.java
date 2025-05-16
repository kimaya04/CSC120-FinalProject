public class Item {
    private String name;
    private String description;
    private boolean isTakeable;
    private boolean isTaken;

    public Item(String name, String description, boolean takeable) {
        this.name = name;
        this.description = description;
        this.isTakeable = takeable;
        this.isTaken = false;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }

    public void take() {
        if (!isTakeable) {
            System.out.println("You cannot take that.");
        } else if (isTaken) {
            System.out.println("You have already taken that.");
        } else {
            isTaken = true;
            System.out.println("You have taken the " + name + ".");
        }
    }

    public void setTaken(boolean taken) {
        this.isTaken = taken;
    }
    

    public void drop() {
        if (!isTaken) {
            System.out.println("You have not taken that.");
        } else {
            isTaken = false;
            System.out.println("You have dropped the " + name + ".");
        }
    }

    public boolean isTakeable() {
        return isTakeable;
    }

    public boolean isTaken() {
        return isTaken;
    }
}
