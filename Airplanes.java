/**
 * represents airplanes with attributes 
 */
public class Airplanes {
    private String model;
    private String fuelType;
    private double maxRange;
    private double fuelBurnRate;
    private double fuelCapacity;
    private double airspeed;
    
    /**
     * constructor for airplane class
     * @param model: make and model of airplane
     * @param fuelType: type of fuel used by airplane
     * @param maxRange: the max range of airplane
     * @param fuelBurnRate: the fuel burn rate of airplane
     * @param fuelCapacity: the fuel capacity of airplane
     * @param airspeed: the speed at which the airplane travels
     */
    public Airplanes(String model, String fuelType, double maxRange, double fuelBurnRate, double fuelCapacity, double airspeed) {
        this.model = model;
        this.fuelType = fuelType;
        this.maxRange = maxRange;
        this.fuelBurnRate = fuelBurnRate;
        this.fuelCapacity = fuelCapacity;
        this.airspeed = airspeed;
    }

    //Getter methods
    public String getModel() { return model; }
    public String getFuelType() { return fuelType; }
    public double getMaxRange() { return maxRange; }
    public double getFuelBurnRate() { return fuelBurnRate; }
    public double getFuelCapacity() { return fuelCapacity; }
    public double getAirspeed() { return airspeed; }
}
