package src;
import java.io.*;
import java.util.*;
/**
 * This class will manage the list of airplanes and handles loading and saving 
 * airplane data from a file.
 */
public class AirplaneManager {
    private final List<Airplane> airplanes;
    private static final String FILE_NAME = "Airplanes_Database.csv";
    
    /**
     * This constructor initializes the airplane list by loading data from file
     */
    public AirplaneManager() {
        this.airplanes = loadAirplanes();
    }
    
    /*
     * 
     * @return the list of airplanes
     */
    public List<Airplane> getAirplanes() { 
        return airplanes; 
    }
    
    /*
     * addAirplane provides two functionalities:
     * 1. Directly add an Airplane object to the list.
     * 2. Interactively collect airplane details from the user via a Scanner object.
     * 
     * @param aiplaneOrScanner - Either an Airplane object or a Scanner for user input.
     */
    public void addAirplane(Object airplaneOrScanner) {
        // Case 1: If we receive an Airplane object directly, add it to the list and save the file.
        if (airplaneOrScanner instanceof Airplane) {
            Airplane airplane = (Airplane) airplaneOrScanner;
            airplanes.add(airplane);
            saveAirplanes();
        }
        // Case 2: If we receive a Scanner object, we will prompt the user for input.
        else if (airplaneOrScanner instanceof Scanner) {
            Scanner scanner = (Scanner) airplaneOrScanner;
            try {
                // Collect string inputs with validation
                System.out.println();
                String makeModel = getStringInput(scanner, "Enter Airplane Make and Model: ");
                
                // Check if the make/model already exists
                for (Airplane existingAirplane : airplanes) {
                    if (existingAirplane.getMakeModel().equalsIgnoreCase(makeModel)) {
                        throw new IllegalArgumentException("An airplane with this make and model already exists");
                    }
                }
                
                String planeType = getStringInput(scanner, "Enter Airplane Type (Jet, Prop, TurboProp): ");
                
                // Collect numeric inputs with validation
                double fuelType = getDoubleInput(scanner, "Enter Fuel Type (1=AVGAS, 2=JetA): ", (double) 1, (double) 2);
                double fuelBurnRate = getDoubleInput(scanner, "Enter Fuel Burn Rate (liters per hour): ", (double) 0.0, (double) 24000);
                double fuelCapacity = getDoubleInput(scanner, "Enter Fuel Capacity (in liters): ", (double) 150, (double) 375000);
                double airspeed = getDoubleInput(scanner, "Enter Airspeed (in knots): ", (double) 8, (double) 600);
    
                // Create a new airplane object and add it to the list
                Airplane newAirplane = new Airplane(makeModel, planeType, fuelType, fuelBurnRate, fuelCapacity, airspeed);
                airplanes.add(newAirplane);
                saveAirplanes();
                System.out.println("\nAirplane added successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Airplane addition cancelled.");
            }
        }
    }
    
