import java.io.*;
import java.util.*;
/**
 * This class will manage the list of airplanes and handles loading and saving 
 * airplane data from a file.
 */
public class AirplaneManager {
    private List<Airplane> airplanes;
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
    
    /** loads airplane data from file
     * @return list of airplanes
     */
    private List<Airplane> loadAirplanes(){
        List<Airplane> airplanes = new ArrayList<>();
    	try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
    		String line;
    		while((line = reader.readLine()) != null) {
    			String[] data = line.split(",");
                airplanes.add(new Airplane(data[0], data[1], Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5])));
    		}
    	} catch(IOException e) {
    		System.out.println("No existing airplane data found.");
    	}
    	return airplanes;
    }
    
    /**
     * saves list of airplanes to file
     */
    private void saveAirplanes() {
    	try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))){
            for(Airplane airplane : airplanes) {
    			writer.write(airplane.getModel() + "," + airplane.getFuelType() + "," + airplane.getMaxRange() + ","
    					 + airplane.getFuelBurnRate() + "," + airplane.getFuelCapacity() + "," + airplane.getAirspeed() + "\n");
    		}
    	} catch(IOException e) {
    		System.out.println("Error saving airplane data.");
    	}
    }
}
