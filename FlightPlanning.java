import java.util.ArrayList;
import java.util.List;
/**
 * this class handles all flight planning by calculating distance,
 * estimated time, fuel needs and determines if refueling stops
 * are necessary.
 */
public class FlightPlanning {
	
	/**
	 * This will plan a flight between 2 airports
	 * @param start: the airport where flight will start
	 * @param destination: the airport where flight will end.
	 * @param airplane: type of airplane used
	 * @param allAirports: list of all airports
	 * @return a flight object that contains all flight details
	 */
	public static Flight planFlight(Airport start, Airport destination, Airplanes airplane, List<Airport> allAirports) {
        double distance = haversineDistance(start.getLatitude(), start.getLongitude(), destination.getLatitude(), destination.getLongitude());
        double airspeedMph = airplane.getAirspeed() * 0.621371; // Convert km/h to mph
        double estimatedTime = distance / (airspeedMph * 0.85); // Factor in realistic airspeed reduction
        double fuelNeeded = estimatedTime * airplane.getFuelBurnRate() * 0.264172; // Convert liters to gallons
        double heading = calculateHeading(start, destination);
        List<String> refuelStops = new ArrayList<>();
        
        //if distance is greater than airplane's max range it will find a refueling stop
        if(distance > airplane.getMaxRange()) {
        	Airport refuelAirport = findNearestAirport(start, allAirports);
        	if(refuelAirport != null) {
        		refuelStops.add(refuelAirport.getAirportName());
        	}
        }
        
        return new Flight(start.getAirportName(), destination.getAirportName(), estimatedTime, distance, fuelNeeded, heading, destination.getRadioFrequencies(), refuelStops);
    }
	
	/**
	 * this will find the nearest airport to the given location(airport) 
	 * for potential refuel stop
	 * @param current: the current airport
	 * @param allAirports: list of all available airports
	 * @return the nearest airport
	 */
	private static Airport findNearestAirport(Airport current, List<Airport> allAirports) {
		Airport nearest = null;
		double minDistance = Double.MAX_VALUE;
		for(Airport airport : allAirports) {
			if(!airport.equals(current)) {
				double dist = haversineDistance(current.getLatitude(), current.getLongitude(), airport.getLatitude(), airport.getLongitude());
				if(dist < minDistance) {
					minDistance = dist;
					nearest = airport;
				}
			}
		}
		return nearest;
	}

	/**
	 * This will calculate distance using haversine formula 
	 * between 2 points on earth.
	 * @param lat1: latitude coordinate of first point.
	 * @param lon1: longitude coordinate of first point
	 * @param lat2: latitude coordinate of second point.
	 * @param lon2: longitude coordinate of second point.
	 * @return the distance between both points in miles
	 */
    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 3959; // Radius of Earth in miles
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    /**
     * This will calculate the heading from one airport to another.
     * @param a: starting airport
     * @param b: destination airport
     * @return will return heading in degrees
     */
    private static double calculateHeading(Airport a, Airport b) {
        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double deltaLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double y = Math.sin(deltaLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                   Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }
}

