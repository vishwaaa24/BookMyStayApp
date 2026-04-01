import java.util.*;

/**
 * BookMyStayApp - UC5: Booking Request (First-Come-First-Served)
 * BookMyStayApp - UC6: Reservation Confirmation & Room Allocation
 * @author [Your Name]
 * @version 5.0
 * @version 6.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 5.0             ");
        System.out.println("   Application Version: 6.0             ");
        System.out.println("========================================");

        // UC5: Initialize the Booking Request Queue
        // Setup Inventory (UC3)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Suite Room", 1);

        // Setup Request Queue (UC5)
        BookingRequestQueue intakeQueue = new BookingRequestQueue();
        intakeQueue.addRequest(new Reservation("Alice", "Single Room"));
        intakeQueue.addRequest(new Reservation("Bob", "Single Room"));
        intakeQueue.addRequest(new Reservation("Charlie", "Single Room")); // This should fail (only 2 left)
        intakeQueue.addRequest(new Reservation("David", "Suite Room"));

        System.out.println("\n--- Receiving Incoming Booking Requests ---");
        // UC6: Initialize Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Simulating guests submitting requests
        intakeQueue.addRequest(new Reservation("Guest_Alice", "Suite Room"));
        intakeQueue.addRequest(new Reservation("Guest_Bob", "Single Room"));
        intakeQueue.addRequest(new Reservation("Guest_Charlie", "Double Room"));
        System.out.println("\n--- Starting Room Allocation Process ---");

        // Displaying the queued requests in arrival order
        intakeQueue.displayQueue();
        // Process the queue until empty
        while (!intakeQueue.isEmpty()) {
            Reservation currentRequest = intakeQueue.getNextRequest();
            bookingService.processAllocation(currentRequest);
        }

        System.out.println("\nStatus: All requests are queued and waiting for processing.");
        System.out.println("Note: No inventory has been modified yet.");
        // Display Final State
        bookingService.displayAllocations();
    }
}

/**
 * UC5: Reservation Class
 * Represents a Guest's intent to book a specific room type.
 * UC6: BookingService Class
 * Handles allocation logic and prevents double booking using Sets.
 */
class Reservation {
    private String guestName;
    private String requestedRoomType;
    class BookingService {
        private RoomInventory inventory;
        // Maps Room Type -> Set of Unique Room IDs assigned
        private Map<String, Set<String>> allocatedRooms;
        private int roomCounter = 100; // To generate unique IDs

        public Reservation(String guestName, String requestedRoomType) {
            this.guestName = guestName;
            this.requestedRoomType = requestedRoomType;
    public BookingService(RoomInventory inventory) {
                this.inventory = inventory;
                this.allocatedRooms = new HashMap<>();
            }

            @Override
            public String toString() {
                return "Request [Guest: " + guestName + " | Room: " + requestedRoomType + "]";
            }
        }
        public void processAllocation(Reservation res) {
            String type = res.getRequestedRoomType();
            int available = inventory.getAvailability(type);

/**
 * UC5: BookingRequestQueue Class
 * Uses a LinkedList-based Queue to maintain FIFO order.
 */
            class BookingRequestQueue {
                // Queue preserves the order of arrival (First-In, First-Out)
                private Queue<Reservation> requestQueue;
        if (available > 0) {
                    // Generate unique Room ID (e.g., Single-101)
                    String roomID = type.substring(0, 1) + (++roomCounter);

    public BookingRequestQueue() {
                        this.requestQueue = new LinkedList<>();
                    }
                    // Ensure the set exists for this room type
                    allocatedRooms.putIfAbsent(type, new HashSet<>());

                    // Add request to the back of the line
                    public void addRequest(Reservation request) {
                        requestQueue.add(request);
                        System.out.println("Intake: Received " + request);
                        // Add to Set (Prevents Double Booking)
                        if (allocatedRooms.get(type).add(roomID)) {
                            inventory.updateAvailability(type, available - 1);
                            System.out.println("CONFIRMED: " + res.getGuestName() + " assigned to " + roomID);
                        }
                    } else {
                        System.out.println("REJECTED: No availability for " + res.getGuestName() + " (" + type + ")");
                    }
                }

                // Display all waiting requests
                public void displayQueue() {
                    System.out.println("\n--- Current Booking Queue (FIFO Order) ---");
                    if (requestQueue.isEmpty()) {
                        System.out.println("Queue is empty.");
                    } else {
                        for (Reservation res : requestQueue) {
                            System.out.println(res);
                        }
                        public void displayAllocations() {
                            System.out.println("\n--- Final Allocation Report ---");
                            for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
                                System.out.println(entry.getKey() + " Assignments: " + entry.getValue());
                            }
                        }
                    }
                }

/** * Note: Ensure your Reservation class from UC5 has these getter methods:
 * public String getGuestName() { return guestName; }
 * public String getRequestedRoomType() { return requestedRoomType; }
 * * And BookingRequestQueue needs:
 * public Reservation getNextRequest() { return requestQueue.poll(); }
 * public boolean isEmpty() { return requestQueue.isEmpty(); }
 */