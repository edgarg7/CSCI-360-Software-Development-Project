import java.util.List;

/**
 * this class represents flights between 2 airports
 * shows estimated time, fuel needs, and refueling stops if needed.
 */
public class Flight {
    private String startingAirport;
    private String destinationAirport;
    private double estimatedTime;
    private double distance;
    private double fuelNeeded;
    private double heading;
    private String destinationCOM;
    private List<String> refuelStops;

    /**
     * Construtor for this class
     * @param startingAirport: the starting airport.
     * @param destinationAirport: the destination airport.
     * @param estimatedTime: Estimated time it will take to travel
     * @param distance: distance between chosen airports.
     * @param fuelNeeded: the amount of fuel needed to make the trip.
     * @param heading: the direction in which the plane will fly
     * @param destinationCOM: the communication frequency used at destination airport
     * @param refuelStops: will show airport name if a refuel stop is needed at another airport.
     */
    public Flight(String startingAirport, String destinationAirport, double estimatedTime, double distance, double fuelNeeded, double heading, String destinationCOM, List<String> refuelStops) {
        this.startingAirport = startingAirport;
        this.destinationAirport = destinationAirport;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
        this.fuelNeeded = fuelNeeded;
        this.heading = heading;
        this.destinationCOM = destinationCOM;
        this.refuelStops = refuelStops;
    }

    /**
     * this will display all flight details and 
     * include refueling stops if necessary.
     */
    public void displayFlightPlan() {
    	System.out.println("\nFlight Plan:");
    	System.out.println("Start: " + startingAirport);
    	System.out.println("Destination: " + destinationAirport);
    	System.out.printf("Estimated Time: %.2f hours\n", estimatedTime);
    	System.out.printf("Estimated Distance: %.2f miles\n", distance);
    	System.out.printf("Fuel Needed: %.2f gallons\n", fuelNeeded);
    	System.out.printf("Heading: %.2f degrees\n", heading);
    	System.out.println("Detination Communication Frequency: " + destinationCOM);
    	if(!refuelStops.isEmpty()) {
    		System.out.println("Refuel Stops:");
    		for(String stop : refuelStops) {
    			System.out.println(" - " + stop);
    		}
    	}
    }
    
    //getter and setter methods
	public String getStartingAirport() {
		return startingAirport;
	}
	
	public void setStartingAirport(String startingAirport) {
		this.startingAirport = startingAirport;
	}

	public String getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public double getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getFuelNeeded() {
		return fuelNeeded;
	}

	public void setFuelNeeded(double fuelNeeded) {
		this.fuelNeeded = fuelNeeded;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public String getDestinationCOM() {
		return destinationCOM;
	}

	public void setDestinationCOM(String destinationCOM) {
		this.destinationCOM = destinationCOM;
	}
    
    
}

