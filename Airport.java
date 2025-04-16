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
    public String getAirportName() { return airportName; }
    public String getICAOID() { return ICAOID; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getFuelTypes() { return fuelTypes; }
    public String getRadioFrequencies() { return radioFrequencies; }
}

