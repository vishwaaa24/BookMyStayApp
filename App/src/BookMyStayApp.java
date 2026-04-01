import java.util.*;

/**
 * BookMyStayApp - UC8: Booking History & Reporting
 * BookMyStayApp - UC9: Error Handling & Validation
 * @author [Your Name]
 * @version 8.0
 * @version 9.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 8.0             ");
        System.out.println("   Application Version: 9.0             ");
        System.out.println("========================================");

        // UC8: Initialize History and Reporting
        BookingHistory history = new BookingHistory();
        ReportService reportService = new ReportService(history);
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1); // Only 1 room available

        // Simulating confirmed bookings being added to history
        System.out.println("\n--- Recording Confirmed Bookings to History ---");
        history.recordBooking("RES101: Alice - Single Room");
        history.recordBooking("RES102: Bob - Double Room");
        history.recordBooking("RES103: Charlie - Suite Room");
        // UC9: Testing Validation and Error Handling
        System.out.println("\n--- Processing Bookings with Validation ---");

        // Admin requests a report
        reportService.generateSummaryReport();
    }
}
// Scenario 1: Valid Booking
processSafeBooking("Alice", "Single Room", inventory);

/**
 * UC8: BookingHistory Class
 * Uses a List to maintain a chronological audit trail of all confirmed bookings.
 */
class BookingHistory {
    // List preserves the order of confirmation
    private List<String> historyLog;
    // Scenario 2: Invalid Room Type (Should throw exception)
    processSafeBooking("Bob", "Penthouse", inventory);

    public BookingHistory() {
        this.historyLog = new ArrayList<>();
        // Scenario 3: Out of Stock (Should throw exception)
        processSafeBooking("Charlie", "Single Room", inventory);
    }

    public void recordBooking(String bookingDetails) {
        historyLog.add(bookingDetails);
        System.out.println("History: Archived [" + bookingDetails + "]");
    }
    /**
     * Fail-Fast Validation Logic
     */
    public static void processSafeBooking(String guest, String type, RoomInventory inv) {
        try {
            System.out.println("\nAttempting to book: " + type + " for " + guest);

            // 1. Validate Input (Does the room type exist?)
            if (!type.equals("Single Room") && !type.equals("Double Room") && !type.equals("Suite Room")) {
                throw new InvalidBookingException("Error: Room type '" + type + "' does not exist in our system.");
            }

            // 2. Validate State (Is it available?)
            int count = inv.getAvailability(type);
            if (count <= 0) {
                throw new InvalidBookingException("Error: '" + type + "' is currently sold out.");
            }

            public List<String> getHistoryLog() {
                // Returning a copy to ensure Read-Only access (Defensive Programming)
                return new ArrayList<>(historyLog);
                // If passes validation, proceed
                inv.updateAvailability(type, count - 1);
                System.out.println("Success: Booking confirmed for " + guest);

            } catch (InvalidBookingException e) {
                // Graceful Failure Handling
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * UC8: ReportService Class
     * Decouples reporting logic from data storage.
     * UC9: Custom Exception Class
     * Making error causes explicit and readable.
     */
    class ReportService {
        private BookingHistory history;

        public ReportService(BookingHistory history) {
            this.history = history;
            class InvalidBookingException extends Exception {
                public InvalidBookingException(String message) {
                    super(message);
                }
            }

            public void generateSummaryReport() {
                List<String> logs = history.getHistoryLog();

                System.out.println("\n********** ADMIN OPERATIONAL REPORT **********");
                System.out.println("Total Bookings Processed: " + logs.size());
                System.out.println("----------------------------------------------");

                if (logs.isEmpty()) {
                    System.out.println("No history found.");
                } else {
                    for (int i = 0; i < logs.size(); i++) {
                        System.out.println((i + 1) + ". " + logs.get(i));
                    }
                }
                System.out.println("**********************************************");
            }
// --- Previous RoomInventory classes remain below ---
            class RoomInventory {
                private Map<String, Integer> inventory = new HashMap<>();
                public void addRoomType(String type, int count) { inventory.put(type, count); }
                public int getAvailability(String type) { return inventory.getOrDefault(type, 0); }
                public void updateAvailability(String type, int newCount) { inventory.put(type, newCount); }
            }