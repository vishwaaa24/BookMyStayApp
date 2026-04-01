import java.util.*;

/**
 * BookMyStayApp - UC7: Add-On Service Selection
 * BookMyStayApp - UC8: Booking History & Reporting
 * @author [Your Name]
 * @version 7.0
 * @version 8.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // UC1: Welcome Message
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App!         ");
        System.out.println("   Application Version: 7.0             ");
        System.out.println("   Application Version: 8.0             ");
        System.out.println("========================================");

        // UC7: Initialize Add-On Manager
        AddOnManager addonManager = new AddOnManager();
        // UC8: Initialize History and Reporting
        BookingHistory history = new BookingHistory();
        ReportService reportService = new ReportService(history);

        // Assume we have a Reservation ID from UC6 (e.g., "S101")
        String reservationId = "S101";
        // Simulating confirmed bookings being added to history
        System.out.println("\n--- Recording Confirmed Bookings to History ---");
        history.recordBooking("RES101: Alice - Single Room");
        history.recordBooking("RES102: Bob - Double Room");
        history.recordBooking("RES103: Charlie - Suite Room");

        System.out.println("\n--- Selecting Add-On Services for Reservation: " + reservationId + " ---");

        // Guest selects services
        addonManager.addServiceToReservation(reservationId, new Service("Breakfast", 500.0));
        addonManager.addServiceToReservation(reservationId, new Service("Late Checkout", 1000.0));
        addonManager.addServiceToReservation(reservationId, new Service("WiFi Plus", 200.0));

        // Display services and total extra cost
        addonManager.displayServicesForReservation(reservationId);
        // Admin requests a report
        reportService.generateSummaryReport();
    }
}

/**
 * UC7: Service Class
 * Represents an optional offering with a name and price.
 * UC8: BookingHistory Class
 * Uses a List to maintain a chronological audit trail of all confirmed bookings.
 */
class Service {
    private String name;
    private double price;
    class BookingHistory {
        // List preserves the order of confirmation
        private List<String> historyLog;

        public Service(String name, double price) {
            this.name = name;
            this.price = price;
    public BookingHistory() {
                this.historyLog = new ArrayList<>();
            }

            public String getName() { return name; }
            public double getPrice() { return price; }
            public void recordBooking(String bookingDetails) {
                historyLog.add(bookingDetails);
                System.out.println("History: Archived [" + bookingDetails + "]");
            }

            @Override
            public String toString() {
                return name + " (Rs. " + price + ")";
                public List<String> getHistoryLog() {
                    // Returning a copy to ensure Read-Only access (Defensive Programming)
                    return new ArrayList<>(historyLog);
                }
            }

/**
 * UC7: AddOnManager Class
 * Uses Map<String, List<Service>> to link one reservation to many services.
 * UC8: ReportService Class
 * Decouples reporting logic from data storage.
 */
            class AddOnManager {
                // Reservation ID -> List of selected services
                private Map<String, List<Service>> reservationAddOns;
                class ReportService {
                    private BookingHistory history;

                    public AddOnManager() {
                        this.reservationAddOns = new HashMap<>();
    public ReportService(BookingHistory history) {
                            this.history = history;
                        }

                        public void addServiceToReservation(String resId, Service service) {
                            // If the reservation doesn't have a list yet, create one
                            reservationAddOns.putIfAbsent(resId, new ArrayList<>());
                            reservationAddOns.get(resId).add(service);
                            System.out.println("Added: " + service.getName() + " to " + resId);
                        }

                        public void displayServicesForReservation(String resId) {
                            List<Service> services = reservationAddOns.get(resId);
                            public void generateSummaryReport() {
                                List<String> logs = history.getHistoryLog();

                                if (services == null || services.isEmpty()) {
                                    System.out.println("No add-ons selected for " + resId);
                                    return;
                                }
                                System.out.println("\n********** ADMIN OPERATIONAL REPORT **********");
                                System.out.println("Total Bookings Processed: " + logs.size());
                                System.out.println("----------------------------------------------");

                                System.out.println("\nSummary for " + resId + ":");
                                double totalCost = 0;
                                for (Service s : services) {
                                    System.out.println("- " + s);
                                    totalCost += s.getPrice();
                                    if (logs.isEmpty()) {
                                        System.out.println("No history found.");
                                    } else {
                                        for (int i = 0; i < logs.size(); i++) {
                                            System.out.println((i + 1) + ". " + logs.get(i));
                                        }
                                    }
                                    System.out.println("Total Add-On Cost: Rs. " + totalCost);
                                    System.out.println("**********************************************");
                                }
                            }