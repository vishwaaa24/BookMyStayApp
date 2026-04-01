public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message and App Info
        System.out.println("******************************************");
        System.out.println("   Welcome to Book My Stay Application!   ");
        System.out.println("   System Version: 1.0                    ");
        System.out.println("******************************************");
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 2.0             ");
        System.out.println("========================================");

        System.out.println("Application started successfully.");
        // UC2: Domain Modeling & Static Availability

        // 1. Defining Room Objects (Polymorphism)
        Room single = new SingleRoom("S101", 1500.0);
        Room doubleRm = new DoubleRoom("D201", 2500.0);
        Room suite = new SuiteRoom("SU301", 5000.0);

        // 2. Static Availability Representation
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // 3. Displaying Room Details
        System.out.println("\n--- Available Room Types ---");
        displayRoomInfo(single, singleRoomAvailability);
        displayRoomInfo(doubleRm, doubleRoomAvailability);
        displayRoomInfo(suite, suiteRoomAvailability);
    }

    // Helper method to display room details
    public static void displayRoomInfo(Room room, int availability) {
        room.displayDetails();
        System.out.println("Availability: " + availability + " rooms left");
        System.out.println("------------------------------------");
    }
}

// --- UC2: OBJECT MODELING (Classes below) ---

// Abstract Class - Enforces a consistent structure
abstract class Room {
    private String roomNumber;
    private String type;
    private double price;

    public Room(String roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
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