package ZipCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private static final String DATABASE = "D:\\Project1-2OldCode\\project-1-2\\Data\\MassZipLatLon.csv";

    public static double[] readCoordinates(String zipCode) {
        double[] coordinates = new double[2];
        double[] error = new double[2];
        error[0] = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3 && values[0].equals(zipCode)) {

                    coordinates[0] = Double.parseDouble(values[1]); // Latitude
                    coordinates[1] = Double.parseDouble(values[2]); // Longitude

                    System.out.println("wow");
                    return coordinates; // Return coordinates if zip code found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //if csv file doesn't open it goes to the api
            return coordinates;

        }

        // Handle case when zip code is not found and it goes to the api 
        System.err.println("Zip code not found: " + zipCode);
        System.out.println(Api.apiResponse(zipCode));
        return Api.apiResponse(zipCode);

    }
}
