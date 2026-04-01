@@ -1,67 +1,78 @@
        import java.io.*;
        import java.util.*;

/**
 * BookMyStayApp - UC11: Concurrent Booking Simulation (Thread Safety)
 * BookMyStayApp - UC12: Data Persistence & System Recovery
 * @author [Your Name]
 * @version 11.0
 * @version 12.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   UC11: Multi-threaded Simulation      ");
        System.out.println("   UC12: Persistence & Recovery         ");
        System.out.println("========================================");

        String filename = "system_state.ser";
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Deluxe Room", 1); // Only ONE room left!

        // Creating two threads (Users) trying to book the SAME room at the SAME time
        Runnable bookingTask1 = () -> processConcurrentBooking("User_Alice", "Deluxe Room", inventory);
        Runnable bookingTask2 = () -> processConcurrentBooking("User_Bob", "Deluxe Room", inventory);
        // 1. Try to recover state from file
        PersistenceService persistence = new PersistenceService();
        RoomInventory recoveredInventory = persistence.loadState(filename);

        Thread thread1 = new Thread(bookingTask1);
        Thread thread2 = new Thread(bookingTask2);
        if (recoveredInventory != null) {
            inventory = recoveredInventory;
            System.out.println("RECOVERY SUCCESSFUL: Loaded existing inventory.");
        } else {
            System.out.println("INITIALIZING NEW STATE: No persistence file found.");
            inventory.addRoomType("Deluxe Room", 10);
        }

        System.out.println("\nSimulating simultaneous booking attempts...");
        thread1.start();
        thread2.start();
    }
    // 2. Perform a change
        inventory.updateAvailability("Deluxe Room", 8);
        System.out.println("Current Deluxe Rooms: " + inventory.getAvailability("Deluxe Room"));

    /**
     * Using 'synchronized' to ensure only one thread can execute this logic at once.
     * This prevents the "Double Booking" race condition.
     */
    public static synchronized void processConcurrentBooking(String guest, String type, RoomInventory inv) {
        int available = inv.getAvailability(type);
        // 3. Save state before shutdown
        persistence.saveState(inventory, filename);
        System.out.println("SYSTEM SHUTDOWN: State saved to " + filename);
    }
}

        System.out.println(guest + " is checking availability... found: " + available);
/**
 * UC12: PersistenceService
 * Handles Serialization (Save) and Deserialization (Load).
 */
class PersistenceService {

        if (available > 0) {
        // Simulating a small processing delay
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        public void saveState(RoomInventory inventory, String filename) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(inventory);
            } catch (IOException e) {
                System.err.println("Error saving state: " + e.getMessage());
            }
        }

        inv.updateAvailability(type, available - 1);
        System.out.println("SUCCESS: Room allocated to " + guest);
    } else {
        System.out.println("FAILED: No rooms left for " + guest);
        public RoomInventory loadState(String filename) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (RoomInventory) ois.readObject();
            } catch (FileNotFoundException e) {
                return null; // Normal if it's the first time running
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading state: " + e.getMessage());
                return null;
            }
        }
    }

    // --- RoomInventory must also be thread-safe ---
    class RoomInventory {
        /**
         * RoomInventory must implement Serializable to be saved to a file.
         */
        class RoomInventory implements Serializable {
            private static final long serialVersionUID = 1L; // Ensures version compatibility
            private Map<String, Integer> inventory = new HashMap<>();

            public synchronized void addRoomType(String type, int count) {
                inventory.put(type, count);
            }

            public synchronized int getAvailability(String type) {
                return inventory.getOrDefault(type, 0);
            }

            public synchronized void updateAvailability(String type, int newCount) {
                inventory.put(type, newCount);
            }
            public synchronized void addRoomType(String type, int count) { inventory.put(type, count); }
            public synchronized int getAvailability(String type) { return inventory.getOrDefault(type, 0); }
            public synchronized void updateAvailability(String type, int newCount) { inventory.put(type, newCount); }
        }