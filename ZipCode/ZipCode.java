package ZipCode;

public class ZipCode extends Reader {
    private String zipCode;
    private double latitude;
    private double longitude;
    private double[] coordinates;


    public ZipCode(String zipCode) {

        this.zipCode = zipCode;
        this.coordinates = readCoordinates(zipCode);
        assert this.coordinates != null;
        this.latitude = this.coordinates[0];
        this.longitude = this.coordinates[1];
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


}
