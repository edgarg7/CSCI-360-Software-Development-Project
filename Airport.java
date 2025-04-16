import java.util.Collection;
import java.util.List;

/**
 * represents airports with attributes
 */
public class Airport {
    private String airportID;
    private String airportName;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Double radioFrequencies;
    private String regionState;
    private String regionAbbr;
    private String city;
    private String ICAO;
    private String iataCode;
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
    public Airport(String airportID, String airportName, Double latitude, Double longitude, Double elevation, Double radioFrequencies, String regionState, String regionAbbr, String city, String ICAO, String iataCode, Double fuelTypes) {
        this.airportID = airportID;
        this.airportName = airportName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.radioFrequencies = radioFrequencies;
        this.regionState = regionState;
        this.regionAbbr = regionAbbr;
        this.city = city;
        this.ICAO = ICAO;
        this.iataCode = iataCode;
        this.fuelTypes = fuelTypes;
    }

    //Getter methods
    public String getAirportName() {return airportName;}
    public String getAirportID() {return airportID;}
    public Double getLatitude() {return latitude;}
    public Double getLongitude() {return longitude;}
    public Double getElevation() {return elevation;}
    public Double getRadioFrequencies() {return radioFrequencies;}
    public String getRegionState() {return regionState;}
    public String getRegionAbbr() {return regionAbbr;}
    public String getCity() {return city;}
    public String getICAO() {return ICAO;}
    public String getIataCode() {return iataCode;}
    public Double getFuelTypes() {return fuelTypes;}

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

