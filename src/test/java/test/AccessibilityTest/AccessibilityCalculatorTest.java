package test.AccessibilityTest;

import bus.maapstrichtfinal.Accessibility.AccessibilityCalculator;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AccessibilityCalculatorTest {

    private static final String TEST_INPUT_FILE1 = "src/test/java/test/AccessibilityTest/test_input1.csv";
    private static final String TEST_INPUT_FILE2 = "src/test/java/test/AccessibilityTest/test_input2.csv";
    private static final String TEST_INPUT_FILE3 = "src/test/java/test/AccessibilityTest/test_input3.csv.csv";
    private static final String TEST_INPUT_FILE4 = "src/test/java/test/AccessibilityTest/test_input4.csv.csv";
    private static final String TEST_OUTPUT_FILE1 = "src/test/java/test/AccessibilityTest/test_output1.csv";
    private static final String TEST_OUTPUT_FILE2 = "src/test/java/test/AccessibilityTest/test_output2.csv";

    private static final String TEST_OUTPUT_FILE3 = "src/test/java/test/AccessibilityTest/test_output3.csv";

    private static final String TEST_OUTPUT_FILE4 = "src/test/java/test/AccessibilityTest/test_output4.csv";


    @Before
    public void setup() throws IOException {
        // healthcare
        try (FileWriter writer = new FileWriter(TEST_INPUT_FILE1)) {
            writer.write("PostalCode,Latitude,Longitude\n");
            writer.write("6224JG,50.8489957,5.7190804\n");
            writer.write("6224LW,50.8468926,5.7076974\n");
        }

        //schools
        try (FileWriter writer = new FileWriter(TEST_INPUT_FILE2)) {
            writer.write("PostalCode,Latitude,Longitude\n");
            writer.write("6217GR,50.854144,5.6721329\n");
            writer.write("6223BE,50.8763104,5.6882335\n");
        }

        //shops
        try (FileWriter writer = new FileWriter(TEST_INPUT_FILE3)) {
            writer.write("PostalCode,Latitude,Longitude\n");
            writer.write("6211GR,50.8490023,5.6920294\n");
            writer.write("6211GN,50.8486873,5.6925064\n");
        }
        //tourism
        try (FileWriter writer = new FileWriter(TEST_INPUT_FILE4)) {
            writer.write("PostalCode,Latitude,Longitude\n");
            writer.write("6227HB,50.8336988,5.7271565\n");
            writer.write("6211HD,50.8478604,5.6932084\n");
        }
    }

    @Test
    public void testProcessFileData() throws IOException {
        // verify output
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_OUTPUT_FILE1))) {
            AccessibilityCalculator.processFileData(TEST_INPUT_FILE1, TEST_OUTPUT_FILE1);
            String header = br.readLine();
            assertEquals("PostalCode,Accessibility", header);

            String line1 = br.readLine();
            assertEquals("6224JG,1.0", line1);

            String line2 = br.readLine();
            assertEquals("6224LW,1.003004504503377", line2);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(TEST_OUTPUT_FILE2))) {
            AccessibilityCalculator.processFileData(TEST_INPUT_FILE2, TEST_OUTPUT_FILE2);
            String header = br.readLine();
            assertEquals("PostalCode,Accessibility", header);

            String line1 = br.readLine();
            assertEquals("6217GR,1.005012520859401", line1);

            String line2 = br.readLine();
            assertEquals("6223BE,1.003004504503377", line2);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(TEST_OUTPUT_FILE3))) {
            AccessibilityCalculator.processFileData(TEST_INPUT_FILE3, TEST_OUTPUT_FILE3);
            String header = br.readLine();
            assertEquals("PostalCode,Accessibility", header);

            String line1 = br.readLine();
            assertEquals("6211GR,1.0010005001667084", line1);

            String line2 = br.readLine();
            assertEquals("6211GN,1.0020020013340003", line2);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(TEST_OUTPUT_FILE4))) {
            AccessibilityCalculator.processFileData(TEST_INPUT_FILE4, TEST_OUTPUT_FILE4);
            String header = br.readLine();
            assertEquals("PostalCode,Accessibility", header);

            String line1 = br.readLine();
            assertEquals("6227HB,1.006018036054065", line1);

            String line2 = br.readLine();
            assertEquals("6211HD,1.0020020013340003", line2);
        }
    }

    @Test
    public void testAddBonusPoints() throws IOException {
        // test data
        Map<String, Double> accessibilityMap = new HashMap<>();
        accessibilityMap.put("6224JG", 2.0-1.0);
        accessibilityMap.put("6224LW", 3.307012515180719-1.3);

        String bonusFile = "src/test/java/test/AccessibilityTest/test_bonus.csv";
        try (FileWriter writer = new FileWriter(bonusFile)) {
            writer.write("PostalCode,BonusPoints\n");
            writer.write("6224JG,10.0\n");
            writer.write("6224LW,13.0\n");
        }

        AccessibilityCalculator.addBonusPoints(accessibilityMap, bonusFile);

        // verify output
        assertEquals(2.0, accessibilityMap.get("6224JG"), 0.001);
        assertEquals(3.307012515180719, accessibilityMap.get("6224LW"), 0.001);
    }
}
