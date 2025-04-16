import java.io.*;
import java.util.*;
/**
 * This class will manage the list of airplanes and handles loading and saving 
 * airplane data from a file.
 */
public class AirplaneManager {
    private final List<Airplane> airplanes;
    private static final String FILE_NAME = "airplanes.txt";
    
    /**
     * This constructor initializes the airplane list by loading data from file
     */
    public AirplaneManager() {
        this.airplanes = loadAirplanes();
    }
    
    /**
     * 
     * @return the list of airplanes
     */
    public List<Airplane> getAirplanes() { 
        return airplanes; 
    }
    
    /**
     * 
     * @param airplane will add an airplane to the list and save to file
     */
    public void addAirplane(Object airplaneOrScanner) {
    	if (airplaneOrScanner instanceof Airplane) {
        Airplane airplane = (Airplane) airplaneOrScanner;
        airplanes.add(airplane);
    	saveAirplanes();
    }
        else if (airplaneOrScanner instanceof Scanner) {
        Scanner scanner = (Scanner) airplaneOrScanner;
        System.out.print("Enter Airplane Make and Model: ");
        String makeModel = scanner.nextLine();
        System.out.print("Enter Airplane Type (Jet, Prop, TurboProp): ");
        String planeType = scanner.nextLine();
        System.out.print("Enter Fuel Type: ");
        String fuelType = scanner.nextLine();
        System.out.print("Enter Max Range: ");
        double maxRange = scanner.nextDouble(); // We may not need this if we can make a method to calculate it with the other data
        System.out.print("Enter Fuel Burn Rate: ");
        double fuelBurnRate = scanner.nextDouble();
        System.out.print("Enter Fuel Capacity: ");
        double fuelCapacity = scanner.nextDouble();
        System.out.print("Enter Airspeed: ");
        double airspeed = scanner.nextDouble();
        scanner.nextLine();

        Airplane newAirplane = new Airplane(makeModel, planeType, fuelType, maxRange, fuelBurnRate, fuelCapacity, airspeed);
        airplanes.add(newAirplane);
        saveAirplanes();
        System.out.println("Airplane added successfully!");
        }
    }
    
