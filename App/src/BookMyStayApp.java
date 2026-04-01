import java.util.*;

/**
 * BookMyStayApp - UC6: Reservation Confirmation & Room Allocation
 * BookMyStayApp - UC7: Add-On Service Selection
 * @author [Your Name]
 * @version 6.0
 * @version 7.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 6.0             ");
        System.out.println("   Application Version: 7.0             ");
        System.out.println("========================================");

        // Setup Inventory (UC3)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Suite Room", 1);
        // UC7: Initialize Add-On Manager
        AddOnManager addonManager = new AddOnManager();

        // Setup Request Queue (UC5)
        BookingRequestQueue intakeQueue = new BookingRequestQueue();
        intakeQueue.addRequest(new Reservation("Alice", "Single Room"));
        intakeQueue.addRequest(new Reservation("Bob", "Single Room"));
        intakeQueue.addRequest(new Reservation("Charlie", "Single Room")); // This should fail (only 2 left)
        intakeQueue.addRequest(new Reservation("David", "Suite Room"));
        // Assume we have a Reservation ID from UC6 (e.g., "S101")
        String reservationId = "S101";

        // UC6: Initialize Booking Service
        BookingService bookingService = new BookingService(inventory);
        System.out.println("\n--- Selecting Add-On Services for Reservation: " + reservationId + " ---");

        System.out.println("\n--- Starting Room Allocation Process ---");
        // Guest selects services
        addonManager.addServiceToReservation(reservationId, new Service("Breakfast", 500.0));
        addonManager.addServiceToReservation(reservationId, new Service("Late Checkout", 1000.0));
        addonManager.addServiceToReservation(reservationId, new Service("WiFi Plus", 200.0));

        // Process the queue until empty
        while (!intakeQueue.isEmpty()) {
            Reservation currentRequest = intakeQueue.getNextRequest();
            bookingService.processAllocation(currentRequest);
        }

        // Display Final State
        bookingService.displayAllocations();
        // Display services and total extra cost
        addonManager.displayServicesForReservation(reservationId);
    }
}

/**
 * UC6: BookingService Class
 * Handles allocation logic and prevents double booking using Sets.
 * UC7: Service Class
 * Represents an optional offering with a name and price.
 */
class BookingService {
    private RoomInventory inventory;
    // Maps Room Type -> Set of Unique Room IDs assigned
    private Map<String, Set<String>> allocatedRooms;
    private int roomCounter = 100; // To generate unique IDs

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        class Service {
            private String name;
            private double price;

            public Service(String name, double price) {
                this.name = name;
                this.price = price;
            }

            public void processAllocation(Reservation res) {
                String type = res.getRequestedRoomType();
                int available = inventory.getAvailability(type);
                public String getName() { return name; }
                public double getPrice() { return price; }

                if (available > 0) {
                    // Generate unique Room ID (e.g., Single-101)
                    String roomID = type.substring(0, 1) + (++roomCounter);
                    @Override
                    public String toString() {
                        return name + " (Rs. " + price + ")";
                    }
                }

                // Ensure the set exists for this room type
                allocatedRooms.putIfAbsent(type, new HashSet<>());
/**
 * UC7: AddOnManager Class
 * Uses Map<String, List<Service>> to link one reservation to many services.
 */
                class AddOnManager {
                    // Reservation ID -> List of selected services
                    private Map<String, List<Service>> reservationAddOns;

                    // Add to Set (Prevents Double Booking)
            if (allocatedRooms.get(type).add(roomID)) {
                        inventory.updateAvailability(type, available - 1);
                        System.out.println("CONFIRMED: " + res.getGuestName() + " assigned to " + roomID);
                    }
                } else {
                    System.out.println("REJECTED: No availability for " + res.getGuestName() + " (" + type + ")");
                }
    public AddOnManager() {
                    this.reservationAddOns = new HashMap<>();
                }

                public void displayAllocations() {
                    System.out.println("\n--- Final Allocation Report ---");
                    for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
                        System.out.println(entry.getKey() + " Assignments: " + entry.getValue());
                    }
                    public void addServiceToReservation(String resId, Service service) {
                        // If the reservation doesn't have a list yet, create one
                        reservationAddOns.putIfAbsent(resId, new ArrayList<>());
                        reservationAddOns.get(resId).add(service);
                        System.out.println("Added: " + service.getName() + " to " + resId);
                    }
                }

/** * Note: Ensure your Reservation class from UC5 has these getter methods:
 * public String getGuestName() { return guestName; }
 * public String getRequestedRoomType() { return requestedRoomType; }
 * * And BookingRequestQueue needs:
 * public Reservation getNextRequest() { return requestQueue.poll(); }
 * public boolean isEmpty() { return requestQueue.isEmpty(); }
 */
                public void displayServicesForReservation(String resId) {
                    List<Service> services = reservationAddOns.get(resId);

                    if (services == null || services.isEmpty()) {
                        System.out.println("No add-ons selected for " + resId);
                        return;
                    }

                    System.out.println("\nSummary for " + resId + ":");
                    double totalCost = 0;
                    for (Service s : services) {
                        System.out.println("- " + s);
                        totalCost += s.getPrice();
                    }
                    System.out.println("Total Add-On Cost: Rs. " + totalCost);
                }
            }