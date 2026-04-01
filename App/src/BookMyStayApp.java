import java.util.*;

/**
 * BookMyStayApp - UC10: Booking Cancellation & Inventory Rollback
 * BookMyStayApp - UC11: Concurrent Booking Simulation (Thread Safety)
 * @author [Your Name]
 * @version 10.0
 * @version 11.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 10.0            ");
        System.out.println("   UC11: Multi-threaded Simulation      ");
        System.out.println("========================================");

        // Setup Inventory (UC3)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Suite Room", 2);
        inventory.addRoomType("Deluxe Room", 1); // Only ONE room left!

        // Setup Cancellation Service
        CancellationService cancellationService = new CancellationService(inventory);
        // Creating two threads (Users) trying to book the SAME room at the SAME time
        Runnable bookingTask1 = () -> processConcurrentBooking("User_Alice", "Deluxe Room", inventory);
        Runnable bookingTask2 = () -> processConcurrentBooking("User_Bob", "Deluxe Room", inventory);

        // Simulating a booking that we might want to cancel
        String roomID = "SU-505";
        System.out.println("\n--- Initial State ---");
        System.out.println("Suite Availability: " + inventory.getAvailability("Suite Room"));
        Thread thread1 = new Thread(bookingTask1);
        Thread thread2 = new Thread(bookingTask2);

        // UC10: Performing a Cancellation (Rollback)
        System.out.println("\nAction: Guest cancels reservation for " + roomID);
        cancellationService.processCancellation("Suite Room", roomID);

        System.out.println("\n--- Final State ---");
        System.out.println("Suite Availability: " + inventory.getAvailability("Suite Room"));
        cancellationService.displayRollbackHistory();
    }
}

/**
 * UC10: CancellationService Class
 * Uses a Stack to track released room IDs for LIFO rollback behavior.
 */
class CancellationService {
    private RoomInventory inventory;
    // Stack tracks room IDs in the order they are cancelled
    private Stack<String> releasedRoomsStack;

    public CancellationService(RoomInventory inventory) {
        this.inventory = inventory;
        this.releasedRoomsStack = new Stack<>();
        System.out.println("\nSimulating simultaneous booking attempts...");
        thread1.start();
        thread2.start();
    }

    public void processCancellation(String type, String roomID) {
        // 1. Validation: Ensure the roomID is not null/empty
        if (roomID == null || roomID.isEmpty()) {
            System.err.println("Error: Invalid Room ID for cancellation.");
            return;
        }

        // 2. Rollback Logic: Add to Stack (LIFO)
        releasedRoomsStack.push(roomID);
        /**
         * Using 'synchronized' to ensure only one thread can execute this logic at once.
         * This prevents the "Double Booking" race condition.
         */
        public static synchronized void processConcurrentBooking(String guest, String type, RoomInventory inv) {
            int available = inv.getAvailability(type);

            // 3. Inventory Restoration: Increment count
            int currentCount = inventory.getAvailability(type);
            inventory.updateAvailability(type, currentCount + 1);
            System.out.println(guest + " is checking availability... found: " + available);

            System.out.println("Success: Room " + roomID + " rolled back to inventory.");
        }
        if (available > 0) {
            // Simulating a small processing delay
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            public void displayRollbackHistory() {
                System.out.println("\n--- Room Rollback Stack (Recent to Oldest) ---");
                if (releasedRoomsStack.isEmpty()) {
                    System.out.println("No cancellations recorded.");
                    inv.updateAvailability(type, available - 1);
                    System.out.println("SUCCESS: Room allocated to " + guest);
                } else {
                    // Displaying stack content
                    for (int i = releasedRoomsStack.size() - 1; i >= 0; i--) {
                        System.out.println("Cancelled: " + releasedRoomsStack.get(i));
                    }
                    System.out.println("FAILED: No rooms left for " + guest);
                }
            }
        }

// --- Previous classes (RoomInventory) remain the same ---
// --- RoomInventory must also be thread-safe ---
        class RoomInventory {
            private Map<String, Integer> inventory = new HashMap<>();
            public void addRoomType(String type, int count) { inventory.put(type, count); }
            public int getAvailability(String type) { return inventory.getOrDefault(type, 0); }
            public void updateAvailability(String type, int newCount) { inventory.put(type, newCount); }

            public synchronized void addRoomType(String type, int count) {
                inventory.put(type, count);
            }

            public synchronized int getAvailability(String type) {
                return inventory.getOrDefault(type, 0);
            }

            public synchronized void updateAvailability(String type, int newCount) {
                inventory.put(type, newCount);
            }
        }