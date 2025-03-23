import java.io.*;
import java.util.*;

public class AirportManager {
    private List<Airport> airports;
    private static final String FILE_NAME = "airports.txt";

    public AirportManager() {
        this.airports = loadAirports();
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void addAirport(Airport airport) {
        airports.add(airport);
        saveAirports();
    }

    public void removeAirport(String ICAOID) {
        airports.removeIf(airport -> airport.getICAOID().equals(ICAOID));
        saveAirports();
    }

    public void modifyAirport(String ICAOID, Airport updatedAirport) {
        for (int i = 0; i < airports.size(); i++) {
            if (airports.get(i).getICAOID().equals(ICAOID)) {
                airports.set(i, updatedAirport);
                break;
            }
        }
        saveAirports();
    }

    public Airport searchAirport(String ICAOID) {
        for (Airport airport : airports) {
            if (airport.getICAOID().equals(ICAOID)) {
                return airport;
            }
        }
        return null;
    }

    public void displayAirport(String ICAOID) {
        Airport airport = searchAirport(ICAOID);
        if (airport != null) {
            System.out.println("Airport Name: " + airport.getAirportName());
            System.out.println("ICAO ID: " + airport.getICAOID());
            System.out.println("Latitude: " + airport.getLatitude());
            System.out.println("Longitude: " + airport.getLongitude());
            System.out.println("Fuel Types: " + airport.getFuelTypes());
            System.out.println("Radio Frequencies: " + airport.getRadioFrequencies());
        } else {
            System.out.println("Airport not found.");
        }
    }

    public boolean validateAirport(String ICAOID) {
        return searchAirport(ICAOID) != null;
    }

    public void setAirport(String ICAOID, Airport updatedAirport) {
        modifyAirport(ICAOID, updatedAirport);
    }

    public void setFrequencies(String ICAOID, String radioFrequencies) {
        Airport airport = searchAirport(ICAOID);
        if (airport != null) {
            airport.setRadioFrequencies(radioFrequencies);
            saveAirports();
        }
    }

    public String getFrequencies(String ICAOID) {
        Airport airport = searchAirport(ICAOID);
        return airport != null ? airport.getRadioFrequencies() : null;
    }

    private List<Airport> loadAirports() {
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                airports.add(new Airport(data[0], data[1], Double.parseDouble(data[2]), Double.parseDouble(data[3]), data[4], data[5]));
            }
        } catch (IOException e) {
            System.out.println("No existing airport data found.");
        }
        return airports;
    }

    private void saveAirports() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Airport Name, ICAO, Latitude, Longitude, Fuel Type, Frequency\n"); // Write header line
            for (Airport airport : airports) {
                writer.write(airport.getAirportName() + "," + airport.getICAOID() + "," + airport.getLatitude() + ","
                        + airport.getLongitude() + "," + airport.getFuelTypes() + "," + airport.getRadioFrequencies() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving airport data.");
        }
    }
}