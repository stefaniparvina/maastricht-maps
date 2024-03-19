import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    
    private static String csvFile = "MassZipLatLon.csv";

    public static double[] csvReader(String zipCode){
        double[]coordinates = new double[2];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); //divide the information in each line separated with "," and stores in the dynamic array
                
                // Check if the first element in the line is the desired string
                if (values[0].equals(zipCode)) {
                    coordinates[0] = Double.parseDouble(values[1]);
                    coordinates[1] = Double.parseDouble(values[2]);
                    //System.out.println(coordinates[0]+ " " + coordinates[1]);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return coordinates; // if user enters an invalid zip code - it returns [0.0,0.0]
        } 
        public static void main(String[]ards){
            double[]answer = csvReader("6211ED");
            for(double a:answer){
                System.out.println(a);
            }
        }

    }
