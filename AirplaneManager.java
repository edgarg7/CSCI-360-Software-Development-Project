import java.io.*;
import java.util.*;
/**
 * This class will manage the list of airplanes and handles loading and saving 
 * airplane data from a file.
 */
public class AirplaneManager {
    private List<Airplanes> airplanes;
    private static final String FILE_NAME = "database/airplanes.txt";
    
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
    public List<Airplanes> getAirplanes() { return airplanes; }
    
    /**
     * 
     * @param airplane will add an airplane to the list and save to file
     */
    public void addAirplane(Airplanes airplane) {
    	airplanes.add(airplane);
    	saveAirplanes();
    }
    
    /** loads airplane data from file
     * @return list of airplanes
     */
    private List<Airplanes> loadAirplanes(){
    	List<Airplanes> airplanes = new ArrayList<>();
    	try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
    		String line;
    		while((line = reader.readLine()) != null) {
    			String[] data = line.split(",");
    			airplanes.add(new Airplanes(data[0], data[1], Double.parseDouble(data[2]),
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
    		for(Airplanes airplane : airplanes) {
    			writer.write(airplane.getModel() + "," + airplane.getFuelType() + "," + airplane.getMaxRange() + ","
    					 + airplane.getFuelBurnRate() + "," + airplane.getFuelCapacity() + "," + airplane.getAirspeed() + "\n");
    		}
    	} catch(IOException e) {
    		System.out.println("Error saving airplane data.");
    	}
    }
}
