@@ -2,92 +2,91 @@
        import java.util.Map;

/**
 * BookMyStayApp - UC3: Centralized Room Inventory Management
 * BookMyStayApp - UC4: Room Search & Availability Check
 * @author [Your Name]
 * @version 3.0
 * @version 4.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 3.0             ");
        System.out.println("   Application Version: 4.0             ");
        System.out.println("========================================");

        // UC3: Initialize Centralized Inventory
        // Setup Inventory (from UC3)
        RoomInventory inventory = new RoomInventory();

        // Registering Room Types with initial counts
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Double Room", 0); // Sold out for testing
        inventory.addRoomType("Suite Room", 2);

        // Display Current Inventory
        System.out.println("\n--- Initial Room Inventory ---");
        inventory.displayInventory();
        // Setup Room Details (from UC2)
        Map<String, Room> roomTemplates = new HashMap<>();
        roomTemplates.put("Single Room", new SingleRoom("SR-Basic", 1500.0));
        roomTemplates.put("Double Room", new DoubleRoom("DR-Deluxe", 2500.0));
        roomTemplates.put("Suite Room", new SuiteRoom("SU-Luxury", 5000.0));

        // Demonstrate a controlled update (e.g., a booking happens)
        System.out.println("\nAction: Booking 1 Double Room...");
        inventory.updateAvailability("Double Room", 2); // Updating count to 2
        // UC4: Room Search Service (Read-Only)
        SearchService searchService = new SearchService(inventory, roomTemplates);

        // Display Inventory after update
        System.out.println("\n--- Updated Room Inventory ---");
        inventory.displayInventory();
        System.out.println("\nGuest is searching for available rooms...");
        searchService.displayAvailableRooms();
    }
}

/**
 * UC3: RoomInventory Class
 * Manages availability using a HashMap for O(1) lookup.
 * UC4: SearchService Class
 * Reinforces safe data access by only reading inventory, not modifying it.
 */
class RoomInventory {
    // HashMap acts as the Single Source of Truth
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }
    class SearchService {
        private RoomInventory inventory;
        private Map<String, Room> roomTemplates;

        // Method to register/add room types
        public void addRoomType(String type, int count) {
            inventory.put(type, count);
    public SearchService(RoomInventory inventory, Map<String, Room> roomTemplates) {
                this.inventory = inventory;
                this.roomTemplates = roomTemplates;
            }

            // Method to get current availability
            public int getAvailability(String type) {
                return inventory.getOrDefault(type, 0);
            }

            // Controlled update of room availability
            public void updateAvailability(String type, int newCount) {
                if (inventory.containsKey(type)) {
                    inventory.put(type, newCount);
                    public void displayAvailableRooms() {
                        System.out.println("--- Search Results: Available Options ---");
                        boolean found = false;

                        for (String type : roomTemplates.keySet()) {
                            int count = inventory.getAvailability(type);

                            // Defensive Programming: Filter out unavailable rooms
                            if (count > 0) {
                                Room details = roomTemplates.get(type);
                                details.displayDetails();
                                System.out.println("Status: " + count + " rooms available");
                                System.out.println("------------------------------------");
                                found = true;
                            }
                        }
                    }

                    // Display all inventory status
                    public void displayInventory() {
                        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
                            if (!found) {
                                System.out.println("No rooms are currently available.");
                            }
                        }
                    }

// --- Keep your Room classes from UC2 below ---
// --- Previous UC Classes (RoomInventory, Room, etc.) remain below ---
                    class RoomInventory {
                        private Map<String, Integer> inventory = new HashMap<>();
                        public void addRoomType(String type, int count) { inventory.put(type, count); }
                        public int getAvailability(String type) { return inventory.getOrDefault(type, 0); }
                    }

                    abstract class Room {
                        private String roomNumber;
                        private String type;
                        private String roomNumber, type;
                        private double price;

                        public Room(String roomNumber, String type, double price) {
                            this.roomNumber = roomNumber;
                            this.type = type;
                            this.price = price;
                        }

                        public Room(String rn, String t, double p) { this.roomNumber = rn; this.type = t; this.price = p; }
                        public void displayDetails() {
                            System.out.println("Room: [" + roomNumber + "] | Type: " + type + " | Price: Rs." + price);
                            System.out.println("Type: " + type + " | Price: Rs." + price);
                        }
                    }
// (SingleRoom, DoubleRoom, SuiteRoom classes remain the same as UC2)

                    class SingleRoom extends Room { public SingleRoom(String rn, double p) { super(rn, "Single Room", p); } }
                    class DoubleRoom extends Room { public DoubleRoom(String rn, double p) { super(rn, "Double Room", p); } }
                    class SuiteRoom extends Room { public SuiteRoom(String rn, double p) { super(rn, "Suite Room", p); } }