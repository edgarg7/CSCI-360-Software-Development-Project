import java.io.*;
import java.util.*;

public class AirportManager {
    private List<Airport> airports;
    private static final String FILE_NAME = "airports.txt";

    // Constructor for AirportManager
    public AirportManager() {
        this.airports = loadAirports();
    }

    // Returns list of airports
    public List<Airport> getAirports() {
        return airports;
    }

    // Adds an airport to Airports.txt file
    public void addAirport(Airport airport) {
        airports.add(airport);
        saveAirports();
    }

    // Adds an airport using user input (This is the previous function used in Main.java but updated)
    public void addAirportFromInput(Scanner scanner) {
        System.out.print("Enter Airport Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ICAO ID: ");
        String ICAOID = scanner.nextLine();
        System.out.print("Enter Latitude: ");
        double latitude = scanner.nextDouble();
        System.out.print("Enter Longitude: ");
        double longitude = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Fuel Type: ");
        String fuelType = scanner.nextLine();
        System.out.print("Enter Radio Frequency: ");
        String radioFrequencies = scanner.nextLine();

        Airport newAirport = new Airport(name, ICAOID, latitude, longitude, fuelType, radioFrequencies);
        addAirport(newAirport);
        System.out.println("Airport added successfully!");
    }

    // Modifies an airport using user input
    public void modifyAirportFromInput(Scanner scanner) {
        int start = 0;
        int batchSize = 10;
        boolean moreAirports = true;

        while (moreAirports) {
            for (int i = start; i < start + batchSize && i < airports.size(); i++) {
                Airport airport = airports.get(i);
                System.out.println((i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAOID() + ")");
            }

            System.out.print("Enter the number of the airport to modify, type 'more' to see more airports, or type 'search' to search by ICAOID: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("more")) {
                if (start + batchSize >= airports.size()) {
                    System.out.println("No more airports to display.");
                    continue;
                }
                start += batchSize;
            } else if (input.equalsIgnoreCase("search")) {
                System.out.print("Enter ICAO ID to search: ");
                String ICAOID = scanner.nextLine();
                Airport airport = searchAirport(ICAOID);
                if (airport == null) {
                    System.out.println("Airport not found.");
                    continue;
                }
                modifyAirportDetails(scanner, airport);
                moreAirports = false; // Exit the loop after modification
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;

                    if (index < 0 || index >= airports.size()) {
                        System.out.println("Invalid selection.");
                        continue;
                    }

                    Airport airport = airports.get(index);
                    modifyAirportDetails(scanner, airport);
                    moreAirports = false; // Exit the loop after modification
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                }
            }
        }
    }

    // Helper method to modify airport details
    private void modifyAirportDetails(Scanner scanner, Airport airport) {
        System.out.print("Enter new Airport Name (current: " + airport.getAirportName() + "): ");
        String name = scanner.nextLine();
        System.out.print("Enter new Latitude (current: " + airport.getLatitude() + "): ");
        double latitude = scanner.nextDouble();
        System.out.print("Enter new Longitude (current: " + airport.getLongitude() + "): ");
        double longitude = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Fuel Type (current: " + airport.getFuelTypes() + "): ");
        String fuelType = scanner.nextLine();
        System.out.print("Enter new Radio Frequency (current: " + airport.getRadioFrequencies() + "): ");
        String radioFrequencies = scanner.nextLine();

        Airport updatedAirport = new Airport(name, airport.getICAOID(), latitude, longitude, fuelType, radioFrequencies);
        modifyAirport(airport.getICAOID(), updatedAirport);
        System.out.println("Airport modified successfully!");
    }

    // Removes an airport from Airports.txt file
    public void removeAirport(String ICAOID) {
        airports.removeIf(airport -> airport.getICAOID().equals(ICAOID));
        saveAirports();
    }

