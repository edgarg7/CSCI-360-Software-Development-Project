package src;
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

    /*
     * addAirport provides two functionalities:
     * 1. Directly add an Airport object to the list.
     * 2. Interactively collect airport details from the user via a Scanner object.
     * 
     * @param airportOrScanner - Either an Airpoort object or a Scanner for user input.
     */
    public void addAirport(Object airportOrScanner) {
        // Case 1: If we receive an Airport object directly, add it to the list and save the file. 
        if (airportOrScanner instanceof Airport) {
            Airport airport = (Airport) airportOrScanner;
            airports.add(airport);
            saveAirports();
        } 
        // Case 2: If we receive a Scanner object, we will prompt the user for input.
        else if (airportOrScanner instanceof Scanner) {
            Scanner scanner = (Scanner) airportOrScanner;
            try {
                // Collect string inputs
                // Each call to getStringInput will prompt the user and validate non-empty input using the handler method.
                System.out.println();
                String airportName = getStringInput(scanner, "Enter Airport Name: ");
                String regionState = getStringInput(scanner, "Enter Region/State: ");
                String regionAbbr = getStringInput(scanner, "Enter Region Abbreviation: ");
                String city = getStringInput(scanner, "Enter City: ");
                String ICAO = getStringInput(scanner, "Enter ICAO Code: ");

                // Check if ICAO already exists.
                for (Airport existingAirport : airports) {
                    if (existingAirport.getICAO().equalsIgnoreCase(ICAO)) {
                        throw new IllegalArgumentException("An airport with ICAO code " + ICAO + " already exists");
                    }
                }

                // Collect numeric inputs with validation of range.
                // For latitude/longitude I added ranges to check if the user input is valid. Latitude is between -90 and 90, and Longitude is between -180 and 180.
                double latitude = getDoubleInput(scanner, "Enter Latitude (-90 to 90): ", (double) -90, (double) 90);
                double longitude = getDoubleInput(scanner, "Enter Longitude (-180 to 180): ", (double) -180, (double) 180);

                // I also checked the range of radio frequencies from the average radio frequencies of airports in the United States.
                double radioFrequencies = getDoubleInput(scanner, "Enter Radio Frequencies: ", (double) 100, (double) 400);

                // I wanted to use a numeric input for the fuel types, so I used a double to represent the fuel types. 
                // This should make it easier to understand and use in the code.
                double fuelTypes = getDoubleInput(scanner, "Enter Fuel Types (1=AVGAS, 2=Jet A, 3=both): ", (double) 1, (double) 3);

                // Create a new airport object and add it to the list
                Airport newAirport = new Airport(airportName, latitude, longitude,
                        radioFrequencies, regionState, regionAbbr, city,
                        ICAO, fuelTypes);
                airports.add(newAirport);
                saveAirports();
                System.out.println("\nAirport added successfully!");

            // Handle exceptions for invalid input
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Airport addition cancelled.");
            }
        }
    }

    /*
     * This is the handler method for validation of the string inputs used in addAirport.
     * If the input is empty, it throws an IllegalArgumentException.
     * If the input contains only digits, it prompts the user to enter a valid string.
     */
    private String getStringInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be empty.");
            }
            
            // Check if input contains only digits
            if (input.matches("^\\d+$")) {
                System.out.println("Error: Input cannot contain only numbers for this field. Please try again.");
                continue;
            }
            
            return input;
        }
    }

    /*
     * This is the handler method for validation of the numeric inputs used in addAirport.
     * It checks if the input is a valid number and within the specified range.
     * If the input is invalid, it prompts the user again.
     * The method also ensures if the input can be parsed as a double.
     */
    private double getDoubleInput(Scanner scanner, String prompt, Double min, Double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                double value = Double.parseDouble(input);

                // Check range if min and max are provided
                if (min != null && value < min) {
                    System.out.println("Error: Value must be at least " + min);
                    continue; // Prompt again
                }
                if (max != null && value > max) {
                    System.out.println("Error: Value must be at most " + max);
                    continue; // Prompt again
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    /*
     * modifyAirport provides two functionalities:
     * 1. Modification of a existing airport via ICAO 
     * 2. Modificaiton of a existing airport via Scanner with text input.
     * 
     * @param param1 - Either a String (ICAO) or Scanner for user input.
     * @param additionalParams - Optional Airport object for using programmatically.
     */
    public void modifyAirport(Object param1, Object... additionalParams) {
        // Case 1: Programmatic modification.
        if (param1 instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Airport) {
            String ICAOID = (String) param1;
            Airport updatedAirport = (Airport) additionalParams[0];

            // Find and replace the airport with matching ICAO.
            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).getICAO().equalsIgnoreCase(ICAOID)) {
                    airports.set(i, updatedAirport);
                    saveAirports();
                    break;
                }
            }
        } 
        // Case 2: Interactive modification via UI.
        else if (param1 instanceof Scanner) {
            Scanner scanner = (Scanner) param1;
            int start = 0; // Starting index for displaying airports.
            int batchSize = 10; // Number of airports to display at a time.
            boolean moreAirports = true; // Flag to control the loop.

            while (moreAirports) {
                // Display airports in batches for user selection.
                // Shows airport name and ICAO code with index number.
                for (int i = start; i < start + batchSize && i < airports.size(); i++) {
                    Airport airport = airports.get(i);
                    System.out.println("\n" + (i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAO() + ")");
                }

                // Prompt for the user to select an airport, display more airports, or search for a airport.
                System.out.print("\nEnter the number of the airport to modify, type 'more' to see more airports, or type 'search' to search by ICAO: ");
                String input = scanner.nextLine();

                // If the user enters "more", show the next batch of airports.
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= airports.size()) {
                        // End of list.
                        System.out.println("No more airports to show.");
                        continue;
                    }
                    start += batchSize; // Move to the next page.

                // If the user enters "search", prompt for ICAO code.
                } else if (input.equalsIgnoreCase("search")) {
                    try {
                        // Get ICAO to search with.
                        String ICAO = getStringInput(scanner, "Enter ICAO code to search: ");

                        // Search for matching airport.
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

                        // Found airport, display details and modify.
                        modifyAirportDetails(scanner, foundAirport);
                        moreAirports = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                
                // If the user enters a number, parse the number and modify the corresponding airport.
                } else {
                    try {
                        // Convert input to an array index.
                        int index = Integer.parseInt(input) - 1;

                        // Check if the index is valid / within the bounds.
                        if (index < 0 || index >= airports.size()) {
                            System.out.println("Invalid selection");
                            continue;
                        }

                        // Modify the selected airport.
                        Airport airport = airports.get(index);
                        modifyAirportDetails(scanner, airport);
                        moreAirports = false; // Exit the loop after modification.
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                    }
                }
            }
        }
    }

    /* 
     * Helper method that handles the detailed modification of an airport.
     * Shows the current values and collects the new values from the user with validation.
     * 
     * @param scanner - Scanner for user input.
     * @param airport - Airport object to be modified.
     */
    private void modifyAirportDetails(Scanner scanner, Airport airport) {
        try {
            // For each field, show the current value and collect new value (or keep the current).
            // All string inputs use the helper method getStringInputWithDefault that supports keeping current value.
            String airportName = getStringInputWithDefault(scanner, "Enter new Airport Name (current: " + airport.getAirportName() + "): ", airport.getAirportName());
            String regionState = getStringInputWithDefault(scanner, "Enter new Region/State (current: " + airport.getRegionState() + "): ", airport.getRegionState());
            String regionAbbr = getStringInputWithDefault(scanner, "Enter new Region Abbreviation (current: " + airport.getRegionAbbr() + "): ", airport.getRegionAbbr());
            String city = getStringInputWithDefault(scanner, "Enter new City (current: " + airport.getCity() + "): ", airport.getCity());

            // ICAO code requires special handling to check for duplicates.
            String ICAO = airport.getICAO(); // Store original ICAO
            String newICAO = getStringInputWithDefault(scanner, "Enter new ICAO Code (current: " + ICAO + "): ", ICAO);

            // If the ICAO changed, check for duplicates.
            if (!newICAO.equalsIgnoreCase(ICAO)) {
                for (Airport existingAirport : airports) {
                    if (existingAirport != airport && existingAirport.getICAO().equalsIgnoreCase(newICAO)) {
                        throw new IllegalArgumentException("An airport with ICAO code " + newICAO + " already exists");
                    }
                }
            }

            // Numeric inputs with range validation.
            // Shows current value and uses another helper method getDoubleInputWithDefault that supports keeping current value.
            double latitude = getDoubleInputWithDefault(scanner, "Enter new Latitude (-90 to 90) (current: " + airport.getLatitude() + "): ",
                    airport.getLatitude(), -90.0, 90.0);
            double longitude = getDoubleInputWithDefault(scanner, "Enter new Longitude (-180 to 180) (current: " + airport.getLongitude() + "): ",
                    airport.getLongitude(), -180.0, 180.0);
            double radioFrequencies = getDoubleInputWithDefault(scanner, "Enter new Radio Frequencies (current: " + airport.getRadioFrequencies() + "): ",
                    airport.getRadioFrequencies(), null, null);
            double fuelTypes = getDoubleInputWithDefault(scanner, "Enter new Fuel Types (1=AVGAS, 2=Jet A, 3=both) (current: " + airport.getFuelTypes() + "): ",
                    airport.getFuelTypes(), 1.0, 3.0);

            // Create new airport with updated details and update the collection.
            Airport updatedAirport = new Airport(airportName, latitude, longitude,
                    radioFrequencies, regionState, regionAbbr, city,
                    newICAO, fuelTypes);

            // Update the airport in the list.
            for (int i = 0; i < airports.size(); i++) {
                if (airports.get(i).getICAO().equalsIgnoreCase(ICAO)) {
                    airports.set(i, updatedAirport);
                    break;
                }
            }

            // Save changes to the file.
            saveAirports();
            System.out.println("\nAirport updated successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nAirport modification cancelled.");
        }
    }

    /*
     * Helper method for collecting string input that supports keeping the current value.
     * Returns the default value if the user input is empty.
     * Validates that the string input does not contain only digits.
     */
    private String getStringInputWithDefault(Scanner scanner, String prompt, String defaultValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            // Return default value if input is empty
            if (input.isEmpty()) {
                return defaultValue;
            }
            
            // Check if input contains only digits
            if (input.matches("^\\d+$")) {
                System.out.println("Error: Input cannot contain only numbers for this field. Please try again.");
                continue;
            }
            
            return input;
        }
    }

    /*
     * Helper method for collecting numeric input that supports keeping the existing value.
     * - Returns the default value if the user input is empty.
     * - Validates numeric format.
     * - Uses range constraints if specified.
     * - Will continue to prompt until a valid input is received.
     */
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

                // Check range if min and max are provided.
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

    /*
     * Displays detailed information about an airport with two options:
     * 1. Directly display details using an Airport object.
     * 2. Search for an airport using a Scanner object.
     * 
     * @param param - Either a Airport object or a Scanner for user input.
     */
    public void displayAirport(Object param) {
        Airport airport = null;

        // Case 1: If we receive a Scanner, search for the airport first.
        if (param instanceof Scanner) {
            // Start of the search process.
            System.out.println("\n[[--Airport Search--]]");
            Scanner scanner = (Scanner) param;

            // Pass 'true' flag to searchAirport to indicate the call is from displayAirport.
            // This will prevent display duplication of the airport details.
            airport = searchAirport(scanner, true);

        // Case 2: Directly display the airport.
        } else if (param instanceof Airport) {
            airport = (Airport) param;
        }

        // Display the airport details if found.
        if (airport != null) {
            System.out.println("\n[[--Airport Details--]]");
            System.out.println("Airport Name: " + airport.getAirportName());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
            System.out.println("Region/State: " + airport.getRegionState());
            System.out.println("Region Abbreviation: " + airport.getRegionAbbr());
            System.out.println("City: " + airport.getCity());
            System.out.println("ICAO Code: " + airport.getICAO());

            // Display fuel types with human-readable format.
            // Converts numeric codes to text labels.
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
        } 
        // Handler for when no airport is found/provided.
        else {
            System.out.println("No airport found to display.");
        }
    }

    /*
     * Two search functionalities:
     * 1. Interactive search via user input (Scanner).
     * 2. Programmatic search by ICAO or name (String).
     * 
     * Also supports partial matching and handles multiple search results.
     * 
     * @param searchParam - Either a Sanner for interactive search or String for direct search
     * @param additionalParams - Optional parameters for programmatic search:
     *                        - Boolean: If true, it will indicate that a call is from displayAirport.
     *                        - Boolean: If false when searchParam is a String, it will enable secondary search by name.
     */
    public Airport searchAirport(Object searchParam, Object... additionalParams) {
        // Case 1: Interactive search using Scanner. I made a 5 step process.
        if (searchParam instanceof Scanner) {
            Scanner scanner = (Scanner) searchParam;

            // Step 1: Get search mode from user (ICAO or name).
            // Use validation to ensure the user enters a valid choice (1 or 2).
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

            // Step 2: Initialize the search variables.
            List<Airport> matchingAirports = new ArrayList<>(); // Dynamic collection for results.
            String searchTerm = "";
            boolean searchByICAO = false;

            // Step 3: Collect search term based upon the chosen search mode
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
                    // Invalid choice, should not reach here due to earlier validation.
                    System.out.println("Invalid choice. Please try again.");
                    return null;
            }

            // Step 4: Perform search with partial matching. 
            for (Airport airport : airports) {
                if (searchByICAO) {
                    // Case-insensitive, partial match for ICAO.
                    if (airport.getICAO().toUpperCase().contains(searchTerm)) {
                        matchingAirports.add(airport);
                    }
                } else {
                    // Case-insensitive, partial match for Airport Name.
                    if (airport.getAirportName().toLowerCase().contains(searchTerm)) {
                        matchingAirports.add(airport);
                    }
                }
            }

            // Step 5: Process search results based on number of matches.
            // This can have multiple result "types".

            // Type 1: No matches found.
            if (matchingAirports.isEmpty()) {
                System.out.println("No airports found matching your search.");
                return null;
            } 

            // Type 2: One match found.
            else if (matchingAirports.size() == 1) {
                // Only one match found
                Airport foundAirport = matchingAirports.get(0);

                // Check if the search is called from displayAirport.
                // The flag prevents duplicate display of airport details.
                boolean calledFromDisplayAirport = false;
                if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                    calledFromDisplayAirport = (Boolean) additionalParams[0];
                }

                // Only display details here if it is NOT called from displayAirport.
                if (!calledFromDisplayAirport) {
                    displayAirport(foundAirport);
                }

                return foundAirport;
            } 
            
            // Type 3: Multiple matches found. 
            else {
                // Show list of matches with index numbers.
                System.out.println("\nFound " + matchingAirports.size() + " matching airports:");
                for (int i = 0; i < matchingAirports.size(); i++) {
                    Airport airport = matchingAirports.get(i);
                    System.out.println((i + 1) + ". " + airport.getAirportName() + " (" + airport.getICAO() + ")");
                }

                // Continuously prompt until valid selection is made
                Airport selectedAirport = null;
                boolean validSelection = false;

                while (!validSelection) {
                    // Prompt the user to select from the multiple results.
                    System.out.print("\nEnter the number of the airport to select: ");
                    try {
                        int selection = Integer.parseInt(scanner.nextLine().trim());

                        // Validate selection range.
                        if (selection >= 1 && selection <= matchingAirports.size()) {
                            selectedAirport = matchingAirports.get(selection - 1);
                            validSelection = true;
                        } else {
                            System.out.println("Invalid selection. Please enter a number between 1 and " + matchingAirports.size() + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }

                // Check if called from displayAirport.
                boolean calledFromDisplayAirport = false;
                if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                    calledFromDisplayAirport = (Boolean) additionalParams[0];
                }

                // Only display details here if it is NOT called from displayAirport.
                if (!calledFromDisplayAirport) {
                    displayAirport(selectedAirport);
                }

                return selectedAirport;
            }
        }
        // Case 2: Programmatic search by String.
        else if (searchParam instanceof String) {
            String searchValue = (String) searchParam;

            // Primary search by exact ICAO match (case-insensitve).
            for (Airport airport : airports) {
                if (airport.getICAO().equalsIgnoreCase(searchValue)) {
                    return airport;
                }
            }

            // Secondary search by exact Airport Name match (if enabled by flag).
            // This will only run if the first program is false (or non-Boolean).
            if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean && !(Boolean) additionalParams[0]) {
                for (Airport airport : airports) {
                    if (airport.getAirportName().equalsIgnoreCase(searchValue)) {
                        return airport;
                    }
                }
            }
        }

        // If no match is found, return null.
        return null;
    }

    /*
     * Removes an airport with two functionalities:
     * 1. Interactive removal with user confirmation.
     * 2. Direct programmatic removal
     * 
     * @param scannerOrIcaoId - Either a Scanner for interactive removal or String ICAO ID
     */
    public void removeAirport(Object scannerOrIcaoId) {
        // Case 1: Interactive removal using Scanner.
        if (scannerOrIcaoId instanceof Scanner) {
            Scanner scanner = (Scanner) scannerOrIcaoId;
            System.out.print("Enter ICAO ID of the airport to remove: ");
            String ICAOID = scanner.nextLine();
            Airport airport = searchAirport(ICAOID);
            
            if (airport != null) {
                // Display airport details so user knows what they're removing.
                System.out.println("\nYou are about to remove the following airport:");
                displayAirport(airport);
                
                // Ask for confirmation.
                System.out.print("\nAre you sure you want to remove this airport? (Y/N): ");
                String confirmation = scanner.nextLine().trim();
                
                if (confirmation.equalsIgnoreCase("Y")) {
                    // Call same method with ICAOID to perform removal.
                    removeAirport(ICAOID);
                    System.out.println("\nAirport removed successfully!");
                } else {
                    System.out.println("\nAirport removal cancelled.");
                }
            } else {
                System.out.println("Airport not found.");
            }
        } 
        // Case 2: Programmatic removal using ICAO ID.
        else if (scannerOrIcaoId instanceof String) {
            String ICAOID = (String) scannerOrIcaoId;
            airports.removeIf(airport -> airport.getICAO().equalsIgnoreCase(ICAOID));
            saveAirports();
        }
    }
    
    /*
     * Loads airport data from a CSV or text file. (Please DO NOT change "FILE_NAME", everything WILL BREAK.
     *                                              If you need to change the file, please structure the same way as "Airport_Database.csv")
     * Handles file parsing, data conversion, and basic validation
     */
    private List<Airport> loadAirports() {
        List<Airport> loadedAirports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true; // Flag to identify and skip header rows
            while ((line = reader.readLine()) != null) {
                // Skip CSV header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                // Skip comment lines or empty lines for better file reading.
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }

                // Parse CSV format - Split line by comma separator.lklk
                String[] parts = line.split(",");
                if (parts.length >= 9) { // Make sure we have all fields
                    try {
                        // Parse for each field
                        String airportName = parts[0];

                        // I use Double.parseDouble to convert from a string to double objects
                        Double latitude = Double.parseDouble(parts[1]);
                        Double longitude = Double.parseDouble(parts[2]);
                        Double radioFrequencies = Double.parseDouble(parts[3]);

                        // Parse the rest of the fields
                        String regionState = parts[4];
                        String regionAbbr = parts[5];
                        String city = parts[6];
                        String ICAO = parts[7];
                        Double fuelTypes = Double.parseDouble(parts[8]);

                        // Create a new Airport object and add it to the list
                        loadedAirports.add(new Airport(airportName, latitude, longitude, radioFrequencies, regionState,
                                regionAbbr, city, ICAO, fuelTypes));
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

    /*
     * Saves the airport data to a CSV or text file.
     */
    private void saveAirports() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // false = overwrite file
            // Write CSV header row with cloumn names for readability and data structure documentation
            // This will help anyone who reads the file to understand the data structure.
            writer.println("Airport Name,Latitude,Longitude,Frequency,Region_State,Region_Abbr.,City,ICAO,Fuel Types");

            // Iterate through the collection and write each airport as a CSV row.
            for (Airport airport : airports) {
                writer.println(airport.getAirportName() + ","       // Full name of the airport
                        + airport.getLatitude() + ","               // Geographical latitude (North/South position)
                        + airport.getLongitude() + ","              // Geographical longitude (East/West position)
                        + airport.getRadioFrequencies() + ","       // Communication frequencies (in MHz)
                        + airport.getRegionState() + ","            // Region or state name
                        + airport.getRegionAbbr() + ","             // Abbreviation of the region/state
                        + airport.getCity() + ","                   // City where the airport is located
                        + airport.getICAO() + ","                   // ICAO code (International Civil Aviation Organization)
                        + airport.getFuelTypes());                  // Fuel types available (1=AVGAS, 2=Jet A, 3=both)
            }
            // File is automatically closed by try-with-resources.
        } catch (IOException e) {
            // Log any file writing errors but allow the program to continue.
            System.out.println("Error saving airports: " + e.getMessage());
            // NOTE: Data will remain even if the save fails.
        }
    }
}