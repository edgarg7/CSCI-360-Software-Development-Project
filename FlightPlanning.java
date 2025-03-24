import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all flight planning by calculating distance,
 * estimated time, fuel needs, and determines if refueling stops are necessary.
 */
public class FlightPlanning {

    // Main method to plan the flight with enhanced refueling check
    public static Flight planFlight(Airport start, Airport destination, Airplanes airplane, List<Airport> allAirports) {
        validateInput(start, destination, airplane, allAirports);

        double distance = calculateHaversineDistance(start, destination);
        double airspeedMph = airplane.getAirspeed() * 0.621371; // Convert km/h to mph
        double estimatedTime = distance / (airspeedMph * 0.85);
        double fuelNeeded = estimatedTime * airplane.getFuelBurnRate() * 0.264172; // Convert liters to gallons
        double heading = calculateHeading(start, destination);
        List<String> refuelStops = new ArrayList<>();

        // Check if a refuel stop is necessary
        if (fuelNeeded > airplane.getFuelCapacity()) {
            List<Airport> refuelingAirports = findRefuelingAirports(start, destination, airplane, allAirports);
            for (Airport refuelAirport : refuelingAirports) {
                refuelStops.add(refuelAirport.getAirportName());
            }
        }

        return new Flight(start.getAirportName(), destination.getAirportName(), estimatedTime, distance, fuelNeeded, heading, destination.getRadioFrequencies(), refuelStops);
    }

    // Validate inputs to ensure no null values
    private static void validateInput(Airport start, Airport destination, Airplanes airplane, List<Airport> allAirports) {
        if (start == null || destination == null || airplane == null || allAirports == null) {
            throw new IllegalArgumentException("Invalid input data: Ensure all values are provided.");
        }
    }

    // Filter and find valid refueling airports
    private static List<Airport> findRefuelingAirports(Airport start, Airport destination, Airplanes airplane, List<Airport> allAirports) {
        List<Airport> validRefuelingAirports = new ArrayList<>();
        for (Airport airport : allAirports) {
            if (!airport.equals(start) && !airport.equals(destination)
                && airport.getAvailableFuelTypes().stream().anyMatch(fuelType -> fuelType.equals(airplane.getFuelType()))) {

                double dist = calculateHaversineDistance(start, airport);
                if (dist <= airplane.getMaxRange()) {
                    validRefuelingAirports.add(airport);
                }
            }
        }
        return validRefuelingAirports;
    }

    // Calculate distance using the Haversine formula
    private static double calculateHaversineDistance(Airport a, Airport b) {
        final int R = 3959; // Radius of Earth in miles
        double lat1 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon2 = Math.toRadians(b.getLongitude());
        double latDistance = lat2 - lat1;
        double lonDistance = lon2 - lon1;

        double aFormula = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                         + Math.cos(lat1) * Math.cos(lat2)
                         * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(aFormula), Math.sqrt(1 - aFormula));
        return R * c;
    }

    // Calculate the heading from one airport to another
    private static double calculateHeading(Airport a, Airport b) {
        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double deltaLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double y = Math.sin(deltaLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }
}
