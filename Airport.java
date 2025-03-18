/**
 * represents airports with attributes
 */
public class Airport {
    private String airportName;
    private String ICAOID;
    private double latitude;
    private double longitude;
    private String fuelTypes;
    private String radioFrequencies;
    
    /**
     * Constructor for airport class
     * @param airportName: The name of the airport
     * @param ICAOID: The ICAO code of the airport
     * @param latitude: latitude coordinates of the airport
     * @param longitude: longitude coordinates of the airport
     * @param fuelTypes: fuel types available at airport
     * @param radioFrequencies: radio frequency used at airport
     */
    public Airport(String airportName, String ICAOID, double latitude, double longitude, String fuelTypes, String radioFrequencies) {
        this.airportName = airportName;
        this.ICAOID = ICAOID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fuelTypes = fuelTypes;
        this.radioFrequencies = radioFrequencies;
    }

    //Getter methods
    public String getAirportName() { return airportName; }
    public String getICAOID() { return ICAOID; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getFuelTypes() { return fuelTypes; }
    public String getRadioFrequencies() { return radioFrequencies; }
}

