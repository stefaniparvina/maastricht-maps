package bus.maapstrichtfinal.ZipCode;

import java.util.Arrays;

public class ZipCode extends Reader {
    private String zipCode;
    private final double latitude;
    private final double longitude;
    private double[] coordinates;

    // Constructor to initialize a ZipCode object with a given zip code
    public ZipCode(String zipCode) {
        this.zipCode = zipCode;
        this.coordinates = readCoordinates(zipCode);

        // Ensure that coordinates are retrieved successfully and are not the default invalid coordinates
        assert (this.coordinates != null) && (!Arrays.equals(this.coordinates, new double[]{-1, -1}));
        this.latitude = this.coordinates[0];
        this.longitude = this.coordinates[1];
    }

    // Getter method for longitude
    public double getLongitude() {
        return longitude;
    }

    // Getter method for latitude
    public double getLatitude() {
        return latitude;
    }
}
