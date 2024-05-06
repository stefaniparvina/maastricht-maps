package bus.maapstrichtfinal.Calculations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BusFinder {
    static final double R = 6371.01; 

    public static void main(String[] args) {
        //example coordinates
        double lat1 = 50.850581;
        double lon1 = 5.704040;
        double lat2 = 50.854377;
        double lon2 = 5.690444;

        int busStop1 = closestBusStop(lat1, lon1);
        int busStop2 = closestBusStop(lat2, lon2);
        System.out.println(busStop1);
        System.out.println(busStop2);
        
    }

    static int closestBusStop(double lat, double lon) {
        try (BufferedReader reader = new BufferedReader(new FileReader("stops.txt"))) {
            reader.readLine(); 
            int busStopID = 0;
            double minDistance = Double.MAX_VALUE;
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCSVLine(line);
                if (fields.size() < 5) continue;

                try {
                    double stopLat = Double.parseDouble(fields.get(3).trim());
                    double stopLon = Double.parseDouble(fields.get(4).trim());
                    double distance = haversine(lat, lon, stopLat, stopLon);
                    if (distance < minDistance) {
                        minDistance = distance;
                        busStopID = Integer.parseInt(fields.get(0).trim());
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            return busStopID;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes; 
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString()); 
        return fields;
    }

    static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                 + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
