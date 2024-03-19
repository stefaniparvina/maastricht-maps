package ZipCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private static final String DATABASE = "Data/MassZipLatLon.csv";

    public static double[] readCoordinates(String zipCode) {
        double[] coordinates = new double[2];

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3 && values[0].equals(zipCode)) {

                    coordinates[0] = Double.parseDouble(values[1]); // Latitude
                    coordinates[1] = Double.parseDouble(values[2]); // Longitude


                    return coordinates; // Return coordinates if zip code found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Handle case when zip code is not found
        System.err.println("Zip code not found: " + zipCode);
        return null;
    }
}
