
import java.util.List;
import java.util.Scanner;

/**
 * this class handles the user interface and manages
 * flight planning operations.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AirplaneManager airplaneManager = new AirplaneManager();
        AirportManager airportManager = new AirportManager();

        while (true) {
            System.out.println("\nFlight Planning System");
            System.out.println("1. Airport Functions");
            System.out.println("2. Modify Airport");
            System.out.println("3. Add Airplane");
            System.out.println("4. Plan a Flight");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
            case 1:
                airportFunctionsMenu(scanner, airportManager); 
                break;
            case 2:
                airportManager.modifyAirport(scanner);
                break;
            case 3:
                airplaneManager.addAirplane(scanner);
                break;
            case 4:
                planFlight(scanner, airportManager, airplaneManager);
                break;
            case 5:
                System.out.println("Exiting program...");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * This will display the Airport Functions menu.
     */
    private static void airportFunctionsMenu(Scanner scanner, AirportManager airportManager) {
        while (true) {
            System.out.println("\nAirport Functions");
            System.out.println("1. Add Airport");
            System.out.println("2. Modify Airport");
            System.out.println("3. Display Airport Information");
            System.out.println("4. Search Airport");
            System.out.println("5. Remove Airport");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
            case 1:
                airportManager.addAirport(scanner);
                break;
            case 2:
                airportManager.modifyAirport(scanner);
                break;
            case 3:
            airportManager.displayAirport(scanner);
                break;
            case 4:
                airportManager.searchAirport(scanner);
                break;
            case 5:
                airportManager.removeAirport(scanner);
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /** 
    private static void addAirplane(Scanner scanner, AirplaneManager airplaneManager) {
        System.out.print("Enter Make and Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Plane Type (Jet, Prop, Turboprop): ");
        String planeType = scanner.nextLine();
        System.out.print("Enter Fuel Type: ");
        String fuelType = scanner.nextLine();
        System.out.print("Enter Max Range: ");
        double maxRange = scanner.nextDouble();
        System.out.print("Enter Fuel Burn Rate: ");
        double fuelBurnRate = scanner.nextDouble();
        System.out.print("Enter Fuel Capacity: ");
        double fuelCapacity = scanner.nextDouble();
        System.out.print("Enter Airspeed: ");
        double airspeed = scanner.nextDouble();
        scanner.nextLine();

        airplaneManager.addAirplane(new Airplanes(model, fuelType, maxRange, fuelBurnRate, fuelCapacity, airspeed));
        System.out.println("Airplane added successfully!");
    }
    **/
    /**
     * This handles flight planning, distance, fuel estimation, and refueling stops.
     */
    private static void planFlight(Scanner scanner, AirportManager airportManager, AirplaneManager airplaneManager) {
        System.out.println("\nAvailable Airports:");
        List<Airport> airports = airportManager.getAirports();
        for (int i = 0; i < airports.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, airports.get(i).getAirportName(), airports.get(i).getICAOID());
        }
        System.out.print("Select start airport (number): ");
        int startIdx = scanner.nextInt() - 1;

        System.out.println("\nAvailable Destination Airports:");
        for (int i = 0; i < airports.size(); i++) {
            if (i != startIdx) {
                System.out.printf("%d. %s (%s)\n", i + 1, airports.get(i).getAirportName(), airports.get(i).getICAOID());
            }
        }
        System.out.print("Select destination airport (number): ");
        int destIdx = scanner.nextInt() - 1;

        System.out.println("\nAvailable Airplanes:");
        List<Airplane> airplanes = airplaneManager.getAirplanes();
        for (int i = 0; i < airplanes.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, airplanes.get(i).getModel(), airplanes.get(i).getFuelType());
        }
        System.out.print("Select an airplane (number): ");
        int planeIdx = scanner.nextInt() - 1;
        
        Flight flight = FlightPlanning.planFlight(airports.get(startIdx), airports.get(destIdx), airplanes.get(planeIdx), airports);
        flight.displayFlightPlan();
    }
}