    /*
     * This is the handler method for validation of the string inputs used in addAirplane.
     * If the input is empty, it throws an IllegalArgumentException.
     * If the input contains only digits, it prompts the user to enter a valid string.
     * For airplane types, validates that only Jet, Prop, or TurboProp are accepted.
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
            
            // Special validation for airplane type
            if (prompt.contains("Airplane Type")) {
                // Case-insensitive check against valid airplane types
                if (!input.equalsIgnoreCase("Jet") && 
                    !input.equalsIgnoreCase("Prop") && 
                    !input.equalsIgnoreCase("TurboProp")) {
                    System.out.println("Error: Invalid airplane type. Must be 'Jet', 'Prop', or 'TurboProp'. Please try again.");
                    continue;
                }
            }
            
            return input;
        }
    }
    
    /*
     * This is the handler method for validation of the numeric inputs used in addAirplane.
     * It checks if the input is a valid number and within the specified range.
     * If the input is invalid, it prompts the user again.
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
     * modifyAirplane provides two functionalities:
     * 1. Modification of an existing airplane via make/model
     * 2. Modification of an existing airplane via Scanner with text input
     * 
     * @param param1 - Either a String (make/model) or Scanner for user input
     * @param additionalParams - Optional Airplane object for using programmatically
     */
    public void modifyAirplane(Object param1, Object... additionalParams) {
        // Case 1: Programmatic modification
        if (param1 instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Airplane) {
            String makeModel = (String) param1;
            Airplane updatedAirplane = (Airplane) additionalParams[0];

            // Find and replace the airplane with matching make/model
            for (int i = 0; i < airplanes.size(); i++) {
                if (airplanes.get(i).getMakeModel().equalsIgnoreCase(makeModel)) {
                    airplanes.set(i, updatedAirplane);
                    saveAirplanes();
                    break;
                }
            }
        } 
        // Case 2: Interactive modification via UI
        else if (param1 instanceof Scanner) {
            Scanner scanner = (Scanner) param1;
            int start = 0; // Starting index for displaying airplanes
            int batchSize = 10; // Number of airplanes to display at a time
            boolean moreAirplanes = true; // Flag to control the loop

            while (moreAirplanes) {
                // Display airplanes in batches for user selection
                // Shows airplane make/model with index number
                for (int i = start; i < start + batchSize && i < airplanes.size(); i++) {
                    Airplane airplane = airplanes.get(i);
                    System.out.println("\n" + (i + 1) + ". " + airplane.getMakeModel() + " (" + airplane.getPlaneType() + ")");
                }

                // Prompt for the user to select an airplane, display more airplanes, or search
                System.out.print("\nEnter the number of the airplane to modify, type 'more' to see more airplanes, or type 'search' to search by make/model: ");
                String input = scanner.nextLine();

                // If the user enters "more", show the next batch of airplanes
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= airplanes.size()) {
                        // End of list
                        System.out.println("No more airplanes to show.");
                        continue;
                    }
                    start += batchSize; // Move to the next page

                // If the user enters "search", prompt for make/model
                } else if (input.equalsIgnoreCase("search")) {
                    try {
                        // Get make/model to search with
                        String makeModel = getStringInput(scanner, "Enter make and model to search: ");

                        // Search for matching airplane
                        Airplane foundAirplane = null;
                        for (Airplane airplane : airplanes) {
                            if (airplane.getMakeModel().equalsIgnoreCase(makeModel)) {
                                foundAirplane = airplane;
                                break;
                            }
                        }

                        if (foundAirplane == null) {
                            System.out.println("No airplane found with make/model: " + makeModel);
                            continue;
                        }

                        // Found airplane, display details and modify
                        modifyAirplaneDetails(scanner, foundAirplane);
                        moreAirplanes = false;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                
                // If the user enters a number, parse the number and modify the corresponding airplane
                } else {
                    try {
                        // Convert input to an array index
                        int index = Integer.parseInt(input) - 1;

                        // Check if the index is valid / within the bounds
                        if (index < 0 || index >= airplanes.size()) {
                            System.out.println("Invalid selection");
                            continue;
                        }

                        // Modify the selected airplane
                        Airplane airplane = airplanes.get(index);
                        modifyAirplaneDetails(scanner, airplane);
                        moreAirplanes = false; // Exit the loop after modification
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                    }
                }
            }
        }
    }

    /*
     * Helper method that handles the detailed modification of an airplane.
     * Shows the current values and collects the new values from the user with validation.
     * 
     * @param scanner - Scanner for user input
     * @param airplane - Airplane object to be modified
     */
    private void modifyAirplaneDetails(Scanner scanner, Airplane airplane) {
        try {
            // For each field, show the current value and collect new value (or keep the current)
            // All string inputs use the helper method getStringInputWithDefault that supports keeping current value
            String makeModel = airplane.getMakeModel(); // Store original make/model
            String newMakeModel = getStringInputWithDefault(scanner, "Enter new Make and Model (current: " + makeModel + "): ", makeModel);
            
            // If the make/model changed, check for duplicates
            if (!newMakeModel.equalsIgnoreCase(makeModel)) {
                for (Airplane existingAirplane : airplanes) {
                    if (existingAirplane != airplane && existingAirplane.getMakeModel().equalsIgnoreCase(newMakeModel)) {
                        throw new IllegalArgumentException("An airplane with this make and model already exists");
                    }
                }
            }
            
            String planeType = getStringInputWithDefault(scanner, "Enter new Airplane Type (Jet, Prop, TurboProp) (current: " + 
                    airplane.getPlaneType() + "): ", airplane.getPlaneType());
            
            // Numeric inputs with range validation
            // Shows current value and uses helper method getDoubleInputWithDefault that supports keeping current value
            double fuelType = getDoubleInputWithDefault(scanner, "Enter new Fuel Type (1=AVGAS, 2=JetA) (current: " + 
                    airplane.getFuelType() + "): ", airplane.getFuelType(), 1.0, 2.0); 
            double fuelBurnRate = getDoubleInputWithDefault(scanner, "Enter new Fuel Burn Rate (liters per hour) (current: " + 
                    airplane.getFuelBurnRate() + "): ", airplane.getFuelBurnRate(), (double) 1, 24000.0);
            double fuelCapacity = getDoubleInputWithDefault(scanner, "Enter new Fuel Capacity (in liters) (current: " + 
                    airplane.getFuelCapacity() + "): ", airplane.getFuelCapacity(), 150.0, 375000.0);
            double airspeed = getDoubleInputWithDefault(scanner, "Enter new Airspeed (in knots) (current: " + 
                    airplane.getAirspeed() + "): ", airplane.getAirspeed(), 8.0, 600.0);

            // Create new airplane with updated details
            Airplane updatedAirplane = new Airplane(newMakeModel, planeType, fuelType, fuelBurnRate, fuelCapacity, airspeed);

            // Update the airplane in the list
            for (int i = 0; i < airplanes.size(); i++) {
                if (airplanes.get(i).getMakeModel().equalsIgnoreCase(makeModel)) {
                    airplanes.set(i, updatedAirplane);
                    break;
                }
            }

            // Save changes to the file
            saveAirplanes();
            System.out.println("\nAirplane updated successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nAirplane modification cancelled.");
        }
    }

    /*
     * Helper method for collecting string input that supports keeping the current value.
     * Returns the default value if the user input is empty.
     * Validates that the string input does not contain only digits.
     * For airplane types, validates that only Jet, Prop, or TurboProp are accepted.
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
                System.out.println("Current value: " + defaultValue);
                continue;
            }
            
            // Special validation for airplane type
            if (prompt.contains("Airplane Type")) {
                // Case-insensitive check against valid airplane types
                if (!input.equalsIgnoreCase("Jet") && 
                    !input.equalsIgnoreCase("Prop") && 
                    !input.equalsIgnoreCase("TurboProp")) {
                    System.out.println("Error: Invalid airplane type. Must be 'Jet', 'Prop', or 'TurboProp'. Please try again.");
                    System.out.println("Current value: " + defaultValue);
                    continue;
                }
            }
            
            return input;
        }
    }

    /**
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
    
    /*
    * Displays detailed information about an airplane with two options:
    * 1. Directly display details using an Airplane object.
    * 2. Search for an airplane using a Scanner object.
    * 
    * @param param - Either an Airplane object or a Scanner for user input.
    */
    public void displayAirplane(Object param) {
        Airplane airplane = null;

        // Case 1: If we receive a Scanner, search for the airplane first.
        if (param instanceof Scanner) {
            // Start of the search process.
            System.out.println("\n[[--Airplane Search--]]");
            Scanner scanner = (Scanner) param;

            // Pass 'true' flag to searchAirplane to indicate the call is from displayAirplane.
            // This will prevent display duplication of the airplane details.
            airplane = searchAirplane(scanner, true);

        // Case 2: Directly display the airplane.
        } else if (param instanceof Airplane) {
            airplane = (Airplane) param;
        }

        // Display the airplane details if found.
        if (airplane != null) {
            System.out.println("\n[[--Airplane Details--]]");
            System.out.println("Make and Model: " + airplane.getMakeModel());
            System.out.println("Plane Type: " + airplane.getPlaneType());
            
            // Convert numeric fuel type to human-readable format
            String fuelTypeStr = "";
            double fuelTypeValue = airplane.getFuelType();
            if (fuelTypeValue == 1.0) {
                fuelTypeStr = "AVGAS";
            } else if (fuelTypeValue == 2.0) {
                fuelTypeStr = "JetA";
            } else {
                fuelTypeStr = String.valueOf(fuelTypeValue); // Fallback for unknown values
            }
            System.out.println("Fuel Type: " + fuelTypeStr);
            
            System.out.println("Fuel Burn Rate: " + airplane.getFuelBurnRate() + " liters per hour");
            System.out.println("Fuel Capacity: " + airplane.getFuelCapacity() + " liters");
            System.out.println("Airspeed: " + airplane.getAirspeed() + " knots");
        } 
        // Handler for when no airplane is found/provided.
        else {
            System.out.println("No airplane found to display.");
        }
    }

    /*
    * Two search functionalities:
    * 1. Interactive search via user input (Scanner).
    * 2. Programmatic search by make/model or type (String).
    * 
    * Also supports partial matching and handles multiple search results.
    * 
    * @param searchParam - Either a Scanner for interactive search or String for direct search
    * @param additionalParams - Optional parameters for programmatic search:
    *                        - Boolean: If true, it will indicate that a call is from displayAirplane.
    *                        - Boolean: If false when searchParam is a String, it will enable secondary search by plane type.
    */
    public Airplane searchAirplane(Object searchParam, Object... additionalParams) {
        // Case 1: Interactive search using Scanner.
        if (searchParam instanceof Scanner) {
            Scanner scanner = (Scanner) searchParam;

            // Step 1: Get search mode from user (make/model or type).
            // Use validation to ensure the user enters a valid choice (1 or 2).
            int searchChoice = 0;
            boolean validChoice = false;

            while (!validChoice) {
                System.out.print("Search by (1) Make and Model or (2) Plane Type: ");
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
            List<Airplane> matchingAirplanes = new ArrayList<>();
            String searchTerm = "";
            boolean searchByMakeModel = false;

            // Step 3: Collect search term based upon the chosen search mode
            switch (searchChoice) {
                case 1:
                    // Search by make and model
                    System.out.print("Enter full or partial Make and Model to search: ");
                    searchTerm = scanner.nextLine().trim().toLowerCase();
                    searchByMakeModel = true;
                    break;
                case 2:
                    // Search by plane type
                    System.out.print("Enter Plane Type (Jet, Prop, TurboProp) to search: ");
                    searchTerm = scanner.nextLine().trim().toLowerCase();
                    break;
                default:
                    // Invalid choice, should not reach here due to earlier validation.
                    System.out.println("Invalid choice. Please try again.");
                    return null;
            }

            // Step 4: Perform search with partial matching.
            for (Airplane airplane : airplanes) {
                if (searchByMakeModel) {
                    // Case-insensitive, partial match for make and model.
                    if (airplane.getMakeModel().toLowerCase().contains(searchTerm)) {
                        matchingAirplanes.add(airplane);
                    }
                } else {
                    // Case-insensitive, partial match for plane type.
                    if (airplane.getPlaneType().toLowerCase().contains(searchTerm)) {
                        matchingAirplanes.add(airplane);
                    }
                }
            }

            // Step 5: Process search results based on number of matches.
            // Type 1: No matches found.
            if (matchingAirplanes.isEmpty()) {
                System.out.println("No airplanes found matching your search.");
                return null;
            } 
            // Type 2: One match found.
            else if (matchingAirplanes.size() == 1) {
                Airplane foundAirplane = matchingAirplanes.get(0);

                // Check if the search is called from displayAirplane.
                boolean calledFromDisplayAirplane = false;
                if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                    calledFromDisplayAirplane = (Boolean) additionalParams[0];
                }

                // Only display details here if it is NOT called from displayAirplane.
                if (!calledFromDisplayAirplane) {
                    displayAirplane(foundAirplane);
                }

                return foundAirplane;
            } 
            // Type 3: Multiple matches found.
            else {
                // Show list of matches with index numbers.
                System.out.println("\nFound " + matchingAirplanes.size() + " matching airplanes:");
                for (int i = 0; i < matchingAirplanes.size(); i++) {
                    Airplane airplane = matchingAirplanes.get(i);
                    System.out.println((i + 1) + ". " + airplane.getMakeModel() + " (" + airplane.getPlaneType() + ")");
                }

                // Continuously prompt until valid selection is made
                Airplane selectedAirplane = null;
                boolean validSelection = false;

                while (!validSelection) {
                    // Prompt the user to select from the multiple results.
                    System.out.print("\nEnter the number of the airplane to select: ");
                    try {
                        int selection = Integer.parseInt(scanner.nextLine().trim());

                        // Validate selection range.
                        if (selection >= 1 && selection <= matchingAirplanes.size()) {
                            selectedAirplane = matchingAirplanes.get(selection - 1);
                            validSelection = true;
                        } else {
                            System.out.println("Invalid selection. Please enter a number between 1 and " + matchingAirplanes.size() + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }

                // Check if called from displayAirplane.
                boolean calledFromDisplayAirplane = false;
                if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
                    calledFromDisplayAirplane = (Boolean) additionalParams[0];
                }

                // Only display details here if it is NOT called from displayAirplane.
                if (!calledFromDisplayAirplane) {
                    displayAirplane(selectedAirplane);
                }

                return selectedAirplane;
            }
        }
        // Case 2: Programmatic search by String.
        else if (searchParam instanceof String) {
            String searchValue = (String) searchParam;

            // Primary search by exact make/model match (case-insensitive).
            for (Airplane airplane : airplanes) {
                if (airplane.getMakeModel().equalsIgnoreCase(searchValue)) {
                    return airplane;
                }
            }

            // Secondary search by exact plane type match (if enabled by flag).
            if (additionalParams.length > 0 && additionalParams[0] instanceof Boolean && !(Boolean) additionalParams[0]) {
                for (Airplane airplane : airplanes) {
                    if (airplane.getPlaneType().equalsIgnoreCase(searchValue)) {
                        return airplane;
                    }
                }
            }
        }

        // If no match is found, return null.
        return null;
    }

    /**
    * Removes an airplane with two functionalities:
    * 1. Interactive removal with user confirmation.
    * 2. Direct programmatic removal
    * 
    * @param scannerOrMakeModel - Either a Scanner for interactive removal or String Make/Model
    */
    public void removeAirplane(Object scannerOrMakeModel) {
        // Case 1: Interactive removal using Scanner.
        if (scannerOrMakeModel instanceof Scanner) {
            Scanner scanner = (Scanner) scannerOrMakeModel;
            System.out.print("Enter Make and Model of the airplane to remove: ");
            String makeModel = scanner.nextLine();
            Airplane airplane = searchAirplane(makeModel);
            
            if (airplane != null) {
                // Display airplane details so user knows what they're removing.
                System.out.println("\nYou are about to remove the following airplane:");
                displayAirplane(airplane);
                
                // Ask for confirmation.
                System.out.print("\nAre you sure you want to remove this airplane? (Y/N): ");
                String confirmation = scanner.nextLine().trim();
                
                if (confirmation.equalsIgnoreCase("Y")) {
                    // Call same method with makeModel to perform removal.
                    removeAirplane(makeModel);
                    System.out.println("\nAirplane removed successfully!");
                } else {
                    System.out.println("\nAirplane removal cancelled.");
                }
            } else {
                System.out.println("Airplane not found.");
            }
        } 
        // Case 2: Programmatic removal using Make/Model.
        else if (scannerOrMakeModel instanceof String) {
            String makeModel = (String) scannerOrMakeModel;
            airplanes.removeIf(airplane -> airplane.getMakeModel().equalsIgnoreCase(makeModel));
            saveAirplanes();
        }
    }
 

    /**
     * Loads airplane data from a CSV or text file.
     * Handles file parsing, data conversion, and basic validation
     * @return list of airplanes
     */
    private List<Airplane> loadAirplanes() {
        List<Airplane> loadedAirplanes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true; // Flag to identify and skip header rows
            while ((line = reader.readLine()) != null) {
                // Skip CSV header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                // Skip comment lines or empty lines for better file reading
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }

                // Parse CSV format - Split line by comma separator
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Make sure we have all required fields
                    try {
                        // Parse each field
                        String makeModel = parts[0].trim();
                        String planeType = parts[1].trim();
                        
                        // Parse numeric values
                        Double fuelType = Double.parseDouble(parts[2].trim());
                        double fuelBurnRate = Double.parseDouble(parts[3].trim());
                        double fuelCapacity = Double.parseDouble(parts[4].trim());
                        double airspeed = Double.parseDouble(parts[5].trim());

                        // Create a new Airplane object and add it to the list
                        loadedAirplanes.add(new Airplane(makeModel, planeType, fuelType, 
                                                        fuelBurnRate, fuelCapacity, airspeed));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing number in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading airplanes: " + e.getMessage());
        }
        return loadedAirplanes;
    }
    
    /**
     * Saves the airplane data to a CSV or text file.
     */
    private void saveAirplanes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // false = overwrite file
            // Write CSV header row with column names for readability and data structure documentation
            // This will help anyone who reads the file to understand the data structure.
            writer.println("Make and Model,Plane Type,Fuel Type,Fuel Burn Rate,Fuel Capacity,Airspeed");

            // Iterate through the collection and write each airplane as a CSV row.
            for (Airplane airplane : airplanes) {
                writer.println(airplane.getMakeModel() + ","            // Make and model of the airplane
                        + airplane.getPlaneType() + ","                 // Type of airplane (Jet, Prop, TurboProp)
                        + airplane.getFuelType() + ","                  // Type of fuel used (1=AVGAS, 2=JetA)
                        + airplane.getFuelBurnRate() + ","              // Fuel consumption rate in liters per hour
                        + airplane.getFuelCapacity() + ","              // Total fuel capacity in liters
                        + airplane.getAirspeed());                      // Cruising airspeed in knots
            }
            // File is automatically closed by try-with-resources.
        } catch (IOException e) {
            // Log any file writing errors but allow the program to continue.
            System.out.println("Error saving airplanes: " + e.getMessage());
            // NOTE: Data will remain in memory even if the save fails.
        }
    }
}
