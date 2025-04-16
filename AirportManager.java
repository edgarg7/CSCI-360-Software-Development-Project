// I am utilizing "instanceof" in the code as a comparison to check the type of the object (scanner or airport).
// If the object is a scanner, it will prompt the user for input. 
// If it is a variable, or a number, it will prompt a different method to be called via a else if statement. 

import java.io.*;
import java.util.*;

public class AirportManager {

    private final List<Airport> airports;
    private static final String FILE_NAME = "Airport_Database.csv";

    // Constructor for AirportManager
    public AirportManager() {
        this.airports = loadAirports();
    }

    // Returns list of airports
    public List<Airport> getAirports() {
        return airports;
    }

    // Adds an airport to Airports.txt file
    public void addAirport(Object airportOrScanner) {
        // Directly add an airport object
        if (airportOrScanner instanceof Airport) {
            Airport airport = (Airport) airportOrScanner;
            airports.add(airport);
            saveAirports();
        } // If there is an instance of a scanner (user input) this method will be called
        else if (airportOrScanner instanceof Scanner) {
            Scanner scanner = (Scanner) airportOrScanner;
            try {
                // Collect string inputs
                System.out.println();
                String airportID = getStringInput(scanner, "Enter Airport ID: ");
                String airportName = getStringInput(scanner, "Enter Airport Name: ");
                String regionState = getStringInput(scanner, "Enter Region/State: ");
                String regionAbbr = getStringInput(scanner, "Enter Region Abbreviation: ");
                String city = getStringInput(scanner, "Enter City: ");
                String ICAO = getStringInput(scanner, "Enter ICAO Code: ");
                String iataCode = getStringInput(scanner, "Enter IATA Code: ");

                // Check if ICAO already exists
                for (Airport existingAirport : airports) {
                    if (existingAirport.getICAO().equalsIgnoreCase(ICAO)) {
                        throw new IllegalArgumentException("An airport with ICAO code " + ICAO + " already exists");
                    }
                }

                // Collect numeric inputs. Have a limit on latitude and longitude so they are not out of bounds
                double latitude = getDoubleInput(scanner, "Enter Latitude (-90 to 90): ", (double) -90, (double) 90);
                double longitude = getDoubleInput(scanner, "Enter Longitude (-180 to 180): ", (double) -180, (double) 180);
                double elevation = getDoubleInput(scanner, "Enter Elevation (in feet): ", null, null);
                double radioFrequencies = getDoubleInput(scanner, "Enter Radio Frequencies: ", null, null);
                double fuelTypes = getDoubleInput(scanner, "Enter Fuel Types (1=AVGAS, 2=Jet A, 3=both): ", (double) 1, (double) 3);

                // Create a new airport object and add it to the list
                Airport newAirport = new Airport(airportID, airportName, latitude, longitude, elevation,
                        radioFrequencies, regionState, regionAbbr, city,
                        ICAO, iataCode, fuelTypes);
                airports.add(newAirport);
                saveAirports();
                System.out.println("Airport added successfully!");

            // Handle exceptions for invalid input
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Airport addition cancelled.");
            }
        }
    }

