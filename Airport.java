import java.util.Collection;
import java.util.List;

/**
 * represents airports with attributes
 */
public class Airport {
    private String airportName;
    private Double latitude;
    private Double longitude;
    private Double radioFrequencies;
    private String radioType;
    private String ICAO;
    private Double fuelTypes;
    
    /**
     * Constructor for airport class
     * @param radioType: The type of radio used at the airport
     * @param airportName: The name of the airport
     * @param ICAOID: The ICAO code of the airport
     * @param latitude: latitude coordinates of the airport
     * @param longitude: longitude coordinates of the airport
     * @param fuelTypes: fuel types available at airport
     * @param radioFrequencies: radio frequency used at airport
     */
    public Airport(String airportID, String airportName, Double latitude, Double longitude, Double elevation, Double radioFrequencies, String regionState, String regionAbbr, String city, String ICAO, String iataCode, Double fuelTypes, String radioType) {
        this.airportName = airportName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radioFrequencies = radioFrequencies;
        this.radioType = radioType;
        this.ICAO = ICAO;
        this.fuelTypes = fuelTypes;
    }

    //Getter methods
    public String getAirportName() { return airportName; }
    public String getICAOID() { return getICAOID(); }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Double getFuelTypes() { return fuelTypes; }
    public Double getRadioFrequencies() { return radioFrequencies; }

    /**
     * Retrieves a collection of available fuel types at the airport.
     * @return A collection of available fuel types.
     */
    public Collection<String> getAvailableFuelTypes() {
        // Example implementation, assuming fuelTypes is a bitmask or similar representation
        // Replace with actual logic as per your requirements
        return List.of("Jet A", "100LL", "Avgas");
    }
}