    // Removes an airport using user input
    public void removeAirportFromInput(Scanner scanner) {
        System.out.print("Enter ICAO ID of the airport to remove: ");
        String ICAOID = scanner.nextLine();
        Airport airport = searchAirport(ICAOID);
        if (airport != null) {
            removeAirport(ICAOID);
            System.out.println("Airport removed successfully!");
        } else {
            System.out.println("Airport not found.");
        }
    }

    // Modifies an airport in the Airports.txt file
    public void modifyAirport(String ICAOID, Airport updatedAirport) {
        for (int i = 0; i < airports.size(); i++) {
            if (airports.get(i).getICAOID().equals(ICAOID)) {
                airports.set(i, updatedAirport);
                break;
            }
        }
        saveAirports();
    }

    // Allows the user to search for an airport by ICAOID
    public Airport searchAirport(String ICAOID) {
        for (Airport airport : airports) {
            if (airport.getICAOID().equals(ICAOID)) {
                return airport;
            }
        }
        return null;
    }

    // Allows the user to search for an airport by name
    public Airport searchAirportByName(String name) {
        for (Airport airport : airports) {
            if (airport.getAirportName().equalsIgnoreCase(name)) {
                return airport;
            }
        }
        return null;
    }

    // Searches for an airport using user input
    public void searchAirport(Scanner scanner) {
        System.out.print("Search by (1) ICAO ID or (2) Airport Name: ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (searchChoice == 1) {
            System.out.print("Enter ICAO ID of the airport to search: ");
            String ICAOID = scanner.nextLine();
            displayAirport(ICAOID);
        } else if (searchChoice == 2) {
            System.out.print("Enter Airport Name to search: ");
            String airportName = scanner.nextLine();
            displayAirportByName(airportName);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    // Displays the airport information
    public void displayAirport(String ICAOID) {
        Airport airport = searchAirport(ICAOID);
        if (airport != null) {
            System.out.println("\nAirport Name: " + airport.getAirportName());
            System.out.println("ICAO ID: " + airport.getICAOID());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Fuel Types: " + airport.getFuelTypes());
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
        } else {
            System.out.println("Airport not found.");
        }
    }

    // Displays the airport information by name
    public void displayAirportByName(String name) {
        Airport airport = searchAirportByName(name);
        if (airport != null) {
            System.out.println("Airport Name: " + airport.getAirportName());
            System.out.println("ICAO ID: " + airport.getICAOID());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Fuel Types: " + airport.getFuelTypes());
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
        } else {
            System.out.println("Airport not found.");
        }
    }

    // Validation method for ICAOID
    public boolean validateAirport(String ICAOID) {
        return searchAirport(ICAOID) != null;
    }

    // Setter for airport
    public void setAirport(String ICAOID, Airport updatedAirport) {
        modifyAirport(ICAOID, updatedAirport);
    }

    // Getter for airport
    public void setFrequencies(String ICAOID, String radioFrequencies) {
        Airport airport = searchAirport(ICAOID);
        if (airport != null) {
            airport.setRadioFrequencies(radioFrequencies);
            saveAirports();
        }
    }

    // Getter for frequencies
    public String getFrequencies(String ICAOID) {
        Airport airport = searchAirport(ICAOID);
        return airport != null ? airport.getRadioFrequencies() : null;
    }

    // Loads airports from Airports.txt file
    private List<Airport> loadAirports() {
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                airports.add(new Airport(data[0], data[1], Double.parseDouble(data[2]), Double.parseDouble(data[3]), data[4], data[5]));
            }
        } catch (IOException e) {
            System.out.println("No existing airport data found.");
        }
        return airports;
    }

    // Saves the airports to Airports.txt file
    private void saveAirports() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Airport Name, ICAO, Latitude, Longitude, Fuel Type, Frequency\n"); // Write header line
            for (Airport airport : airports) {
                writer.write(airport.getAirportName() + "," + airport.getICAOID() + "," + airport.getLatitude() + ","
                        + airport.getLongitude() + "," + airport.getFuelTypes() + "," + airport.getRadioFrequencies() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving airport data.");
        }
    }
}