    // Helper method for getting validated string input
    private String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        return input;
    }

    // Helper method for getting validated numeric input
    private double getDoubleInput(Scanner scanner, String prompt, Double min, Double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                double value = Double.parseDouble(input);

                // Check range if min and max are provided
                if (min != null && value < min) {
                    System.out.println("Error: Value must be at least " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("Error: Value must be at most " + max);
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    // Modifies an airport using user input
    public void modifyAirport(Object param1, Object... additionalParams) {
        // Direct modification via ICAO ID and updated Airport object
        if (param1 instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Airport) {
            String ICAOID = (String) param1;
            Airport updatedAirport = (Airport) additionalParams[0];

            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).getICAO().equalsIgnoreCase(ICAOID)) {
                    airports.set(i, updatedAirport);
                    saveAirports();
                    break;
                }
            }
        } // Interactive modification via Scanner
        else if (param1 instanceof Scanner) {
            Scanner scanner = (Scanner) param1;
            int start = 0;
            int batchSize = 10;
            boolean moreAirports = true;

            while (moreAirports) {
                // Display a batch of airports. Will display 10 at a time
                for (int i = start; i < start + batchSize && i < airports.size(); i++) {
                    Airport airport = airports.get(i);
                    System.out.println("\n" + (i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAO() + ")");
                }

                // Prompt for the user to select an airport, display more airports, or search for a airport
                System.out.print("\nEnter the number of the airport to modify, type 'more' to see more airports, or type 'search' to search by ICAO: ");
                String input = scanner.nextLine();

                // If the user enters "more", show the next batch of airports
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= airports.size()) {
                        System.out.println("No more airports to show.");
                        continue;
                    }
                    start += batchSize;
                // If the user enters "search", prompt for ICAO code
                } else if (input.equalsIgnoreCase("search")) {
                    try {
                        String ICAO = getStringInput(scanner, "Enter ICAO code to search: ");

                        Airport foundAirport = null;
                        for (Airport airport : airports) {
                            if (airport.getICAO().equalsIgnoreCase(ICAO)) {
                                foundAirport = airport;
                                break;
                            }
                        }

                        if (foundAirport == null) {
                            System.out.println("No airport found with ICAO code: " + ICAO);
                            continue;
                        }

                        modifyAirportDetails(scanner, foundAirport);
                        moreAirports = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                // If the user enters a number, modify that airport
                } else {
                    try {
                        int index = Integer.parseInt(input) - 1;

                        // Check if the index is valid
                        if (index < 0 || index >= airports.size()) {
                            System.out.println("Invalid selection");
                            continue;
                        }

                        // Modify the selected airport
                        Airport airport = airports.get(index);
                        modifyAirportDetails(scanner, airport);
                        moreAirports = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                    }
                }
            }
        }
    }

    // Helper method to collect modified airport details
    private void modifyAirportDetails(Scanner scanner, Airport airport) {
        try {
            // Collect string inputs with current values shown
            String airportID = getStringInputWithDefault(scanner, "Enter new Airport ID (current: " + airport.getAirportID() + "): ", airport.getAirportID());
            String airportName = getStringInputWithDefault(scanner, "Enter new Airport Name (current: " + airport.getAirportName() + "): ", airport.getAirportName());
            String regionState = getStringInputWithDefault(scanner, "Enter new Region/State (current: " + airport.getRegionState() + "): ", airport.getRegionState());
            String regionAbbr = getStringInputWithDefault(scanner, "Enter new Region Abbreviation (current: " + airport.getRegionAbbr() + "): ", airport.getRegionAbbr());
            String city = getStringInputWithDefault(scanner, "Enter new City (current: " + airport.getCity() + "): ", airport.getCity());

            // We need to handle the ICAO code specially to check for duplicates
            String ICAO = airport.getICAO(); // Store original ICAO
            String newICAO = getStringInputWithDefault(scanner, "Enter new ICAO Code (current: " + ICAO + "): ", ICAO);

            // Check if the new ICAO is different and already exists
            if (!newICAO.equalsIgnoreCase(ICAO)) {
                for (Airport existingAirport : airports) {
                    if (existingAirport != airport && existingAirport.getICAO().equalsIgnoreCase(newICAO)) {
                        throw new IllegalArgumentException("An airport with ICAO code " + newICAO + " already exists");
                    }
                }
            }

            String iataCode = getStringInputWithDefault(scanner, "Enter new IATA Code (current: " + airport.getIataCode() + "): ", airport.getIataCode());

            // Collect numeric inputs with current values shown
            double latitude = getDoubleInputWithDefault(scanner, "Enter new Latitude (-90 to 90) (current: " + airport.getLatitude() + "): ",
                    airport.getLatitude(), -90.0, 90.0);
            double longitude = getDoubleInputWithDefault(scanner, "Enter new Longitude (-180 to 180) (current: " + airport.getLongitude() + "): ",
                    airport.getLongitude(), -180.0, 180.0);
            double elevation = getDoubleInputWithDefault(scanner, "Enter new Elevation (in feet) (current: " + airport.getElevation() + "): ",
                    airport.getElevation(), null, null);
            double radioFrequencies = getDoubleInputWithDefault(scanner, "Enter new Radio Frequencies (current: " + airport.getRadioFrequencies() + "): ",
                    airport.getRadioFrequencies(), null, null);
            double fuelTypes = getDoubleInputWithDefault(scanner, "Enter new Fuel Types (1=AVGAS, 2=Jet A, 3=both) (current: " + airport.getFuelTypes() + "): ",
                    airport.getFuelTypes(), 1.0, 3.0);

            // Create new airport with updated details and update the collection
            Airport updatedAirport = new Airport(airportID, airportName, latitude, longitude, elevation,
                    radioFrequencies, regionState, regionAbbr, city,
                    newICAO, iataCode, fuelTypes);

            // Update the airport in the list
            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).getICAO().equalsIgnoreCase(ICAO)) {
                    airports.set(i, updatedAirport);
                    break;
                }
            }

            saveAirports();
            System.out.println("\nAirport updated successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nAirport modification cancelled.");
        }
    }

    // Helper method for getting string input with a default value
    private String getStringInputWithDefault(Scanner scanner, String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    // Helper method for getting numeric input with a default value
    private double getDoubleInputWithDefault(Scanner scanner, String prompt, double defaultValue, Double min, Double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                // If input is empty, return default value
                if (input.isEmpty()) {
                    return defaultValue;
                }

                double value = Double.parseDouble(input);

                // Check range if min and max are provided
                if (min != null && value < min) {
                    System.out.println("Error: Value must be at least " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("Error: Value must be at most " + max);
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number or press Enter to keep current value");
            }
        }
    }

    // Displays the airport information by ICAOID or name
    public void displayAirport(Object param) {
        Airport airport = null;

        if (param instanceof Scanner) {
            // If we receive a Scanner, search for the airport first
            System.out.println("\n[[--Airport Search--]]");
            Scanner scanner = (Scanner) param;
            airport = searchAirport(scanner);
        } else if (param instanceof Airport) {
            // If we receive an Airport object directly, use that
            airport = (Airport) param;
        }

        if (airport != null) {
            System.out.println("\n[[--Airport Details--]]");
            System.out.println("Airport ID: " + airport.getAirportID());
            System.out.println("Airport Name: " + airport.getAirportName());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Elevation: " + airport.getElevation() + " ft");
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
            System.out.println("Region/State: " + airport.getRegionState());
            System.out.println("Region Abbreviation: " + airport.getRegionAbbr());
            System.out.println("City: " + airport.getCity());
            System.out.println("ICAO Code: " + airport.getICAO());
            System.out.println("IATA Code: " + airport.getIataCode());

            // Display fuel type in a more readable format
            String fuelTypeStr;
            double fuelTypes = airport.getFuelTypes();
            if (fuelTypes == 1.0) {
                fuelTypeStr = "AVGAS only";
            } else if (fuelTypes == 2.0) {
                fuelTypeStr = "Jet A only";
            } else if (fuelTypes == 3.0) {
                fuelTypeStr = "Both AVGAS and Jet A";
            } else {
                fuelTypeStr = String.valueOf(fuelTypes);
            }
            System.out.println("Fuel Types: " + fuelTypeStr);
        } else {
            System.out.println("No airport found to display.");
        }
    }

    // Searches for an airport by ICAOID or name
    public Airport searchAirport(Object searchParam, Object... additionalParams) {
        // If there is an instance of a scanner (user input) this method will be called
        if (searchParam instanceof Scanner) {
            Scanner scanner = (Scanner) searchParam;

            // Use try-catch for numeric input validation
            int searchChoice = 0;
            boolean validChoice = false;

            while (!validChoice) {
                System.out.print("Search by (1) ICAO or (2) Airport Name: ");
                try {
                    String input = scanner.nextLine().trim();
                    searchChoice = Integer.parseInt(input);

                    if (searchChoice == 1 || searchChoice == 2) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number (1 or 2).");
                }
            }

            // I use a ArrayList becuase of the dynamic size of the list
            List<Airport> matchingAirports = new ArrayList<>();
            String searchTerm = "";
            boolean searchByICAO = false;

            // Prompt for search term based on choice
            switch (searchChoice) {
                case 1:
                    // Search by ICAO
                    System.out.print("Enter full or partial ICAO of the airport to search: ");
                    searchTerm = scanner.nextLine().trim().toUpperCase();
                    searchByICAO = true;
                    break;
                case 2:
                    // Search by Airport Name (full or partial)
                    System.out.print("Enter full or partial Airport Name to search: ");
                    searchTerm = scanner.nextLine().trim().toLowerCase();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return null;
            }

            // Find all matching airports
            for (Airport airport : airports) {
                if (searchByICAO) {
                    if (airport.getICAO().toUpperCase().contains(searchTerm)) {
                        matchingAirports.add(airport);
                    }
                } else { // Search by name
                    if (airport.getAirportName().toLowerCase().contains(searchTerm)) {
                        matchingAirports.add(airport);
                    }
                }
            }

            // Handle search results
            if (matchingAirports.isEmpty()) {
                System.out.println("No airports found matching your search.");
                return null;
            } else if (matchingAirports.size() == 1) {
                // Only one match found
                Airport foundAirport = matchingAirports.get(0);

                // If called from main menu (not from displayAirport), show the details
                boolean calledFromDisplayAirport = false;
                if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                    calledFromDisplayAirport = (Boolean) additionalParams[0];
                }

                if (!calledFromDisplayAirport) {
                    displayAirport(foundAirport);
                }

                return foundAirport;
            } else {
                // If there are multiple matches, display the list and ask for selection
                System.out.println("\nFound " + matchingAirports.size() + " matching airports:");
                for (int i = 0; i < matchingAirports.size(); i++) {
                    Airport airport = matchingAirports.get(i);
                    System.out.println((i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAO() + ")");
                }

                System.out.print("\nEnter the number of the airport to select: ");
                try {
                    int selection = Integer.parseInt(scanner.nextLine().trim());
                    if (selection >= 1 && selection <= matchingAirports.size()) {
                        Airport selectedAirport = matchingAirports.get(selection - 1);

                        // If called from main menu (not from displayAirport), show the details
                        boolean calledFromDisplayAirport = false;
                        if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                            calledFromDisplayAirport = (Boolean) additionalParams[0];
                        }

                        if (!calledFromDisplayAirport) {
                            displayAirport(selectedAirport);
                        }

                        return selectedAirport;
                    } else {
                        System.out.println("Invalid selection.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    return null;
                }
            }
        } // Original direct search implementation 
        else if (searchParam instanceof String) {
            String searchValue = (String) searchParam;

            for (Airport airport : airports) {
                if (airport.getICAO().equalsIgnoreCase(searchValue)) {
                    return airport;
                }
            }

            // If no match by ICAO, try searching by name if specified
            if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean && !(Boolean) additionalParams[0]) {
                for (Airport airport : airports) {
                    if (airport.getAirportName().equalsIgnoreCase(searchValue)) {
                        return airport;
                    }
                }
            }
        }

        return null;
    }

    public void removeAirport(Object scannerOrIcaoId) {
        // Remove airport by ICAOID
        if (scannerOrIcaoId instanceof Scanner) {
            Scanner scanner = (Scanner) scannerOrIcaoId;
            System.out.print("Enter ICAO ID of the airport to remove: ");
            String ICAOID = scanner.nextLine();
            Airport airport = searchAirport(ICAOID);
            if (airport != null) {
                // Call the same method with the ICAOID to actually remove it
                removeAirport(ICAOID);
                System.out.println("Airport removed successfully!");
            } else {
                System.out.println("Airport not found.");
            }
        } else if (scannerOrIcaoId instanceof String) {
            String ICAOID = (String) scannerOrIcaoId;
            airports.removeIf(airport -> airport.getICAO().equalsIgnoreCase(ICAOID));
            saveAirports();
        }
    }

    // Loads airports from Airports.txt file
    private List<Airport> loadAirports() {
        List<Airport> loadedAirports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true; // To skip header row

            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Skip comment lines or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 12) { // Make sure we have all fields
                    try {
                        // Parse for each field
                        String airportID = parts[0];
                        String airportName = parts[1];

                        // I use Double.parseDouble to convert from a string to double objects
                        Double latitude = Double.parseDouble(parts[2]);
                        Double longitude = Double.parseDouble(parts[3]);
                        Double elevation = Double.parseDouble(parts[4]);
                        Double radioFrequencies = Double.parseDouble(parts[5]);

                        // Parse the rest of the fields
                        String regionState = parts[6];
                        String regionAbbr = parts[7];
                        String city = parts[8];
                        String ICAO = parts[9];
                        String iataCode = parts[10];
                        Double fuelTypes = Double.parseDouble(parts[11]);

                        // Create a new Airport object and add it to the list
                        loadedAirports.add(new Airport(airportID, airportName, latitude, longitude,
                                elevation, radioFrequencies, regionState,
                                regionAbbr, city, ICAO, iataCode, fuelTypes));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing number in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading airports: " + e.getMessage());
        }
        return loadedAirports;
    }

    // Saves the airports to Airports.txt file
    private void saveAirports() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // false = overwrite file
            // Write header row matching the CSV format
            writer.println("Airport ID,Airport Name,Latitude,Longitude,Elevation (Ft.),Frequency,Region_State,Region_Abbr.,City,ICAO,iata_code,Fuel Types");

            // Write airport data
            for (Airport airport : airports) {
                writer.println(airport.getAirportID() + ","
                        + airport.getAirportName() + ","
                        + airport.getLatitude() + ","
                        + airport.getLongitude() + ","
                        + airport.getElevation() + ","
                        + airport.getRadioFrequencies() + ","
                        + airport.getRegionState() + ","
                        + airport.getRegionAbbr() + ","
                        + airport.getCity() + ","
                        + airport.getICAO() + ","
                        + airport.getIataCode() + ","
                        + airport.getFuelTypes());
            }
        } catch (IOException e) {
            System.out.println("Error saving airports: " + e.getMessage());
        }
    }
}