import java.util.HashMap;
import java.util.Map;
import java.util.*;

/**
 * BookMyStayApp - UC4: Room Search & Availability Check
 * BookMyStayApp - UC5: Booking Request (First-Come-First-Served)
 * @author [Your Name]
 * @version 4.0
 * @version 5.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 4.0             ");
        System.out.println("   Application Version: 5.0             ");
        System.out.println("========================================");

        // Setup Inventory (from UC3)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0); // Sold out for testing
        inventory.addRoomType("Suite Room", 2);
        // UC5: Initialize the Booking Request Queue
        BookingRequestQueue intakeQueue = new BookingRequestQueue();

        // Setup Room Details (from UC2)
        Map<String, Room> roomTemplates = new HashMap<>();
        roomTemplates.put("Single Room", new SingleRoom("SR-Basic", 1500.0));
        roomTemplates.put("Double Room", new DoubleRoom("DR-Deluxe", 2500.0));
        roomTemplates.put("Suite Room", new SuiteRoom("SU-Luxury", 5000.0));
        System.out.println("\n--- Receiving Incoming Booking Requests ---");

        // UC4: Room Search Service (Read-Only)
        SearchService searchService = new SearchService(inventory, roomTemplates);
        // Simulating guests submitting requests
        intakeQueue.addRequest(new Reservation("Guest_Alice", "Suite Room"));
        intakeQueue.addRequest(new Reservation("Guest_Bob", "Single Room"));
        intakeQueue.addRequest(new Reservation("Guest_Charlie", "Double Room"));

        System.out.println("\nGuest is searching for available rooms...");
        searchService.displayAvailableRooms();
        // Displaying the queued requests in arrival order
        intakeQueue.displayQueue();

        System.out.println("\nStatus: All requests are queued and waiting for processing.");
        System.out.println("Note: No inventory has been modified yet.");
    }
}

/**
 * UC4: SearchService Class
 * Reinforces safe data access by only reading inventory, not modifying it.
 * UC5: Reservation Class
 * Represents a Guest's intent to book a specific room type.
 */
class SearchService {
    private RoomInventory inventory;
    private Map<String, Room> roomTemplates;
    class Reservation {
        private String guestName;
        private String requestedRoomType;

        public SearchService(RoomInventory inventory, Map<String, Room> roomTemplates) {
            this.inventory = inventory;
            this.roomTemplates = roomTemplates;
    public Reservation(String guestName, String requestedRoomType) {
                this.guestName = guestName;
                this.requestedRoomType = requestedRoomType;
            }

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

                if (!found) {
                    System.out.println("No rooms are currently available.");
                }
                @Override
                public String toString() {
                    return "Request [Guest: " + guestName + " | Room: " + requestedRoomType + "]";
                }
            }

// --- Previous UC Classes (RoomInventory, Room, etc.) remain below ---
            class RoomInventory {
                private Map<String, Integer> inventory = new HashMap<>();
                public void addRoomType(String type, int count) { inventory.put(type, count); }
                public int getAvailability(String type) { return inventory.getOrDefault(type, 0); }
            }
/**
 * UC5: BookingRequestQueue Class
 * Uses a LinkedList-based Queue to maintain FIFO order.
 */
            class BookingRequestQueue {
                // Queue preserves the order of arrival (First-In, First-Out)
                private Queue<Reservation> requestQueue;

                abstract class Room {
                    private String roomNumber, type;
                    private double price;
                    public Room(String rn, String t, double p) { this.roomNumber = rn; this.type = t; this.price = p; }
                    public void displayDetails() {
                        System.out.println("Type: " + type + " | Price: Rs." + price);
    public BookingRequestQueue() {
                            this.requestQueue = new LinkedList<>();
                        }

                        // Add request to the back of the line
                        public void addRequest(Reservation request) {
                            requestQueue.add(request);
                            System.out.println("Intake: Received " + request);
                        }
                    }

                    class SingleRoom extends Room { public SingleRoom(String rn, double p) { super(rn, "Single Room", p); } }
                    class DoubleRoom extends Room { public DoubleRoom(String rn, double p) { super(rn, "Double Room", p); } }
                    class SuiteRoom extends Room { public SuiteRoom(String rn, double p) { super(rn, "Suite Room", p); } }
                    // Display all waiting requests
                    public void displayQueue() {
                        System.out.println("\n--- Current Booking Queue (FIFO Order) ---");
                        if (requestQueue.isEmpty()) {
                            System.out.println("Queue is empty.");
                        } else {
                            for (Reservation res : requestQueue) {
                                System.out.println(res);
                            }
                        }
                    }
                }