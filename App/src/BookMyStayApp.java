import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp - UC2: Basic Room Types & Static Availability
 * BookMyStayApp - UC3: Centralized Room Inventory Management
 * @author [Your Name]
 * @version 2.0
 * @version 3.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 2.0             ");
        System.out.println("   Application Version: 3.0             ");
        System.out.println("========================================");

        // UC2: Domain Modeling & Static Availability
        // UC3: Initialize Centralized Inventory
        RoomInventory inventory = new RoomInventory();

        // 1. Defining Room Objects (Polymorphism)
        Room single = new SingleRoom("S101", 1500.0);
        Room doubleRm = new DoubleRoom("D201", 2500.0);
        Room suite = new SuiteRoom("SU301", 5000.0);
        // Registering Room Types with initial counts
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // 2. Static Availability Representation
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;
        // Display Current Inventory
        System.out.println("\n--- Initial Room Inventory ---");
        inventory.displayInventory();

        // 3. Displaying Room Details
        System.out.println("\n--- Available Room Types ---");
        displayRoomInfo(single, singleRoomAvailability);
        displayRoomInfo(doubleRm, doubleRoomAvailability);
        displayRoomInfo(suite, suiteRoomAvailability);
    }
    // Demonstrate a controlled update (e.g., a booking happens)
        System.out.println("\nAction: Booking 1 Double Room...");
        inventory.updateAvailability("Double Room", 2); // Updating count to 2

    // Helper method to display room details
    public static void displayRoomInfo(Room room, int availability) {
        room.displayDetails();
        System.out.println("Availability: " + availability + " rooms left");
        System.out.println("------------------------------------");
        // Display Inventory after update
        System.out.println("\n--- Updated Room Inventory ---");
        inventory.displayInventory();
    }
}

// --- UC2: OBJECT MODELING (Classes below) ---
/**
 * UC3: RoomInventory Class
 * Manages availability using a HashMap for O(1) lookup.
 */
class RoomInventory {
    // HashMap acts as the Single Source of Truth
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    // Method to register/add room types
    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Method to get current availability
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Controlled update of room availability
    public void updateAvailability(String type, int newCount) {
        if (inventory.containsKey(type)) {
            inventory.put(type, newCount);
        }
    }

    // Abstract Class - Enforces a consistent structure
    // Display all inventory status
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

// --- Keep your Room classes from UC2 below ---
abstract class Room {
    private String roomNumber;
    private String type;
    @@ -53,27 +86,8 @@ public Room(String roomNumber, String type, double price) {
        this.price = price;
    }

    // Encapsulation: Using a method to display protected data
    public void displayDetails() {
        System.out.println("Room: [" + roomNumber + "] | Type: " + type + " | Price: Rs." + price);
    }
}

// Inheritance: Concrete room classes
class SingleRoom extends Room {
    public SingleRoom(String roomNumber, double price) {
        super(roomNumber, "Single Room", price);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(String roomNumber, double price) {
        super(roomNumber, "Double Room", price);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(String roomNumber, double price) {
        super(roomNumber, "Suite Room", price);
    }
}