// I am utilizing "instanceof" in the code as a comparison to check the type of the object (scanner or airport).
// If the object is a scanner, it will prompt the user for input. If it is a variable, or a number, it will prompt a different method to be called via a else if statement. 
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
        }
        // If there is an instance of a scanner (user input) this method will be called
        else if (airportOrScanner instanceof Scanner) { 
            Scanner scanner = (Scanner) airportOrScanner;

            System.out.print("Enter Airport ID: ");
            String airportID = scanner.nextLine();

            System.out.print("Enter Airport Name: ");
            String airportName = scanner.nextLine();

            System.out.print("Enter Latitude: ");
            double latitude = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Longitude: ");
            double longitude = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Elevation: ");
            double elevation = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Radio Frequencies: ");
            Double radioFrequencies = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Region/State: ");
            String regionState = scanner.nextLine();

            System.out.print("Enter Region Abbreviation: ");
            String regionAbbr = scanner.nextLine();

            System.out.print("Enter City: ");
            String city = scanner.nextLine();

            System.out.print("Enter ICAO Code: ");
            String ICAO = scanner.nextLine();

            System.out.print("Enter IATA Code: ");
            String iataCode = scanner.nextLine();

            System.out.print("Enter Fuel Types (1 for JUST AVGAS, 2 for JUST Jet A, and 3 for both): ");
            Double fuelTypes = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Airport newAirport = new Airport(airportID, airportName, latitude, longitude, elevation, radioFrequencies, regionState, regionAbbr, city, ICAO, iataCode, fuelTypes);
            airports.add(newAirport);
            saveAirports();
            System.out.println("Airport added successfully!");
        }
    }

    // Modifies an airport using user input
    public void modifyAirport(Object param1, Object... additionalParams) {
        // If there is an instance of a string (ICAOID) this method will be called
        if (param1 instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Airport) {
            String ICAOID = (String) param1;
            Airport updatedAirport = (Airport) additionalParams[0];
            
            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).getICAO().equals(ICAOID)) {
                    airports.set(i, updatedAirport);
                    break;
                }
            }
            saveAirports();
        }
        // If there is an instance of a scanner (user input) this method will be called
        else if (param1 instanceof Scanner) {
            Scanner scanner = (Scanner) param1;
            int start = 0;
            int batchSize = 10;
            boolean moreAirports = true;
    
            while (moreAirports) {
                // Display a batch of airports
                for (int i = start; i < start + batchSize && i < airports.size(); i++) {
                    Airport airport = airports.get(i);
                    System.out.println((i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAO() + ")");
                }
    
                System.out.print("Enter the number of the airport to modify, type 'more' to see more airports, or type 'search' to search by ICAOID: ");
                String input = scanner.nextLine();
    
                // If the user inputs "more" or "search" it will show more airports or search for an airport
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= airports.size()) {
                        System.out.println("No more airports to display.");
                        continue;
                    }
                    start += batchSize;

                // If the user inputs "search" it will search for an airport (by ICAOID)
                } else if (input.equalsIgnoreCase("search")) {
                    System.out.print("Enter ICAO ID to search: ");
                    String ICAOID = scanner.nextLine();
                    Airport airport = searchAirport(ICAOID);
                    if (airport == null) {
                        System.out.println("Airport not found.");
                        continue;
                    }
                    
                    // Inline functionality to modify airport details
                    // If the airport is found via search, it will ask for new details
                    System.out.print("Enter new Airport ID (current: " + airport.getAirportID() + "): ");
                    String airportID = scanner.nextLine();

                    System.out.print("Enter new Airport Name (current: " + airport.getAirportName() + "): ");
                    String name = scanner.nextLine();

                    System.out.print("Enter new Latitude (current: " + airport.getLatitude() + "): ");
                    double latitude = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter new Longitude (current: " + airport.getLongitude() + "): ");
                    double longitude = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter new Elevation (current: " + airport.getElevation() + "): ");
                    double elevation = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter new Radio Frequencies (current: " + airport.getRadioFrequencies() + "): ");
                    Double radioFrequencies = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter new Region/State (current: " + airport.getRegionState() + "): ");
                    String regionState = scanner.nextLine();

                    System.out.print("Enter new Region Abbreviation (current: " + airport.getRegionAbbr() + "): ");
                    String regionAbbr = scanner.nextLine();

                    System.out.print("Enter new City (current: " + airport.getCity() + "): ");
                    String city = scanner.nextLine();

                    System.out.print("Enter new ICAO Code (current: " + airport.getICAO() + "): ");
                    String newICAO = scanner.nextLine();

                    System.out.print("Enter new IATA Code (current: " + airport.getIataCode() + "): ");
                    String iataCode = scanner.nextLine();

                    System.out.print("Enter new Fuel Types (current: " + airport.getFuelTypes() + "): ");
                    Double fuelType = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
    
                    // Updates the airport with new details
                    Airport updatedAirport = new Airport(airportID, name, latitude, longitude, elevation, radioFrequencies, regionState, regionAbbr, city, newICAO, iataCode, fuelType);
                    modifyAirport(airport.getICAO(), updatedAirport);
                    System.out.println("Airport modified successfully!");
                    
                    moreAirports = false; // Exit the loop after modification
                } else {
                    // Functionality to modify airport details if the user inputs a number as a selection
                    try {
                        int index = Integer.parseInt(input) - 1;
    
                        if (index < 0 || index >= airports.size()) {
                            System.out.println("Invalid selection.");
                            continue;
                        }
    
                        Airport airport = airports.get(index);
                        
                        // Inline functionality to modify airport details
                        System.out.print("Enter new Airport ID (current: " + airport.getAirportID() + "): ");
                        String airportID = scanner.nextLine();

                        System.out.print("Enter new Airport Name (current: " + airport.getAirportName() + "): ");
                        String name = scanner.nextLine();

                        System.out.print("Enter new Latitude (current: " + airport.getLatitude() + "): ");
                        double latitude = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter new Longitude (current: " + airport.getLongitude() + "): ");
                        double longitude = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter new Elevation (current: " + airport.getElevation() + "): ");
                        double elevation = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter new Radio Frequencies (current: " + airport.getRadioFrequencies() + "): ");
                        Double radioFrequencies = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter new Region/State (current: " + airport.getRegionState() + "): ");
                        String regionState = scanner.nextLine();

                        System.out.print("Enter new Region Abbreviation (current: " + airport.getRegionAbbr() + "): ");
                        String regionAbbr = scanner.nextLine();

                        System.out.print("Enter new City (current: " + airport.getCity() + "): ");
                        String city = scanner.nextLine();

                        System.out.print("Enter new ICAO Code (current: " + airport.getICAO() + "): ");
                        String newICAO = scanner.nextLine();

                        System.out.print("Enter new IATA Code (current: " + airport.getIataCode() + "): ");
                        String iataCode = scanner.nextLine();

                        System.out.print("Enter new Fuel Types (current: " + airport.getFuelTypes() + "): ");
                        Double fuelType = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        
                        // Updates the airport with new details
                        Airport updatedAirport = new Airport(airportID, name, latitude, longitude, elevation, radioFrequencies, regionState, regionAbbr, city, newICAO, iataCode, fuelType);
                        modifyAirport(airport.getICAO(), updatedAirport);
                        System.out.println("Airport modified successfully!");
                        
                        moreAirports = false; // Exit the loop after modification
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                    }
                }
            }
        }
    }

    // Displays the airport information by ICAOID or name
    public void displayAirport(Scanner scanner) {
        System.out.print("Search by (1) ICAO ID or (2) Airport Name: ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Airport airport = null;
        if (searchChoice == 1) {
            System.out.print("Enter ICAO ID of the airport to display: ");
            String ICAOID = scanner.nextLine();
            airport = searchAirport(ICAOID);
        } else if (searchChoice == 2) {
            System.out.print("Enter Airport Name to display: ");
            String airportName = scanner.nextLine();
            airport = searchAirport(airportName, false);
        } else {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        if (airport != null) {
            System.out.println("\nAirport ID: " + airport.getAirportID());
            System.out.println("Airport Name: " + airport.getAirportName());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Elevation: " + airport.getElevation());
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
            System.out.println("Region/State: " + airport.getRegionState());
            System.out.println("Region Abbreviation: " + airport.getRegionAbbr());
            System.out.println("City: " + airport.getCity());
            System.out.println("ICAO Code: " + airport.getICAO());
            System.out.println("IATA Code: " + airport.getIataCode());
            System.out.println("Fuel Types: " + airport.getFuelTypes());
        } else {
            System.out.println("Airport not found.");
        }
    }

    // Searches for an airport by ICAOID or name
    public Airport searchAirport(Object searchParam, Object... additionalParams) {
        // If there is an instance of a scanner (user input) this method will be called
        if (searchParam instanceof Scanner) {
            Scanner scanner = (Scanner) searchParam;
            System.out.print("Search by (1) ICAO ID or (2) Airport Name: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Airport airport = null;
            switch (searchChoice) {
                case 1:
                    System.out.print("Enter ICAO ID of the airport to search: ");
                    String ICAOID = scanner.nextLine();
                    airport = searchAirport(ICAOID, true);
                    break;
                case 2:
                    System.out.print("Enter Airport Name to search: ");
                    String airportName = scanner.nextLine();
                    airport = searchAirport(airportName, false);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return null;
            }

            // Display results if found
            if (airport != null) {
                System.out.println("\nAirport ID: " + airport.getAirportID());
                System.out.println("Airport Name: " + airport.getAirportName());
                System.out.println("Latitude: " + airport.getLatitude());
                System.out.println("Longitude: " + airport.getLongitude());
                System.out.println("Elevation: " + airport.getElevation());
                System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
                System.out.println("Region/State: " + airport.getRegionState());
                System.out.println("Region Abbreviation: " + airport.getRegionAbbr());
                System.out.println("City: " + airport.getCity());
                System.out.println("ICAO Code: " + airport.getICAO());
                System.out.println("IATA Code: " + airport.getIataCode());
                System.out.println("Fuel Types: " + airport.getFuelTypes());
            } else {
                System.out.println("Airport not found.");
            }
            return airport;
        }
        
        // If there is an instance of a string (ICAOID) this method will be called
        else if (searchParam instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
            String identifier = (String) searchParam;
            boolean isICAOID = (Boolean) additionalParams[0];
            
            for (Airport airport : airports) {
                if (isICAOID) {
                    if (airport.getICAO().equalsIgnoreCase(identifier)) {
                        return airport;
                    }
                } else {
                    if (airport.getAirportName().equalsIgnoreCase(identifier)) {
                        return airport;
                    }
                }
            }
            return null;
        }
        
        // Original method for compatibility with previous code
        else if (searchParam instanceof String) {
            String ICAOID = (String) searchParam;
            return searchAirport(ICAOID, true);
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
        }
        else if (scannerOrIcaoId instanceof String) {
            String ICAOID = (String) scannerOrIcaoId;
            airports.removeIf(airport -> airport.getICAO().equalsIgnoreCase(ICAOID));
            saveAirports();
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
    public Double getFrequencies(String ICAOID) {
        Airport airport = searchAirport(ICAOID);
        return airport != null ? airport.getRadioFrequencies() : null;
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
                        String airportID = parts[0];
                        String airportName = parts[1];
                        Double latitude = Double.parseDouble(parts[2]);
                        Double longitude = Double.parseDouble(parts[3]);
                        Double elevation = Double.parseDouble(parts[4]);
                        Double radioFrequencies = Double.parseDouble(parts[5]);
                        String regionState = parts[6];
                        String regionAbbr = parts[7];
                        String city = parts[8];
                        String ICAO = parts[9];
                        String iataCode = parts[10];
                        Double fuelTypes = Double.parseDouble(parts[11]);
                        
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
                writer.println(airport.getAirportID() + "," +
                              airport.getAirportName() + "," +
                              airport.getLatitude() + "," +
                              airport.getLongitude() + "," +
                              airport.getElevation() + "," +
                              airport.getRadioFrequencies() + "," +
                              airport.getRegionState() + "," +
                              airport.getRegionAbbr() + "," +
                              airport.getCity() + "," +
                              airport.getICAO() + "," +
                              airport.getIataCode() + "," +
                              airport.getFuelTypes());
            }
        } catch (IOException e) {
            System.out.println("Error saving airports: " + e.getMessage());
        }
    }
}