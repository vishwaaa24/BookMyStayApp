import java.util.*;

/**
 * BookMyStayApp - UC9: Error Handling & Validation
 * BookMyStayApp - UC10: Booking Cancellation & Inventory Rollback
 * @author [Your Name]
 * @version 9.0
 * @version 10.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 9.0             ");
        System.out.println("   Application Version: 10.0            ");
        System.out.println("========================================");

        // Setup Inventory (UC3)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1); // Only 1 room available
        inventory.addRoomType("Suite Room", 2);

        // UC9: Testing Validation and Error Handling
        System.out.println("\n--- Processing Bookings with Validation ---");
        // Setup Cancellation Service
        CancellationService cancellationService = new CancellationService(inventory);

        // Scenario 1: Valid Booking
        processSafeBooking("Alice", "Single Room", inventory);
        // Simulating a booking that we might want to cancel
        String roomID = "SU-505";
        System.out.println("\n--- Initial State ---");
        System.out.println("Suite Availability: " + inventory.getAvailability("Suite Room"));

        // Scenario 2: Invalid Room Type (Should throw exception)
        processSafeBooking("Bob", "Penthouse", inventory);
        // UC10: Performing a Cancellation (Rollback)
        System.out.println("\nAction: Guest cancels reservation for " + roomID);
        cancellationService.processCancellation("Suite Room", roomID);

        // Scenario 3: Out of Stock (Should throw exception)
        processSafeBooking("Charlie", "Single Room", inventory);
        System.out.println("\n--- Final State ---");
        System.out.println("Suite Availability: " + inventory.getAvailability("Suite Room"));
        cancellationService.displayRollbackHistory();
    }
}

/**
 * Fail-Fast Validation Logic
 */
public static void processSafeBooking(String guest, String type, RoomInventory inv) {
    try {
        System.out.println("\nAttempting to book: " + type + " for " + guest);
/**
 * UC10: CancellationService Class
 * Uses a Stack to track released room IDs for LIFO rollback behavior.
 */
        class CancellationService {
            private RoomInventory inventory;
            // Stack tracks room IDs in the order they are cancelled
            private Stack<String> releasedRoomsStack;

            // 1. Validate Input (Does the room type exist?)
            if (!type.equals("Single Room") && !type.equals("Double Room") && !type.equals("Suite Room")) {
                throw new InvalidBookingException("Error: Room type '" + type + "' does not exist in our system.");
            }
            public CancellationService(RoomInventory inventory) {
                this.inventory = inventory;
                this.releasedRoomsStack = new Stack<>();
            }

            // 2. Validate State (Is it available?)
            int count = inv.getAvailability(type);
            if (count <= 0) {
                throw new InvalidBookingException("Error: '" + type + "' is currently sold out.");
            }
            public void processCancellation(String type, String roomID) {
                // 1. Validation: Ensure the roomID is not null/empty
                if (roomID == null || roomID.isEmpty()) {
                    System.err.println("Error: Invalid Room ID for cancellation.");
                    return;
                }

                // If passes validation, proceed
                inv.updateAvailability(type, count - 1);
                System.out.println("Success: Booking confirmed for " + guest);
                // 2. Rollback Logic: Add to Stack (LIFO)
                releasedRoomsStack.push(roomID);

            } catch (InvalidBookingException e) {
                // Graceful Failure Handling
                System.err.println(e.getMessage());
            }
            // 3. Inventory Restoration: Increment count
            int currentCount = inventory.getAvailability(type);
        inventory.updateAvailability(type, currentCount + 1);

        System.out.println("Success: Room " + roomID + " rolled back to inventory.");
        }
    }

/**
 * UC9: Custom Exception Class
 * Making error causes explicit and readable.
 */
    class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
            public void displayRollbackHistory() {
                System.out.println("\n--- Room Rollback Stack (Recent to Oldest) ---");
                if (releasedRoomsStack.isEmpty()) {
                    System.out.println("No cancellations recorded.");
                } else {
                    // Displaying stack content
                    for (int i = releasedRoomsStack.size() - 1; i >= 0; i--) {
                        System.out.println("Cancelled: " + releasedRoomsStack.get(i));
                    }
                }
            }
        }

        // --- Previous RoomInventory classes remain below ---
// --- Previous classes (RoomInventory) remain the same ---
        class RoomInventory {
            private Map<String, Integer> inventory = new HashMap<>();
            public void addRoomType(String type, int count) { inventory.put(type, count); }