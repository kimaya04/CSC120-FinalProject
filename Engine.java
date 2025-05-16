import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Engine {

    // Attributes
    private HashMap<String, Building> buildings;
    private Student player;
    private Scanner scanner;
    private ArrayList<House> houses;
    private ArrayList<Building> academicBuildings;
    private ArrayList<OutsideLocation> outsideLocations;
    private Cafe campusCafe;
    private ArrayList<String> dates;
    private boolean sceneActive;

    // Constructor
    public Engine() {
        buildings = new HashMap<>();
        houses = new ArrayList<>();
        academicBuildings = new ArrayList<>();
        scanner = new Scanner(System.in);
        outsideLocations = new ArrayList<>();
        dates = new ArrayList<>();
        campusCafe = null;
        sceneActive = false;
        dates.add("Monday, September 2nd: Admissions Day");
        dates.add("Tuesday, September 3rd: Move-in Day");
        dates.add("Wednesday, September 4th: First Day of Classes");
        dates.add("Thursday, September 5th: A Weird Day");
        dates.add("Friday, September 6th: Treat Yourself at the Cafe");
        dates.add("Saturday, September 7th: Cleaning Up");
        dates.add("Sunday, September 8th: Judgement Day");
    }

    public void handleGlobalInput(String input) {
        input = input.trim().toLowerCase();

        if (input.contains("quit")) {
            System.out.println("Goodbye, you would've been a good one.");
            quitGame();
        } 

        if (input.contains("look") || input.contains("look at")) {
            Location current = player.getCurrentRoom();
            System.out.println("You are in: " + current.getName());
            System.out.println(current.getDescription());
            System.out.println("The room contains:");
            for (Item item : current.getItems()) {
                if (item.getDescription() != null) {
                    System.out.println("- " + item.getName() + ": " + item.getDescription());
                } else {
                    System.out.println("- " + item.getName());
                }
            }
        }

        if (input.contains("exits") || input.equals("leave")) {
            Location current = player.getCurrentRoom();
            Map<String, Location> exits = current.getExits();

            System.out.println("From here, you can go:");

            for (Map.Entry<String, Location> entry : exits.entrySet()) {
            String direction = entry.getKey();
            Location destination = entry.getValue();
            System.out.println("- " + direction + " → " + destination.getName());
            }
        }

        if (input.startsWith("take") || input.startsWith("get")) {
            String itemName = input.replaceFirst("^(take|get)\\s*", "").trim().toLowerCase();
        
            if (itemName.isEmpty()) {
                System.out.println("What would you like to take?");
                System.out.print("> ");
                itemName = scanner.nextLine().trim().toLowerCase();
            }
        
            boolean found = false;
            for (Item item : player.getCurrentRoom().getItems()) {
                if (item.getName().toLowerCase().contains(itemName)) {
                    player.addItem(item);
                    player.getCurrentRoom().removeItem(item);
                    System.out.println("You picked up the " + item.getName().toLowerCase() + ".");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("That item isn't here.");
            }
            return;
        }
        

        if (input.startsWith("drop") || input.startsWith("put down")) {
            String itemName = input.replaceFirst("^(drop|put down)\\s*", "").trim().toLowerCase();
        
            if (itemName.isEmpty()) {
                System.out.println("What would you like to drop?");
                System.out.print("> ");
                itemName = scanner.nextLine().trim().toLowerCase();
            }
    
            boolean found = false;
            for (Item item : player.getInventory()) {
                if (item.getName().toLowerCase().contains(itemName)) {
                    player.dropItem(item);
                    player.getCurrentRoom().addItem(item);
                    System.out.println("You dropped the " + item.getName().toLowerCase() + ".");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("You don't have that item.");
            }
            return;
        }
        

        if (input.contains("go") || input.contains("walk") || input.contains("move")) {
            String[] parts = input.split(" ", 2);
            if (parts.length > 1) {
                String direction = parts[1].trim().toLowerCase();
                moveToDirection(direction);
                return;
            } else {
                System.out.println("Please specify a direction to go.");
            }
        }

        if (input.equals("north") || input.equals("south") || input.equals("east") || input.equals("west")) {
            moveToDirection(input);
            return;
        }

        if (input.contains("enter") || input.contains("go to")) {
            input = input.substring(input.indexOf("\\s+") + 1);
            moveTo(input);
        }
        
        else if (input.contains("help")) {
            System.out.println("Available commands: quit, help, move, order, stats");
        } 
        
        else if (input.contains("move") || input.contains("go") || input.contains("move to") || input.contains("go to")) {
            System.out.println("Where would you like to move?");
            String buildingName = scanner.nextLine();
            Building building = getBuilding(buildingName);
            if (building != null) {
                moveTo(building);
                System.out.println("You have moved to " + building.getName() + ".");
            }
        } 
        
        else if (input.contains("stats")) {
            showStats(); 
        } 
    }

    /* 
     * Starts the game.
     * 
     * Initializes the game and displays the start messages.
     */
    public void startGame() {
        initialize();

        System.out.println("In this game, you will try to survive your first semester at Smiff College, Smiffshire. You will explore the campus, meet new people, and try to make it through your classes without losing your mind.");
        System.out.println("You can type 'help' to see a list of commands at any time.");
        System.out.println("You can type 'quit' to quit the game.");
        System.out.println("Or type 'go' to start the game.");
        String input = "";
        while (!input.equalsIgnoreCase("go")) {
            System.out.print("> ");
            input = scanner.nextLine().trim();
        
            if (input.equalsIgnoreCase("quit")) {
                quitGame();
            } else if (input.equalsIgnoreCase("help")) {
                printHelpText();
            } else if (!input.equalsIgnoreCase("go")) {
                System.out.println("Please type 'go' to start, or 'help' or 'quit'.");
            }
        }

        int dateCount = 0;
        while (isSurviving() && dateCount < dates.size()) {
            System.out.println("\n--------------------------------");
            System.out.println(dates.get(dateCount));

            sceneActive = true;

            while (sceneActive) {
                if (dateCount==0) {
                    admissionsDay();
                } else if (dateCount==1) {
                    moveIn();
                } else if (dateCount==2) {
                    quiz();
                } else if (dateCount==3) {
                    squirrel();
                } else if (dateCount==4) {
                    findIDCard();
                } else if (dateCount==5) {
                    cleanRoom();
                } else if (dateCount==6) {
                    return;
                }
                System.out.println("\n--------------------------------");
                showStats();
                System.out.println("--------------------------------");
                sceneActive = false;
            }

            
            if (!sceneActive) {
                while (true) {
                    System.out.println("Type 'next' to continue to the next day.");
                    System.out.print("> ");
                    input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("next")) {
                        dateCount++;
                        break;
                    } else {
                        System.out.println("Invalid command. Please type 'next'.");
                    }
                }
            } 
        }
        if (!isSurviving()) {
            handleDeath();
        } else {
            System.out.println("You have survived your first week at Smiff College. Congratulations!");
            showStats();
            handleWin();
        }

    }

    public void initialize() {

        createBuildings();

        createRooms();

        player = new Student("Player", "You are a new student at Smiffshire College.");
        player.addItem(new Item("ID Card", "Your access to all the buildings on campus.", true));
        player.addItem(new Item("Room Key", "Rusty keys that sometimes work. About 65% of the time.", true));

        for (Item item: player.getInventory()) {
            item.setTaken(true);
        }

    }

    public void createBuildings() {

        OutsideLocation lawn = new OutsideLocation("Chopin Lawn", "a large grassy lawn with a suspicious earthy smell that is definitely not from the grass. It stretches out between the Campus Center and the Admissions Office. BEWARE OF THE SQUIRRELS.");
        outsideLocations.add(lawn);

        OutsideLocation admissionsFront = new OutsideLocation("Chopin Lawn (in front of the Admissions Office)",lawn.getDescription());
        OutsideLocation campusCenterFront = new OutsideLocation("Chopin Lawn (in front of the Campus Center)",lawn.getDescription());
        outsideLocations.add(admissionsFront);
        outsideLocations.add(campusCenterFront);

        OutsideLocation resAreaC = new OutsideLocation("Residential Corner (outside Chappell House)", "the residential side of campus. Cobbled paths wind between the houses, littered with bikes and candy wrappers. Somebody blasts Charli XCX in their room, and you hear the faint echoes of crying. You are not sure if it's coming from the same room.");
        OutsideLocation resAreaS = new OutsideLocation("Residential Corner (outside Sabrina House)", "the residential side of campus. Cobbled paths wind between the houses, littered with bikes and candy wrappers. Somebody blasts Charli XCX in their room, and you hear the faint echoes of crying. You are not sure if it's coming from the same room.");
        OutsideLocation resAreaT = new OutsideLocation("Residential Corner (outside Troye House)", "the residential side of campus. Cobbled paths wind between the houses, littered with bikes and candy wrappers. Somebody blasts Charli XCX in their room, and you hear the faint echoes of crying. You are not sure if it's coming from the same room.");
        OutsideLocation resAreaF = new OutsideLocation("Residential Corner (outside Florence House)", "the residential side of campus. Cobbled paths wind between the houses, littered with bikes and candy wrappers. Somebody blasts Charli XCX in their room, and you hear the faint echoes of crying. You are not sure if it's coming from the same room.");
        outsideLocations.add(resAreaC);
        outsideLocations.add(resAreaS);
        outsideLocations.add(resAreaT);
        outsideLocations.add(resAreaF);

        House chappell = new House("Chappell House", 2, "36 Kingsman Street, Smiffshire", 20);
        House sabrina = new House("Sabrina House", 2, "32 Kingsman Street, Smiffshire", 16);
        House troye = new House("Troye House", 2, "34 Kingsman Street, Smiffshire", 16);
        House florence = new House("Florence House", 2, "38 Kingsman Street, Smiffshire", 20);
        houses.add(chappell);
        houses.add(sabrina);
        houses.add(troye);
        houses.add(florence);

        for (House house : houses) {
            addBuilding(house);
        }
        
        OutsideLocation scholarsRowLib = new OutsideLocation("Scholar's Row (outisde the library)", "the academic side of campus. It houses all the classroom buildings and the library, with paved walkways connecting them. The buildings are all made of the same red brick, and the grass is a little too green to be real. You wonder if they use fake grass here.");
        OutsideLocation scholarsRowArt = new OutsideLocation("Scholar's Row (outside Bridgers Hall)", "the academic side of campus. It houses all the classroom buildings and the library, with paved walkways connecting them. The buildings are all made of the same red brick, and the grass is a little too green to be real. You wonder if they use fake grass here.");
        OutsideLocation scholarsRowTech = new OutsideLocation("Scholar's Row (outside Winehouse Lab)", "the academic side of campus. It houses all the classroom buildings and the library, with paved walkways connecting them. The buildings are all made of the same red brick, and the grass is a little too green to be real. You wonder if they use fake grass here.");
        OutsideLocation scholarsRowSci = new OutsideLocation("Scholar's Row (outside Bowie Lab)", "the academic side of campus. It houses all the classroom buildings and the library, with paved walkways connecting them. The buildings are all made of the same red brick, and the grass is a little too green to be real. You wonder if they use fake grass here.");
        OutsideLocation scholarsRowHum = new OutsideLocation("Scholar's Row (outside McCartney Hall)", "the academic side of campus. It houses all the classroom buildings and the library, with paved walkways connecting them. The buildings are all made of the same red brick, and the grass is a little too green to be real. You wonder if they use fake grass here.");
        outsideLocations.add(scholarsRowLib);
        outsideLocations.add(scholarsRowArt);
        outsideLocations.add(scholarsRowTech);
        outsideLocations.add(scholarsRowSci);
        outsideLocations.add(scholarsRowHum);   

        Building library = new Building("Kiyoko Library", "7 Parkvale Lane, Smiffshire");
        library.setDescription("actually pretty cool for a college library. It's a carved wooden building, with one massive, open room. Beauty and the Beast style, the walls are lined with bookshelves, and at the center of the room is an unmanned desk. The high vaulted ceilings are dotted with skylights, letting in the glorious New England gloom.");
        Building sciencesBuilding = new Building("Bowie Labaratory", "9 Parkvale Lane, Smiffshire");
        sciencesBuilding.setDescription("the science building. A large, modern structure with a glass facade, the building literally reflects the sunlight directly into your eyes. Ouch. The interior is bright and airy, with an industrial design: slate stone floors, pipes running along the walls, steel beams and railings. The classrooms are all open-concept, with large windows overlooking the campus. The smell of bleach and formaldehyde lingers in the air.");
        Building artsBuilding = new Building("Bridgers Hall", "5 Parkvale Lane, Smiffshire");
        artsBuilding.setDescription("the arts building, and most beautifully confusing installation on campus. While the minimalist brick exterior makes it look like any other college building, inside you'll find a maze of staircases and hallways. The velvet carpeting is a deep red, and the walls are lined with portraits of past maestros. You feel intimidated by their gaze.");
        Building humanitiesBuilding = new Building("McCartney Hall", "11 Parkvale Lane, Smiffshire");
        humanitiesBuilding.setDescription("the humanities building. Brightly painted with graffiti and murals, the is the hub for the activists on campus. The building is a mix of old and new, with modern classrooms and old lecture halls. The walls are lined with posters for protests and events, and the air is filled with the smell of coffee and incense.");
        Building techBuilding = new Building("Winehouse Labaratory", "3 Parkvale Lane, Smiffshire");
        techBuilding.setDescription("dark grey and looming, the tech building feels like some angsty Smithie's fever dream. The building is made up of four slotted blocks, with glass atriums on the bottom and second floors. The interior is rustic, wooden; glass balconies overlook the foyer from wide hallways, flanked by classrooms and elevators. Something about the walls makes you feel cooler than you should. It's probably the air conditioning.");
        academicBuildings.add(library);
        academicBuildings.add(sciencesBuilding);
        academicBuildings.add(artsBuilding);
        academicBuildings.add(humanitiesBuilding);
        academicBuildings.add(techBuilding);

        for (Building building : academicBuildings) {
            addBuilding(building);
        }

        Building admissionsOffice = new Building("Admissions Office", "11 Middleton Drive, Smiffshire");
        addBuilding(admissionsOffice);
        Building campusCenter = new Building("Campus Center", "12 Middleton Drive, Smiffshire");
        addBuilding(campusCenter);

        lawn.setExits("south", admissionsFront);
        lawn.setExits("north", campusCenterFront);
        lawn.setExits("west", resAreaC);
        lawn.setExits("east", scholarsRowLib);

        admissionsFront.setExits("south", admissionsOffice);
        admissionsFront.setExits("north", lawn);
        admissionsFront.setExits("west", resAreaC);
        admissionsFront.setExits("east", scholarsRowLib);
        campusCenterFront.setExits("north", campusCenter);
        campusCenterFront.setExits("south", lawn);
        campusCenter.setExits("west", resAreaC);
        campusCenter.setExits("east", scholarsRowLib);

        chappell.setExits("east", resAreaC);
        sabrina.setExits("east", resAreaS);
        troye.setExits("east", resAreaT);
        florence.setExits("east", resAreaF);

        resAreaC.setExits("west", chappell);
        resAreaC.setExits("east", lawn);
        resAreaS.setExits("west", sabrina);
        resAreaS.setExits("east", lawn);
        resAreaT.setExits("west", troye);
        resAreaT.setExits("east", lawn);
        resAreaF.setExits("west", florence);
        resAreaF.setExits("east", lawn);

        scholarsRowLib.setExits("west", lawn);
        scholarsRowLib.setExits("east", library);
        scholarsRowLib.setExits("north", scholarsRowArt);
        scholarsRowLib.setExits("south", scholarsRowSci);

        library.setExits("west", scholarsRowLib);

        scholarsRowArt.setExits("west", lawn);
        scholarsRowArt.setExits("east", artsBuilding);
        scholarsRowArt.setExits("north", scholarsRowTech);
        scholarsRowArt.setExits("south", scholarsRowLib);
        
        artsBuilding.setExits("west", scholarsRowArt);

        scholarsRowTech.setExits("west", lawn);
        scholarsRowTech.setExits("east", techBuilding);
        scholarsRowTech.setExits("south", scholarsRowArt);
        
        techBuilding.setExits("west", scholarsRowTech);

        scholarsRowSci.setExits("west", lawn);
        scholarsRowSci.setExits("east", sciencesBuilding);
        scholarsRowSci.setExits("north", scholarsRowLib);
        scholarsRowSci.setExits("south", scholarsRowHum);

        sciencesBuilding.setExits("west", scholarsRowSci);

        scholarsRowHum.setExits("west", lawn);
        scholarsRowHum.setExits("east", humanitiesBuilding);
        scholarsRowHum.setExits("north", scholarsRowSci);

        humanitiesBuilding.setExits("west", scholarsRowHum);

        admissionsOffice.setExits("north", admissionsFront);
        campusCenter.setExits("south", campusCenterFront);

    }

    public void createRooms() {

        Room reception = new Room("Admissions Office Reception", "a large waiting room, lined with creaky wooden chairs and the faint scent of burnt coffee. The walls are cluttered with crooked posters - one reads, \"Internships That Might Pay (They Won’t),\" another declares, \"Open Mic Night: Cry on Stage for Applause,\" and a third simply says, \"Join the Cheese Appreciation Society!\"");
        reception.addItem(new Item("ID card", "Your access to all the buildings on campus.", true));
        reception.addItem(new Item("Room Key", "Rusty keys that sometimes work.", true));


        for (Building building : buildings.values()) {
            if (building.getName().equals("Admissions Office")) {
                continue; // Skip creating a lobby for the Admissions Office
            }
            Room lobby = new Room("Lobby", "the entry area of the " + building.getName() + ".");
            lobby.setBuilding(building);
            building.addRoom(lobby);
        }
        
        reception.setBuilding(buildings.get("Admissions Office"));
        buildings.get("Admissions Office").addRoom(reception);

        Room quietSpace = new Room("Quiet Space", "an intensely silent room to study in, where time does not seem to exist.");
        quietSpace.setBuilding(buildings.get("Kiyoko Library"));
        buildings.get("Kiyoko Library").addRoom(quietSpace);

        Room bioLab = new Room("Room 112 - Biology Lab", "the smallest room in the building, and somehow the most commonly used. The smell of formaldehyde fills the air, and the ");
        bioLab.setBuilding(buildings.get("Bowie Labaratory"));
        buildings.get("Bowie Labaratory").addRoom(bioLab);

        Cafe cc = new Cafe("Campus Center Cafe", "a little nook in the Campus Center to study and get coffee.");
        cc.setBuilding(buildings.get("Campus Center"));
        buildings.get("Campus Center").addRoom(cc);
        campusCafe = cc;

        Room dormRoom = new Room("Dorm Room", "Your very own bat cave at this strange college. A small bed sits in the corner against the massive window, the desk shoved to other side of the room. Next to the desk is a closet, and that's about it. Oh, also, a Hello Kitty laundry hamper. At least you don't have to buy one now.");
        Room florenceDining = new Room("Florence Dining Hall", "where the food is questionable, and the coffee is worse. At least the view of the pond is nice while you wait in line.");
        Room sabrinaDining = new Room("Sabrina Dining Hall", "small, quiet and suspiciously empty. Good for eavesdropping on intense conversations and Asian food.");

        dormRoom.setBuilding(buildings.get("Chappell House"));
        florenceDining.setBuilding(buildings.get("Florence House"));
        florenceDining.getBuilding().addRoom(florenceDining);
        sabrinaDining.setBuilding(buildings.get("Sabrina House"));
        sabrinaDining.getBuilding().addRoom(sabrinaDining);

        dormRoom.setExits("east", dormRoom.getBuilding().getRooms().get(0));
        dormRoom.getBuilding().getRooms().get(0).setExits("west", dormRoom);
        dormRoom.getBuilding().getRooms().get(0).setExits("east", dormRoom.getBuilding().getExit("east"));

        reception.setExits("north", reception.getBuilding().getExit("north"));

        cc.setExits("south", cc.getBuilding().getRooms().get(0));
        cc.getBuilding().getRooms().get(0).setExits("north", cc);
        cc.getBuilding().getRooms().get(0).setExits("south", cc.getBuilding().getExit("south"));

        florenceDining.setExits("south", florenceDining.getBuilding().getRooms().get(0));
        florenceDining.getBuilding().getRooms().get(0).setExits("north", florenceDining);
        sabrinaDining.setExits("south", sabrinaDining.getBuilding().getRooms().get(0));
        sabrinaDining.getBuilding().getRooms().get(0).setExits("north", sabrinaDining);

        quietSpace.setExits("south", quietSpace.getBuilding().getRooms().get(0));
        quietSpace.getBuilding().getRooms().get(0).setExits("north", quietSpace);

        bioLab.setExits("north", bioLab.getBuilding().getRooms().get(0));
        bioLab.getBuilding().getRooms().get(0).setExits("south", bioLab);
        
        
    }
    
    public void addBuilding(Building building) {
        buildings.put(building.getName(), building);
    }

    public Building getBuilding(String name) {
        return buildings.get(name);
    }

    public void moveTo(Room room) {
        player.setRoom(room);
        player.setBuilding(room.getBuilding());
    }

    public void moveTo(Building building) {
        moveTo(building.getRooms().get(0));
    }

    public void moveTo(String input) {
        Location current = player.getCurrentRoom();
        Map<String, Location> exits = current.getExits();
        String cleanedInput = input.toLowerCase().replaceAll("\\s+", "");
    
        for (Map.Entry<String, Location> entry : exits.entrySet()) {
            String name = entry.getKey().toLowerCase().replaceAll("\\s+", "");
            Location destination = entry.getValue();
    
            String destName = destination.getName().toLowerCase().replaceAll("\\s+", "");
    
            if (cleanedInput.equals(name) || cleanedInput.contains(destName)) {
    
                if (destination instanceof Building) {
                    Building b = (Building) destination;
                    moveTo(b); 
                    return;
                }
    
                if (destination instanceof Room) {
                    moveTo((Room) destination);
                    System.out.println("You walk into " + destination.getName() + ".");
                    System.out.println(destination.getDescription());
                    return;
                }
    
                player.setRoom(destination);
                System.out.println("You walk " + name + " to: " + destination.getName());
                return;
            }
        }
    
        System.out.println("You can't jump to there from here. Be realistic.");
    }

    public void moveToDirection(String direction) {
        direction = direction.trim().toLowerCase();
        Location current = player.getCurrentRoom();
        Map<String, Location> exits = current.getExits();
    
        if (exits.containsKey(direction)) {
            Location next = exits.get(direction);
            if (next instanceof Building) {
                moveTo((Building) next);
            } else if (next instanceof Room) {
                moveTo((Room) next);
            } else {
                player.setRoom(next);
            }
            System.out.println("You move " + direction + " to: " + next.getName());
            System.out.println(next.getDescription());
        } else {
            System.out.println("You can't go " + direction + " from here. Come on.");
        }
    }
    
    public void cleanRoom() {
        ArrayList<Item> inHamper = new ArrayList<>();
    
        Item tshirt = new Item("Dirty Metallica t-shirt", "", false);
        Item pant = new Item("Worn out jeans", "", false);
        Item top = new Item("Beige tank top", "", false);
        Item shirt = new Item("Crumpled blue button-down shirt", "", false);
    
        ArrayList<Item> laundryList = new ArrayList<>();
        laundryList.add(tshirt);
        laundryList.add(pant);
        laundryList.add(top);
        laundryList.add(shirt);
    
        System.out.println("As you walk into your room, you stumble on something — a " + tshirt.getName().toLowerCase() + ".");
        System.out.println("The floor is a mess. You spot:");
        for (Item item : laundryList) {
            System.out.println("- your " + item.getName().toLowerCase());
        }
        System.out.println("strewn across the floor. A broom watches silently from the closet door. You swear it wasn't there before.");
        System.out.println("\nType the name of an item to pick it up and put it in the hamper. You could ignore it, but that would be really lame.");
    
        while (true) {
            boolean broomUsed = false;
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.contains("pick up") || input.contains("put in hamper")) {
                System.out.println("What would you like to pick up?");
                input = scanner.nextLine().trim().toLowerCase();
            }

            if (input.contains("broom")) {
                if (broomUsed) {
                    System.out.println("The broom is already in the closet. You can't use it again.");
                    continue;
                }
                broomUsed = true;
                System.out.println("Whoosh - the broom suddenly zips through the air and under your bed. You hear frantic sweeping, and seconds later, the broom reemerges, and returns to the closet. It gently rests against the door, and it's like nothing happened. \nYou swear the floor sparkled a little just now.");
                player.updateSanity(5);
                showStatUpdate("sanity", 5);
                player.updateHealth(5);
                showStatUpdate("health", 5);
            }

            if (input.contains("ignore")) {
                System.out.println("You ignore the mess. The broom seems disappointed.");
                player.updateSanity(-10);
                showStatUpdate("sanity", -10);
                break;
            }
    
            if (input.equals("done")) {
                break;
            }
    
            boolean found = false;
            for (Item item : laundryList) {
                if (!item.isTaken() && item.getName().toLowerCase().contains(input)) {
                    inHamper.add(item);
                    item.setTaken(true);
                    System.out.println("You put the " + item.getName().toLowerCase() + " in the hamper.");
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                System.out.println("You don't see that item on the floor.");
            }
    
            // Optional: let them know how many items remain
            int remaining = 0;
            for (Item item : laundryList) {
                if (!item.isTaken()) remaining++;
            }
    
            if (remaining == 0) {
                System.out.println("You’ve cleaned up everything. The room looks almost liveable.");
                break;
            } 
        }
    
        // Final outcome
        int cleaned = inHamper.size();
        if (cleaned == laundryList.size()) {
            System.out.println("You feel strangely accomplished. Even the broom seems pleased.");
            player.updateSanity(+15);
            showStatUpdate("sanity", 15);
            player.updateHealth(+5);
            showStatUpdate("health", 5);

        } 
    
        System.out.println("You continue with your day.");
    }
    
    public void admissionsDay() {
        player.setRoom(outsideLocations.get(0));
        player.setBuilding(null);
    
        System.out.println("Welcome to Smiff College! You are a new student here, and you have just arrived on campus. Something is not quite - right about this place. You wonder what is in store for you.");
        System.out.println("You begin your journey at the admissions office. Enter the building and collect the first of your inventory.");
    
        String input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
    
            if (input.contains("enter") || input.contains("go to")) {
                moveTo(buildings.get("Admissions Office"));
                System.out.println("You have moved to the " + buildings.get("Admissions Office").getName() + ".");
                break;
            } else {
                System.out.println("Try typing: 'enter admissions office'.");
            }
        }
    
        System.out.println("\nYou step into the admissions lobby. The room smells faintly of printer ink and citrus-scented disinfectant. Straight ahead seems to be a reception room.");
        input = "";
    
        System.out.println("Behind the reception desk, a girl with a too-wide smile looks up from her clipboard.");
        System.out.println("\"You must be new! What’s your name?\"");
    
        System.out.print("> ");
        String name = scanner.nextLine().trim();
        player.setName(name);
    
        System.out.println("\n\"Nice to meet you, " + name + "!\"");
        System.out.println("She opens the top drawer of the desk and grabs an envelope, which she then hands to you.");
        System.out.println("\"Here’s your room key and ID card. You can scan your ID to enter any building on campus, so make sure you always have it on you.");
        System.out.println("If you have any questions, you can come back here and ask me at any time!\"\n");
        System.out.println("You take the envelope and open it. Inside, you find an ID card and a key. You put them in your pocket.");
    
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
    
            if (input.contains("look")) {
                System.out.println("You look around; " + player.getCurrentRoom().getDescription());
                System.out.println("The receptionist glances down at her computer, and her eyes widen.");
                System.out.println("\"I completely forgot about my meeting with my class dean! See you later, " + name + ".\"");
                System.out.println("Grabbing her backpack from the floor, she gives you a quick pat on the shoulder before running out the door.");
            } else if (input.contains("exit")) {
                player.setRoom(outsideLocations.get(0));
                player.setBuilding(null);
                break;
            } else {
                System.out.println("You can look around or exit the building.");
            }
        }
    
        System.out.println("You step out — into the pouring rain. Fat, grey clouds loom overhead, stretching out over the whole campus.");
        System.out.println("Oh well, guess you won’t be joining any outdoor sports clubs today.\n");
        System.out.println("Despite the weather, you can see that the campus is beautiful, in a very confusing way. None of the buildings match - in fact, together, they look like they were pulled out of a child's drawings. All around you, students bustle about, umbrellas and ponchos aplenty. \nYou feel a little lost, and a little exhilarated.");
    }    

    public void moveIn() {
        player.setRoom(outsideLocations.get(0));
        House newHouse = houses.get(0);
        player.setHouse(newHouse);

        System.out.println("As you admire the campus foliage, you hear a ping. You pull out your phone, and see an email from the housing office. It reads:");
        System.out.println("\"Dear " + player.getName() + ",\n" +
                "Welcome to Smiff College! We are pleased to inform you that you have been assigned to " + newHouse.getName() + ".\n" +
                "Your room is located on the first floor.\n" +
                "Please let us know if you have any questions or concerns.\n" +
                "Best,\n" +
                "The Housing Office\"");

        newHouse.setDescription("the one house with decentralized heating and cooling, leading to many roombound housemates. Also, some of the biggest party animals on campus. Good luck.");
       
        System.out.println("You look at your luggage, and then at the cobbled roads. You sigh, and begin to drag your suitcase down the path.");
        String input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            handleGlobalInput(input);
        
            if (player.getCurrentRoom().getName().contains(newHouse.getName())) {
                break;
            }
        }

        System.out.println("You admire the textured stone exterior of the house as you walk up the path. You climb up the stairs, and stand at the carved wooden door.");
        System.out.println("Welcome to "+ newHouse.getName() + "! This is possibly going to be the strangest part of your journey at Smiff College; you've been put in " + newHouse.getDescription());
        if (newHouse.hasDining()) {
            System.out.println("This house also has a dining hall: " + newHouse.getDiningHall().getDescription());
        }
        input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            
            if (input.contains("enter")) {
                player.setRoom(newHouse.getRooms().get(0));
                player.setBuilding(newHouse);
                System.out.println("You are now in " + newHouse.getName() + ".");
                break;
            } else {
                System.out.println("Try typing: 'enter house'.");
            }
        }

        System.out.println("You scan your ID by the door and it beeps. You push the door open, and tentatively step inside. Stood in a small foyer, you observe your new home: a large wooden staircase leads upstairs straight ahead, and to the left - you peek your head around - is a large, empty common room, with a few couches and a grand piano (nice, you think). To the right, is a sizeable kitchen, equipped with the dustiest appliances you have ever seen. You wonder why on earth that would be - you've already been warned about dining hall food by the internet.");
        System.out.println("You spot your room number on the door to the right of the staircase.");
        input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.contains("dorm room")) {
                for (Room room : newHouse.getRooms()) {
                    if (room.getName().contains("Dorm Room")) {
                        player.setRoom(room);
                        player.setBuilding(newHouse);
                        System.out.println("You are now in " + room.getName() + ".");
                        break;
                    }
                }
                break;
            }
        }

        System.out.println("Welcome to your dorm room, " + player.getCurrentRoom().getDescription());
    }

    public void quiz() {
        ArrayList<String> questions = new ArrayList<>();
        questions.add("1. What's the capital of France?");
        questions.add("2. How many years did I waste with her?");
        questions.add("3. If love is a line and time is a circle, and she left me on a Tuesday, what is the derivative of 5x² + 3x − 7?");
        questions.add("4. What is the answer to life, the universe, and everything?");
        questions.add("5. If John buys 27 bottles of alcohol today, and drinks 3 of them, then goes back and buys 12 more, how many bottles does he have?");
        questions.add("6. What is the opposite of love? (Please answer in one word. Hint: it's not hate. Trust me.)");
        questions.add("7. Where does my mother get her organic produce? (Please see me after class if you have ideas to convince her to buy extra for me.)");
        questions.add("8. I hate women. Not a question.");
        questions.add("9. Will she take me back if I apologize for running her cat over with my car? (There was a lot of blood.)");
        questions.add("10. Bonus Question: I spent my savings on my new car, and cannot afford to pay alimony. Will you chip in?");

        ArrayList<String> answers = new ArrayList<>();
        answers.add("paris");
        answers.add("12");
        answers.add("10x-3");
        answers.add("42");
        answers.add("36");
        answers.add("indifference");
        answers.add("trader joe's");
        answers.add("fair");
        answers.add("yes");
        answers.add("yes");

        ArrayList<String> userAnswers = new ArrayList<>();

        int score = 0;

        player.setRoom(player.getCurrentBuilding().getRoom("Dorm Room"));
        System.out.println("You are in " + player.getCurrentRoom().getName());

        System.out.println("Rise and shine! It's time for your first class. You are already running late, and you have no idea where the building is. No one really gave you a campus tour.");
        System.out.println("You pull out your phone and check your schedule. You are supposed to be in " + buildings.get("Bowie Labaratory").getName() + ", which is on the other side of campus. You have no idea how you are going to get there in time.");
        System.out.println("You grab your backpack and head for the door. (Hint: try typing 'leave room' to start.)");
        String input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            handleGlobalInput(input);
            if (player.getCurrentRoom().getName().contains("Bio Lab")) {
                break;
            }
        }
        System.out.println("You walk into the lab, and the smell of sweat hits your nostrils. You spot a group of volleyball players in jerseys by the air conditioner, messy haired and energized. Figures.\n" + //
                            "You walk to the far corner of the room and sit down at an empty desk. When you look up, you see your professor standing at the front of the room, looking disheveled and slightly unhinged. He is wearing a Hawaiian shirt and cargo shorts, and his hair is sticking up in all directions.");
        System.out.println("\"Surprise!\" his voice booms, startling you a bit. \"My wife filed for divorce today, and I think you all should suffer with me, so I have set a quiz for today's class. I don't actually expect any of you to know the answers, and that is what will bring me joy today.\"\n" + //
                            "He begins handing out papers, and when he gets to you, he stops and stares at you for a moment. You feel a sense of panic setting in; as he hands you the paper, he mutters under his breath, \"You look just like her,\" and a tear rolls down his cheek.\n" + //
                            "He retreats to his desk, and you hear quiet sobs begin to fill the room. You look down at the paper, and your heart sinks. The first question reads:\n");
        for (String question : questions) {
            System.out.println(question);
            input = scanner.nextLine().trim().toLowerCase();
            userAnswers.add(input);
        }
        System.out.println("You finish the quiz and hand it in. The professor looks at you with a mixture of confusion and snot. He pulls out a handkerchief from his pocket and loudly blows his nose before staring at you again. You don't know what to do; finally, he takes the paper from you.");
        System.out.println("You turn to sit back down, but he grabs your arm and whispers, \"Stay, please,\" and you feel absolutely stuck. Turning back to the professor, you smile awkwardly and nod. He seems satisfied, and begins grading your paper. Within a few seconds, he hands it back to you and says pensively, \"I train young minds. Maybe I'm too good for her.\" \nYou glance at your quiz:");
        System.out.println(score + "/" + questions.size());
        for (String question: questions) {
            System.out.println(question);
            if (userAnswers.get(questions.indexOf(question)).equals(answers.get(questions.indexOf(question)))) {
                score++;
                System.out.println("Your answer: " + userAnswers.get(questions.indexOf(question)) + " = CORRECT");
                System.out.println("Correct answer: " + answers.get(questions.indexOf(question)));
            } else {
                System.out.println("Your answer: " + userAnswers.get(questions.indexOf(question)) + " = INCORRECT");
                System.out.println("Correct answer: " + answers.get(questions.indexOf(question)));
            }
        }

        if (score >= 8) {
            System.out.println("You did really well on the quiz! The professor seems lowkey impressed, and you feel a sense of pride.");
            player.updateGPA(0.5);
            showStatUpdate("GPA", 0.5);
            player.updateSanity(15);
            showStatUpdate("sanity", 15);
        } else if (score >= 5) {
            System.out.println("You did okay on the quiz. The professor seems indifferent, and you feel a bit disappointed.");
            player.updateGPA(0.2);
            showStatUpdate("GPA", 0.2);
            player.updateSanity(10);
            showStatUpdate("sanity", 10);
        } else {
            System.out.println("You did pretty meh on the quiz. The professor seems unimpressed, and you feel a bit ashamed.");
            player.updateGPA(-0.1);
            showStatUpdate("GPA", -0.1);
            player.updateSanity(-10);
            showStatUpdate("sanity", -10);
        }
    }

    public void orderAtCafe(Cafe cafe) {
        player.setRoom(buildings.get("Campus Center").getRooms().get(0));
        System.out.println("As you approach the Campus Center cafe, you notice something strange as you look through the glass doors. There are no baristas, or student cashiers - in fact, no staff at all? As you reach the doors, you see why. Students are lined up in front of a row of robots, with coffee dispensers where their stomachs would be. You don’t know whether this is incredibly cool or lowkey terrifying.");
        String input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.contains("enter")) {
                moveTo(cafe);
                break;
            } else {
                System.out.println("Try typing: 'enter cafe'.");
            }
            System.out.println("You step inside and join the line. The robots are all silver and shiny, with uncomfortably bright eyes and hugely oversized hands. They look like something straight out of Star Wars, if the droids turned evil.");
            System.out.println("You finally make your way to the front of the line. You walk up to the robot and press the big red button above the dispenser area. Something whirs inside, and then the robot’s eyes light up bright white. A thin, metallic voice booms out.");
            System.out.println("Welcome to the Campus Center Cafe! Please begin by entering the number of shots of coffee you would like:");
            int coffeeShots = scanner.nextInt();
            System.out.println("Would you like sugar? If yes, enter the number of packets:");
            int sugar = scanner.nextInt();
            System.out.println("Would you like cream? If yes, enter the number of creams:");
            int cream = scanner.nextInt();
            cafe.takeOrder(coffeeShots, sugar, cream);
            System.out.println("Your order has been placed. Please wait while we prepare your drink.");
            System.out.println("Your drink is ready! Enjoy!");

            if (coffeeShots > 1) {
                    double change = 0;
                for (int i=2; i<=coffeeShots; i++) {
                    change-=2.5;
                    player.updateSanity(change);
                }
                System.out.println("You are buzzing with caffeine, and feel slightly off kilter.");
                showStatUpdate("sanity", change);
            }
            if (sugar > 2) {
                double change = 0;
                for (int i=3; i<=sugar; i++) {
                    change-=0.5;
                    player.updateHealth(change);
                }
                System.out.println("You feel a bit sick to your stomach from all the sugar.");
                showStatUpdate("health", change);
            }
            if (cream > 2) {
                double change = 0;
                for (int i=3; i<=cream; i++) {
                    change-=0.5;
                    player.updateHealth(change);
                }
                System.out.println("Your stomach gurgles uncomfortably loudly from all the cream in your coffee.");
                showStatUpdate("health", change);
            } 
            else {
                System.out.println("You are recharged and rejuvenated, just like the world intended for you. Life is good.");
                player.updateHealth(10);
                showStatUpdate("health", 10);
                player.updateSanity(10);
                showStatUpdate("sanity", 10);
            }
        }
    }

    public void squirrel() {
        player.setRoom(outsideLocations.get(0));
        player.setBuilding(null);

        System.out.println("It's a lovely day for a walk. You approach Chopin Lawn with the aspirations of a wide-eyed child, dying to soak in some sun and fresh air. You feel like a Victorian maiden allowed to roam the outside world once more. As you walk toward the tree at the center of the lawn, You see a bench nestled by it.");
        String input = "";
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
        
            if (input.contains("sit")) {
                System.out.println("You sit down on the bench and take a deep breath. The sun is shining, and the grass is green. You feel at peace.");
                break;
            } else if (input.contains("tree")) {
                System.out.println("You walk over to the tree and admire its beauty. The leaves are a vibrant green, and the bark is rough and textured. You feel a sense of calm wash over you.");
                break;
            } else {
                System.out.println("Try typing: 'sit on bench' or 'admire tree'.");
            }
        }
        System.out.println("As you get lost in the cloud patterns, a squirrel approaches you. It looks at you with beady eyes, and you can see its teeth glinting in the sunlight. You wonder if it is rabid.");
        scanner.nextLine();
       // if 
        Random rand = new Random();
        int chance = rand.nextInt(20);
        if (chance >= 10) {
           player.updateHealth(-20);
           player.updateSanity(-10);
            System.out.println("The squirrel makes intense eye contact with you while inching closer. Suddenly, it springs at your ankle and sinks its teeth in. You shake your leg aggressively, and it loses its grip and runs away.\n Your ankle throbs and you pull up the leg of your jeans to find blood dotting your sock. In an instant, the squirrel is back at your feet - it clambers up your pants and you freeze. The creature twitches its nose at you and dives into your bag, which you promptly drop on the grass. \nSeconds later, the squirrel emerges victorious, your ID card in mouth. It scampers off gleefully. \nYou feel a bit woozy.");
            showStatUpdate("health", -20);
            showStatUpdate("sanity", -10);
        } else {
            player.updateSanity(-10);
            System.out.println("The squirrel makes intense eye contact with you while inching closer. It stops in front of you and sniffs your shoe. \nYou stay still - after all, rabies are going around. The squirrel moves towards the bench and jumps up, climbing onto your bag. It sticks its head inside and emerges with your ID card in its mouth. \nIt turns to you and makes a screechy noise that sounds weirdly like laughter. \nIt jumps off the bench and runs away. You let out a breath, then realize your ID card is gone.");
            showStatUpdate("sanity", -10);
        } 
    }
    
    public void findIDCard() {
        ArrayList<String> prompts = new ArrayList<>();
        prompts.add("You hurry into the lobby and glance around; the receptionist isn't there, and there's a sticky note on the desk, saying, \"Gone to give a midterm, back in 3 hours.\" Except you don't know when she wrote it. \nYou look around the room, and spot a tuft of fur by the couches. You approach carefully, and slowly reach out and nudge it with your foot. You sigh with relief - it's just fur. \n\nExcept = why would there be fur inside?\n\nYou throw another look around the room, and a glint from between the couch cushions catches your eye. You reach down, and pull out a set of keys. You turn them over in your hand, but they have no label. \nYou put them back down, and your hand brushes against something cold and flat. Your ID card. You pull it out and let out an involuntary yelp as you realize it is covered in squirrel saliva. Gross. You wipe it on your jeans and put it in your pocket.");
        prompts.add("You scan the perimeter of the lawn, but all you see is green. With a sign, you begin carefully treading over the grass, keeping an eye out for anything out of the ordinary. \nSuddenly, you hear a soft crunch below your shoe, and lift your foot to find your ID card slightly bent and mucky. You pick it up and look around, before realizing that the lawn is empty. You wipe it on your jeans and put it in your pocket.");
        prompts.add("As you near Troye House, you spot a squirrel sitting on the porch. It looks at you with beady eyes, and you can see its teeth glinting in the sunlight. You wonder if it is rabid. \nYou take a step back, and it jumps off the porch and runs away. You look down, and see your ID card lying on the ground. You pick it up and wipe it on your jeans before putting it in your pocket.");



        ArrayList<Location> hidingSpots = new ArrayList<>();
        hidingSpots.add(buildings.get("Admissions Office"));
        hidingSpots.add(outsideLocations.get(0));
        for (Location location: outsideLocations) {
            if (location.getName().contains("outside Troye")) {
                hidingSpots.add(location);
            }
        }

        Random rand = new Random();
        Location hidingSpot = hidingSpots.get(rand.nextInt(hidingSpots.size()));

        System.out.println("Time to try and figure out where that demonic squirrel stashed - you pray, stashed - your ID card. (Hint: try the admissions office.)");
        System.out.print("> ");
        String input = scanner.nextLine().trim().toLowerCase();
        handleGlobalInput(input);
        while (player.getCurrentRoom() != hidingSpot) {
            if (player.getCurrentRoom().getName().contains("Admissions Office")) {
                System.out.println("You walk around the admissions office, looking for your ID card. You check the reception desk, but it's empty. You check the couches, but they're just couches. You check the parking lot, but it's just a parking lot. You check the dumpster, but sadly, it's just a dumpster. \n" + //
                                        "Just as you're dejectedly walking out the door, you spot a flyer on a pillar outside. There's a giant squirrel on it, and it reads, \n" + //
                                        "Squirrels are terrorizing this campus. We must unite.\n" + //
                                        "if u hv lost stuff check lawn undr tree");
            } else if (player.getCurrentRoom().getName().contains("Lawn")) {
                System.out.println("You carefully tread over the grass, scanning the ground for any sign of the card. You walk the length of the lawn, but come up empty handed. You stop by the bench and sit down, staring at the tree. \nSurely it must be nearby.");
            } else if (player.getCurrentRoom().getName().contains("Troye")) {
                System.out.println("As you approach Troye House, you spot a squirrel sitting on the porch. It sneers at you and hisses, before running off with your ID card in hand. Ridiculous.");
            }
            
            else {
                System.out.println("You scan the area, but find nothing. Guess the squirrel went somewhere else.");
                System.out.print("> ");
                input = scanner.nextLine().trim().toLowerCase();
            }
        }
        if (hidingSpot.getName().contains("Admissions Office")) {
            System.out.println(prompts.get(0));
        } else if (hidingSpot.getName().contains("Lawn")) {
            System.out.println(prompts.get(1));
        } else if (hidingSpot.getName().contains("Troye")) {
            System.out.println(prompts.get(2));
        }
        player.addItem(hidingSpot.getItems().get(0));
        hidingSpot.getItems().get(0).setTaken(true);
    }

    public boolean isSurviving() {
        if (player.getHealth() <= 0 || player.getGPA() <= 2.0 || player.getSanity() <= 0) {
            return false;
        } 
        else if (player.getGPA() >= 4.0) {
            System.out.println("You are currently a perfect student with a 4.0 GPA! Congratulations! Don't let it go to your head.");
        } else if (player.getHealth() >= 100) {
            System.out.println("You are currently in perfect health! Congratulations! You might be the only one here, considering the dining hall food.");
        } else if (player.getSanity() >= 100) {
            System.out.println("You are currently completely sane! Congratulations! Hold on to that while you can.");
        }
        return true;
    }

    public void handleWin() {
        if (player.getGPA() >= 4.0) {
            System.out.println("You survived the semester and finished with a perfect GPA! Your dean approaches you and hands you a wad of cash. \"Congratulations, " + player.getName() + "! You  are the lucky winner of the Admission Office's annual 'Special Case' award!\"" + //
            "It feels illegal, but you take the money anyway. You are now a successful survivor of the Smiff experience.");
        } else if (player.getGPA() >= 3.0) {
            System.out.println("A solid academic run! You get a recommendation letter signed by three professors, and a handwritten note: \"Please never come back.\"");
        } else if (player.getGPA() >= 2.0) {
            System.out.println("You scraped by academically. You're handed...  a brochure for a gap year meditation retreat, and a year's subscription to cable news.");
        } 
        if (player.getSanity() >= 65) {
            System.out.println("🧠 You held on to your sanity! A true Smiff miracle. You receive a brain-shaped plushie and a therapist's personal thank-you note.");
        } else if (player.getSanity() >= 40) {
            System.out.println("😵 You made it with most of your mind intact. You're mildly twitchy, but still functional (ish). What an experience.");
        } else {
            System.out.println("💥 Your sanity is one step away from being a legal issue. The school counselor mails you a coupon for scented candles and a handwritten apology.");
        }
    
        if (player.getHealth() >= 35) {
            System.out.println("💪 You're in peak physical condition! For Smiff College standards. The gym coach invites you to co-author their wellness zine and your picture makes it to the newsletter for 'Participation Award of the Month'.");
        } else if (player.getHealth() >= 20) {
            System.out.println("🤒 You survived on dining hall coffee, Taco Bell and the remains of your dreams. We've all been there. At least you made it out alive!");
        } else {
            System.out.println("☠️ You are one step away from being medically escorted off campus. The nurse who has treated all 17 of your colds mutters, \"Maybe I have reached retirement age.\"");
        }
        System.exit(0);
    }

    public void handleDeath() {
        if (player.getHealth()<=0) {
            System.out.println("You suddenly feel your legs buckling and fall to the ground, your vision blurry. You hear a voice mutter, \"Another one bites the dust,\" before everything goes dark. \nYou awaken to find yourself on the Admissions Office floor, swaddled in blankets, as the Admissions Office receptionist spoons applesauce into your mouth. \"Hey little guy, you're okay.\" she coos, \"I think you were just low on sugar. I'm going to make you all better!\" You can't shake the feeling that the world somehow got bigger - the desk towers over you. \nYou catch sight of your reflection in the glossy floor - beady black eyes, a stubby nose and twitching whiskers stare back in horror. You scream, but all that comes out is a high-pitched squeak. You are a squirrel, doomed to harrass Smiff students for all eternity.");
            System.out.println("\033[0;1mGAME OVER\033[0;0m (health = 0)");
            System.exit(0);
        } else if (player.getSanity()<=0) {
            System.out.println("The world around you begins to change colors, and you feel a strange sensation in your feet. The ground seems to disappear and your vision swims with letters and numbers. You blink, and suddenly you're in an empty white room, with no doors or windows. The only sign on the wall reads:\n" + //
                                "SIGN IN TO YOUR ACCOUNT \nEmail: \nPassword: \n" + //
                                "You reach out; a floating keyboard materializes under your fingers. You type in your credentials, and the sign is replaced with a large banner, reading, \n\033[3mWelCoMe To MOOdLe @ SmIFf\033[0m\nSigns appear all around you, flashing new assignments, incoming grades, and endless discussion threads. You have been sucked into the Moodle vortex.\n" + //
                                "\n\033[0;1mGAME OVER\033[0;0m (sanity = 0)");
            System.exit(0);
        } else {
            System.out.println("Out of nowhere, hands grab you from behind and someone throws a sack over your head. Legs flailing and arms swinging wildly at your attackers, you are dragged across campus until reaching a dimly lit room. The sack is pulled off, and you see your dean staring at you from behind a desk. \"You have been reassigned,\" she states mysteriously. “Do you accept your fate, or would you like to appeal via interpretive dance?”");
            System.out.println("You stand up, and the floor opens up beneath you. You fall into a dingier basement, filled with empty storage bins and dusty electrical appliances. Footsteps echo. From the shadows emerges a Res Life staff member you’ve never seen before.\n" + //
                                "“Welcome,” they say solemnly. They hand you a clipboard. Your name is on it. So is your new title:\n" + //
                                "Assistant Coordinator of Absolutely Nothing\n" + //
                                "The printer in the corner begins warming up. It prints the words:\n" + //
                                "Next time: Use Office Hours.");
            System.out.println("\\033[0;1mGAME OVER\\033[0;0m (GPA = 0)");
            System.exit(0);
        }
    }

    public void showStats() {
        System.out.println("Name: " + player.getName());
        System.out.println("Current GPA = " + player.getGPA());
        System.out.println("Current Sanity = " + player.getSanity());
        System.out.println("Current Health = " + player.getHealth());
    }

    public void printHelpText() {
        try {
            File file = new File("cheatsheet.md"); // adjust path if needed
            Scanner fileScanner = new Scanner(file);
            int skipLines = 8;
            int currentLine = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (currentLine++ >= skipLines) {
                    System.out.println(line);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Help file not found.");
        }
    }

    /* Displays statistics to player.
     * 
     * @param stat the statistic to be displayed.
     * @param change the amount the statistic is updated by.
     */
    public void showStatUpdate(String statName, double change) {
        String sign = (change > 0) ? "+" : "";
        System.out.println(sign + change + statName);
    }

    public void quitGame() {
        System.out.println("Thanks for playing! Goodbye!");
        scanner.close();
        System.exit(0);
    }
    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.startGame();
    }


}
