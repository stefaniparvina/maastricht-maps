package bus.maapstrichtfinal.Accessibility;
import bus.maapstrichtfinal.Accessibility.*;
import bus.maapstrichtfinal.Calculations.Distance;
import bus.maapstrichtfinal.ZipCode.ZipCode;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessibilityCalculator {

    public static void processFileData(String inputFile, String outputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {

            writer.write("PostalCode,Accessibility\n"); // Write header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String postalCode = parts[0].trim();
                    String latitudeStr = parts[1].trim();
                    String longitudeStr = parts[2].trim();

                    try{
                        double latitude = Double.parseDouble(latitudeStr);
                        double longitude = Double.parseDouble(longitudeStr);
                        double distance = Distance.calculateDistance(postalCode,latitude,longitude);
                        double E = Math.E;
                        double beta = 0.1;
                        double accessibility = Math.pow(E, (beta * distance));

                        writer.write(postalCode + "," + accessibility + "\n");
                    }
                    catch (NumberFormatException e) {
                        // If the value in the column is not a valid number, you may want to handle or skip it
                        System.err.println("Skipping non-numeric value.");
                    }

                }
            }
        }
    }

    // Method to aggregate accessibility values from multiple files
    public static void aggregateAccessibility(String[] inputFiles, List<String> bonusFiles, String outputFile) throws IOException {
        Map<String, Double> accessibilityMap = new HashMap<>();

        // Aggregate accessibility values from main input files
        for (String inputFile : inputFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String postalCode = parts[0].trim();
                        try {
                            double accessibility = Double.parseDouble(parts[1].trim());

                            // Aggregate accessibility values for each postal code
                            if (accessibilityMap.containsKey(postalCode)) {
                                double currentAccessibility = accessibilityMap.get(postalCode);
                                accessibilityMap.put(postalCode, currentAccessibility + accessibility);
                            } else {
                                accessibilityMap.put(postalCode, accessibility);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping non-numeric accessibility value.");
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + inputFile);
            }
        }

        // Add bonus points from each bonus file
        for (String bonusFile : bonusFiles) {
            addBonusPoints(accessibilityMap, bonusFile);
        }

        // Write aggregated accessibility values to the output file
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("PostalCode,Accessibility\n"); // Write header
            for (Map.Entry<String, Double> entry : accessibilityMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + outputFile);
        }
    }

    // Helper method to add bonus points to accessibility values
    public static void addBonusPoints(Map<String, Double> accessibilityMap, String bonusFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(bonusFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String postalCode = parts[0].trim();
                    try {
                        double bonusPoints = Double.parseDouble(parts[1].trim());
                        if (accessibilityMap.containsKey(postalCode)) {
                            double currentAccessibility = accessibilityMap.get(postalCode);
                            accessibilityMap.put(postalCode, currentAccessibility + (bonusPoints / 10.0));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping non-numeric bonus points value.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + bonusFile);
        }
    }


    public static void main(String[] args) {
        String tourismInputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/tourism_data.csv";
        String tourismOutputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/tourism_cl_data.csv";

        String shopsInputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/shops_data.csv";
        String shopsOutputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/shops_cl_data.csv";

        String schoolsInputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/schools_data.csv";
        String schoolsOutputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/schools_cl_data.csv";

        String healthcareInputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/healthcare_data.csv";
        String healthcareOutputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/healthcare_cl_data.csv";

        String[] aggregateInputFiles = {
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/calculated_data/healthcare_cl_data.csv",
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/calculated_data/schools_cl_data.csv",
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/calculated_data/shops_cl_data.csv",
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/calculated_data/tourism_cl_data.csv"
        };

        List<String> bonusFiles = List.of(
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/wheelchair_data.csv",
                "src/main/java/bus/maapstrichtfinal/Accessibility/data/busstops_data.csv"
        );
        String aggregateOutputFile = "src/main/java/bus/maapstrichtfinal/Accessibility/data/calculated_data/accessibility2_data.csv";

        try {
            processFileData(tourismInputFile, tourismOutputFile);
            processFileData(shopsInputFile, shopsOutputFile);
            processFileData(schoolsInputFile, schoolsOutputFile);
            processFileData(healthcareInputFile, healthcareOutputFile);
            System.out.println("Accessibility calculation completed.");

            aggregateAccessibility(aggregateInputFiles,bonusFiles,aggregateOutputFile);
            System.out.println("Accessibility aggregation completed.");
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        }
    }
}