    // Modifyes an airplane using user input
    public void modifyAirplane(Object param1, Object... additionalParams) {
        if (param1 instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Airplane) {
            String makeModel = (String) param1;
            Airplane updatedAirplane = (Airplane) additionalParams[0];
    
            for (int i = 0; i < airplanes.size(); i++) {
                if (airplanes.get(i).getMakeModel().equals(makeModel)) {
                    airplanes.set(i, updatedAirplane);
                    break;
                }
            }
            saveAirplanes();
        }
    
        else if (param1 instanceof Scanner) {
            Scanner scanner = (Scanner) param1;
            int start = 0;
            int batchSize = 10;
            boolean moreAirplanes = true;  
    
            while (moreAirplanes) {
                // Display a batch of airplanes
                for (int i = start; i < start + batchSize && i < airplanes.size(); i++) {
                    Airplane airplane = airplanes.get(i);
                    System.out.println((i + 1) + ". " + airplane.getMakeModel() + " (" + airplane.getPlaneType() + ")");
                }
    
                System.out.print("Enter the number of the airplane to modify, type 'more' to see more airplanes, or type 'search' to search by plane type: ");
                String input = scanner.nextLine();
    
                // If the user inputs "more" or "search" it will show more airplanes or search for an airplane
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= airplanes.size()) {
                        System.out.println("No more airplanes to show.");
                        continue;
                    } 
                    start += batchSize;
    
                } else if (input.equalsIgnoreCase("search")) {
                    System.out.print("Enter Plane Type to search: ");
                    String planeType = scanner.nextLine();
                    
                    // Find all airplanes of the specified type
                    List<Airplane> matchingPlanes = new ArrayList<>();
                    for (Airplane plane : airplanes) {
                        if (plane.getPlaneType().equalsIgnoreCase(planeType)) {
                            matchingPlanes.add(plane);
                        }
                    }
                    
                    if (matchingPlanes.isEmpty()) {
                        System.out.println("No airplanes found with that plane type.");
                        continue;
                    }
                    
                    // Display matching airplanes in batches of 10
                    int searchStart = 0;
                    boolean searchContinue = true;
                    
                    while (searchContinue) {
                        // Display current batch of search results
                        for (int i = searchStart; i < Math.min(searchStart + batchSize, matchingPlanes.size()); i++) {
                            Airplane plane = matchingPlanes.get(i);
                            System.out.println((i + 1) + ". " + plane.getMakeModel() + " (" + plane.getPlaneType() + ")");
                        }
                        
                        // If we've displayed all planes, don't offer "more"
                        if (searchStart + batchSize >= matchingPlanes.size()) {
                            System.out.print("Enter the number of the airplane to modify: ");
                        } else {
                            System.out.print("Enter the number of the airplane to modify or type 'more' to see more: ");
                        }
                        
                        String searchInput = scanner.nextLine();
                        
                        if (searchInput.equalsIgnoreCase("more")) {
                            if (searchStart + batchSize >= matchingPlanes.size()) {
                                System.out.println("No more airplanes to show.");
                            } else {
                                searchStart += batchSize;
                            }
                        } else {
                            try {
                                int index = Integer.parseInt(searchInput) - 1;
                                if (index >= 0 && index < matchingPlanes.size()) {
                                    Airplane airplane = matchingPlanes.get(index);
                                    
                                    System.out.print("Enter new Airplane Make and Model (current : " + airplane.getMakeModel() + "): ");
                                    String makeModel = scanner.nextLine();
                                    System.out.print("Enter new Airplane Type (current: " + airplane.getPlaneType() + "): ");
                                    String newPlaneType = scanner.nextLine();
                                    System.out.print("Enter new Fuel Type (current: " + airplane.getFuelType() + "): ");
                                    String fuelType = scanner.nextLine(); 
                                    System.out.print("Enter new Max Range (current: " + airplane.getMaxRange() + "): ");
                                    Double maxRange = scanner.nextDouble();
                                    System.out.print("Enter new Fuel Burn Rate (current: " + airplane.getFuelBurnRate() + "): ");
                                    Double fuelBurnRate = scanner.nextDouble();
                                    System.out.print("Enter new Fuel Capacity (current: " + airplane.getFuelCapacity() + "): ");
                                    Double fuelCapacity = scanner.nextDouble();
                                    System.out.print("Enter new Airspeed (current: " + airplane.getAirspeed() + "): ");
                                    Double airspeed = scanner.nextDouble();
                                    scanner.nextLine(); // Consume newline
                                    
                                    // Updates the airplane with new details
                                    Airplane updatedAirplane = new Airplane(makeModel, newPlaneType, fuelType, maxRange, fuelBurnRate, fuelCapacity, airspeed);
                                    modifyAirplane(airplane.getMakeModel(), updatedAirplane);
                                    System.out.println("Airplane updated successfully!");
                                    
                                    searchContinue = false;
                                    moreAirplanes = false;
                                } else {
                                    System.out.println("Invalid selection. Please try again.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number or 'more'.");
                            }
                        }
                    }
                } else {
                    try {
                        int index = Integer.parseInt(input) - 1;
    
                        if (index < 0 || index >= airplanes.size()) {
                            System.out.println("Invalid selection");
                            continue;
                        }
    
                        Airplane airplane = airplanes.get(index);
    
                        System.out.print("Enter new Airplane Make and Model (current : " + airplane.getMakeModel() + "): ");
                        String makeModel = scanner.nextLine();
                        System.out.print("Enter new Airplane Type (current: " + airplane.getPlaneType() + "): ");
                        String planeType = scanner.nextLine();
                        System.out.print("Enter new Fuel Type (current: " + airplane.getFuelType() + "): ");
                        String fuelType = scanner.nextLine(); 
                        System.out.print("Enter new Max Range (current: " + airplane.getMaxRange() + "): ");
                        Double maxRange = scanner.nextDouble();
                        System.out.print("Enter new Fuel Burn Rate (current: " + airplane.getFuelBurnRate() + "): ");
                        Double fuelBurnRate = scanner.nextDouble();
                        System.out.print("Enter new Fuel Capacity (current: " + airplane.getFuelCapacity() + "): ");
                        Double fuelCapacity = scanner.nextDouble();
                        System.out.print("Enter new Airspeed (current: " + airplane.getAirspeed() + "): ");
                        Double airspeed = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
    
                        // Updates the airplane with new details
                        Airplane updatedAirplane = new Airplane(makeModel, planeType, fuelType, maxRange, fuelBurnRate, fuelCapacity, airspeed);
                        modifyAirplane(airplane.getMakeModel(), updatedAirplane);
                        System.out.println("Airplane updated successfully!");
    
                        moreAirplanes = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number, 'more', or 'search'.");
                    }
                }
            }
        }
    }
    
    public void displayAirplane(Scanner scanner) {
        System.out.print("Search by (1) Make and Model or (2) Plane Type: ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        if (searchChoice == 1) {
            // Keep existing logic for Make and Model search
            System.out.print("Enter Make and Model to search: ");
            String makeModel = scanner.nextLine();
            Airplane airplane = searchAirplane(makeModel);
            
            if (airplane != null) {
                System.out.println("\nAirplane Make and Model: " + airplane.getMakeModel());
                System.out.println("Airplane Type: " + airplane.getPlaneType());
                System.out.println("Fuel Type: " + airplane.getFuelType());
                System.out.println("Max Range: " + airplane.getMaxRange());
                System.out.println("Fuel Burn Rate: " + airplane.getFuelBurnRate());
                System.out.println("Fuel Capacity: " + airplane.getFuelCapacity());
                System.out.println("Airspeed: " + airplane.getAirspeed());
            } else {
                System.out.println("Airplane not found.");
            }
        } else if (searchChoice == 2) {
            // New logic for searching by Plane Type
            System.out.print("Enter Plane Type to search: ");
            String planeType = scanner.nextLine();
            
            // Find all airplanes of the specified type
            List<Airplane> matchingPlanes = new ArrayList<>();
            for (Airplane plane : airplanes) {
                if (plane.getPlaneType().equalsIgnoreCase(planeType)) {
                    matchingPlanes.add(plane);
                }
            }
            
            if (matchingPlanes.isEmpty()) {
                System.out.println("No airplanes found with that plane type.");
                return;
            }
            
            // Display matching airplanes in batches of 10
            int start = 0;
            int batchSize = 10;
            boolean moreAirplanes = true;
            
            while (moreAirplanes) {
                // Display current batch
                for (int i = start; i < Math.min(start + batchSize, matchingPlanes.size()); i++) {
                    Airplane plane = matchingPlanes.get(i);
                    System.out.println((i + 1) + ". " + plane.getMakeModel() + " (" + plane.getPlaneType() + ")");
                }
                
                // If we've displayed all planes, don't offer "more"
                if (start + batchSize >= matchingPlanes.size()) {
                    System.out.print("Enter the number of the airplane to display details: ");
                } else {
                    System.out.print("Enter the number of the airplane to display details or type 'more' to see more: ");
                }
                
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("more")) {
                    if (start + batchSize >= matchingPlanes.size()) {
                        System.out.println("No more airplanes to show.");
                    } else {
                        start += batchSize;
                    }
                } else {
                    try {
                        int index = Integer.parseInt(input) - 1;
                        if (index >= 0 && index < matchingPlanes.size()) {
                            Airplane selected = matchingPlanes.get(index);
                            System.out.println("\nAirplane Make and Model: " + selected.getMakeModel());
                            System.out.println("Airplane Type: " + selected.getPlaneType());
                            System.out.println("Fuel Type: " + selected.getFuelType());
                            System.out.println("Max Range: " + selected.getMaxRange());
                            System.out.println("Fuel Burn Rate: " + selected.getFuelBurnRate());
                            System.out.println("Fuel Capacity: " + selected.getFuelCapacity());
                            System.out.println("Airspeed: " + selected.getAirspeed());
                            moreAirplanes = false;
                        } else {
                            System.out.println("Invalid selection. Please try again.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number or 'more'.");
                    }
                }
            }
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    public Airplane searchAirplane (Object searchParam, Object... additionalParams) {
        if (searchParam instanceof Scanner) {
            Scanner scanner = (Scanner) searchParam;
            System.out.print("Search by (1) Make and Model or (2) Plane Type: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine();

            Airplane airplane = null;
            switch (searchChoice) {
                case 1:
                    System.out.print("Enter Make and Model to search: ");
                    String makeModel = scanner.nextLine();
                    airplane = searchAirplane(makeModel);
                    break;
                case 2:
                    System.out.print("Enter Plane Type to search: ");
                    String planeType = scanner.nextLine();
                    airplane = searchAirplane(planeType);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return null;
            }

            if (airplane != null) {
                System.out.println("\nAirplane Make and Model: " + airplane.getMakeModel());
                System.out.println("Airplane Type: " + airplane.getPlaneType());
                System.out.println("Fuel Type: " + airplane.getFuelType());
                System.out.println("Max Range: " + airplane.getMaxRange());
                System.out.println("Fuel Burn Rate: " + airplane.getFuelBurnRate());
                System.out.println("Fuel Capacity: " + airplane.getFuelCapacity());
                System.out.println("Airspeed: " + airplane.getAirspeed());
            } else {
                System.out.println("Airplane not found.");
            }
            return airplane;
        }
        else if (searchParam instanceof String && additionalParams.length > 0 && additionalParams[0] instanceof Boolean) {
            String identifier = (String) searchParam;
            boolean isMakeModel = (Boolean) additionalParams[0];

            for (Airplane airplane : airplanes) {
                if (isMakeModel) {
                    if (airplane.getMakeModel().equalsIgnoreCase(identifier)) {
                        return airplane;
                    }
                } else {
                    if (airplane.getPlaneType().equalsIgnoreCase(identifier)) {
                        return airplane;
                    }
                }
            }
            return null;
        }
        else if (searchParam instanceof String) {
            String MakeModel = (String) searchParam;
            return searchAirplane(MakeModel, true);
        }
        return null;
    }

    public void removeAirplane(Object scannerOrMakeModel) {
        if (scannerOrMakeModel instanceof Scanner) {
            Scanner scanner = (Scanner) scannerOrMakeModel;
            System.out.print("Enter Make and Model of airplane to remove: ");
            String makeModel = scanner.nextLine();
            Airplane airplane = searchAirplane(makeModel);
            if (airplane != null) {
                removeAirplane(makeModel);
                System.out.println("Airplane removed successfully!");
            } else {
                System.out.println("Airplane not found.");
            }
        } else if (scannerOrMakeModel instanceof String) {
            String makeModel = (String) scannerOrMakeModel;
            airplanes.removeIf(airplane -> airplane.getMakeModel().equalsIgnoreCase(makeModel));
            saveAirplanes();
        }
    }
 

    /** loads airplane data from file
     * @return list of airplanes
     */
    private List<Airplane> loadAirplanes(){
        List<Airplane> loadedAirplanes = new ArrayList<>();
    	try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
    		String line;
            reader.readLine(); // skip header
    		while((line = reader.readLine()) != null) {
    			String[] data = line.split(",");
                loadedAirplanes.add(new Airplane(data[0], data[1], data[2], Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]), Double.parseDouble(data[6])));
    		}
    	} catch(IOException e) {
    		System.out.println("No existing airplane data found.");
    	}
        return loadedAirplanes;
    }
    
    /**
     * saves list of airplanes to file
     */
    private void saveAirplanes() {
    	try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))){
            writer.write("Make and Model, Plane Type, Fuel Type, Max Range, Fuel Burn Rate, Fuel Capacity, Airspeed\n");
            for(Airplane airplane : airplanes) {
    			writer.write(airplane.getMakeModel() + "," + airplane.getPlaneType() + "," + airplane.getFuelType() + "," + airplane.getMaxRange() + ","
    					+ airplane.getFuelBurnRate() + "," + airplane.getFuelCapacity() + "," + airplane.getAirspeed() + "\n");
    		}
    	} catch(IOException e) {
    		System.out.println("Error saving airplane data.");
    	}
    }
}
