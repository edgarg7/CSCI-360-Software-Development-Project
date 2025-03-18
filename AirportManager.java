import java.io.*;
import java.util.*;

/**
 * Manages the list of airports and handles 
 * loading and saving data from file. 
 */
public class AirportManager {
    private List<Airport> airports;
    private static final String FILE_NAME = "database/airports.txt";

    /**
     * This constructor initializes airport list 
     * by loading data from file
     */
    public AirportManager() {
        this.airports = loadAirports();
    }

    /**
     * 
     * @return the list of airports
     */
    public List<Airport> getAirports() { return airports; }
    
    /**
     * adds airport to list and saves to file
     */
    public void addAirport(Airport airport) {
    	airports.add(airport);
    	saveAirports();
    }
    
    /**
     * Loads airport data from file
     * @return list of airports
     */
    private List<Airport> loadAirports(){
    	List<Airport> airports = new ArrayList<>();
    	try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
    		String line;
    		while((line = reader.readLine()) != null) {
    			String[] data = line.split(",");
    			airports.add(new Airport(data[0], data[1], Double.parseDouble(data[2]), Double.parseDouble(data[3]), data[4], data[5]));
    		}
    	} catch(IOException e) {
    		System.out.println("No existing airport data found.");
    	}
    	return airports;
    }
    
    /**
     * saves list of airports to file
     */
    private void saveAirports() {
    	try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))){
    		for(Airport airport : airports) {
    			writer.write(airport.getAirportName() + "," + airport.getICAOID() + "," + airport.getLatitude() + ","
    					 + airport.getLongitude() + "," + airport.getFuelTypes() + "," + airport.getRadioFrequencies() + "\n");
    		}
    	} catch(IOException e) {
    		System.out.println("Error saving airport data.");
    	}
    }
}

