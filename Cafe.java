import java.util.ArrayList;
import java.util.Arrays;

public class Cafe extends Room {

    private int nCups;
    private int coffeeOunces;
    private int nSugarPackets;
    private int nCreams;
    private ArrayList<String> menu = new ArrayList<String>(Arrays.asList("Cappuccino", "Latte", "Americano", "Mocha", "Espresso"));

    public Cafe(String name, String address) {
        super(name, address);
        this.nCups = 10;
        this.coffeeOunces = 20;
        this.nSugarPackets = 10;
        this.nCreams = 10;
    }

    public Cafe(String name, String address, int nCups, int coffee, int nSugarPackets, int nCreams) {
        super(name, address);
        this.nCups = nCups;
        this.coffeeOunces = coffee;
        this.nSugarPackets = nSugarPackets;
        this.nCreams = nCreams;
    }

    public void showMenu() {
        System.out.println("*****" + "MENU" + "*****");
        for (String item : menu) {
            System.out.println("- " + item);
        }
    }

    public void takeOrder(int coffee, int sugar, int cream) {
        if (nCups > 0 && nSugarPackets >= sugar && nCreams >= cream && coffeeOunces >= coffee) {
            coffeeOunces -= coffee;
            nCups--;
            nSugarPackets -= sugar;
            nCreams -= cream;
        } else {
            System.out.println("Not enough supplies to take the order, please wait for restock...");
            restock();
        }
    }
    
    public void restock() {
        nCups +=10;
        nSugarPackets += 10;
        nCreams += 10;
        coffeeOunces += 20;
        System.out.println("Inventory restocked.");
    }
}
