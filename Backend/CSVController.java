package Backend;
//import statements
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.7          (the version of the package this class was first added to)
 */

/**
 * The CSVController class handles reading, writing, and parsing CSV files for various application data.
 * It includes functionality for managing pet information, parental control settings, minigame data,
 * and time-related metrics.
 */

public class CSVController {

    // Path to the petInfo.csv file
    private static String petInfoPath = "Backend/CSV/petInfo.csv";

    /**
     * Static initializer block to set up file watchers for petInfo.csv and parentalInfo.csv.
     * Listens for file changes and triggers reloads and notifications to update listeners.
     */

    static {
        try {
            // Watch petInfo.csv for changes
            Path petInfoFilePath = Paths.get(petInfoPath);
            FileWatcher.getInstance().watchFile(petInfoFilePath.toString(), () -> {
                System.out.println("petInfo.csv has changed. Reloading...");
                readPetInfo();
                UpdateService.getInstance().notifyListeners(); // Notify listeners of changes
            });

            // Watch parentalInfo.csv for changes
            String parentalInfoPath = "Backend/CSV/parentalInfo.csv";
            Path parentalInfoFilePath = Paths.get(parentalInfoPath);
            FileWatcher.getInstance().watchFile(parentalInfoFilePath.toString(), () -> {
                try {
                    Thread.sleep(50); // Add slight delay to handle file write operations
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("parentalInfo.csv has changed. Reloading...");
                readParentalInfo();
                UpdateService.getInstance().notifyListeners(); // Notify listeners of changes
            });

        } catch (Exception e) {
            System.err.println("Error initializing file watchers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads the pet information from the petInfo.csv file and parses its content.
     */
    public static void readPetInfo() {
        // Call parsePetInfo with the path to the petInfo.csv file
        parsePetInfo(petInfoPath);
    }

    /**
     * Parses the specified CSV file to extract pet information and store it in the CSVDataStore.
     * Each row corresponds to a specific pet (e.g., Shrek, Toothless, Puss).
     *
     * @param csvPath The path to the CSV file containing pet information.
     */
    public static void parsePetInfo(String csvPath) {
        System.out.println("Parsing petInfo...");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvPath))) {
            if (br == null) {
                System.out.println("CSV file not found: " + csvPath);
                return;
            }

            String line;
            boolean isHeader = true; // Flag to skip the header row
            int rowIndex = 0;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header row
                    continue;
                }

                // Split the line into fields using a regex that preserves empty fields
                String[] row = line.split(",", -1); // -1 ensures trailing empty fields are not discarded
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim(); // Trim whitespace from each field
                }

                // Map each row to the corresponding pet in the CSVDataStore
                switch (rowIndex) {
                    case 1 -> {
                        if (!Arrays.equals(CSVDataStore.getInstance().getToothless(), row)) {
                            CSVDataStore.getInstance().setToothless(row);
                        }
                    }
                    case 0 -> {
                        if (!Arrays.equals(CSVDataStore.getInstance().getShrek(), row)) {
                            CSVDataStore.getInstance().setShrek(row);
                        }
                    }
                    case 2 -> {
                        if (!Arrays.equals(CSVDataStore.getInstance().getPuss(), row)) {
                            CSVDataStore.getInstance().setPuss(row);
                        }
                    }
                }
                rowIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the pet information for a specific pet in the petInfo.csv file.
     * Synchronizes access to ensure thread safety and notifies listeners of changes.
     *
     * @param pet The pet ID (1 for Shrek, 2 for Toothless, 3 for Puss).
     */
    public static synchronized void writePetInfo(int pet) {
        String petInfoPath = "Backend/CSV/petInfo.csv"; // Path to the CSV file

        synchronized (CSVDataStore.getInstance()) {
            try {
                // Read all lines from the CSV file
                List<String> lines = Files.readAllLines(Paths.get(petInfoPath));

                // Ensure there are enough rows for modification
                while (lines.size() < 4) { // Header + 3 rows for pets
                    lines.add(""); // Add empty rows if missing
                }

                // Determine the correct row index based on pet ID
                int rowIndex = switch (pet) {
                    case 1 -> 1; // Shrek
                    case 2 -> 2; // Toothless
                    case 3 -> 3; // Puss
                    default -> -1; // Invalid pet ID
                };

                if (rowIndex == -1) {
                    System.err.println("Invalid pet ID: " + pet);
                    return;
                }

                // Retrieve updated data for the specified pet
                String[] updatedRow = switch (pet) {
                    case 1 -> CSVDataStore.getInstance().getShrek();
                    case 2 -> CSVDataStore.getInstance().getToothless();
                    case 3 -> CSVDataStore.getInstance().getPuss();
                    default -> null;
                };

                if (updatedRow == null) {
                    System.err.println("Updated row data is null for pet ID: " + pet);
                    return;
                }

                System.out.println("Updated Row: " + Arrays.toString(updatedRow));

                // Update the corresponding row in the CSV
                lines.set(rowIndex, String.join(",", updatedRow));

                // Write all rows back to the CSV file
                Files.write(
                        Paths.get(petInfoPath),
                        lines,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.SYNC
                );

                // Reload the updated data and notify listeners
                if (!FileWatcher.isFileWatcherDisabled()) {
                    readPetInfo();
                }
                UpdateService.getInstance().notifyListeners();

            } catch (IOException e) {
                System.err.println("Error updating pet info for pet ID: " + pet);
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the minigame information from the minigameInfo.csv file and parses its content.
     */
    public static void readMinigameInfo() {
        String minigameInfoPath = "CSV/minigameInfo.csv"; // Path to the CSV file
        parseMinigameInfo(minigameInfoPath);
    }

    /**
     * Parses the minigameInfo.csv file to extract data and organize it by question type and difficulty.
     * Handles quoted fields and maps the data into a structured format.
     *
     * @param csvPath The path to the CSV file containing minigame information.
     */
    public static void parseMinigameInfo(String csvPath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CSVController.class.getResourceAsStream(csvPath)))) {
            if (br == null) {
                System.out.println("CSV file not found: " + csvPath);
                return;
            }

            List<String[]> rows = new ArrayList<>();
            String line;

            // Regex pattern to split lines while handling quotes
            Pattern csvPattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            // Read and parse each line
            while ((line = br.readLine()) != null) {
                // Split line using regex and remove surrounding quotes
                String[] fields = line.split(csvPattern.pattern(), -1);
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                }
                rows.add(fields);
            }

            // Organize data by question type and difficulty
            Map<String, List<String>> minigameData = new HashMap<>();
            String[] headers = rows.get(1); // Question Type row
            String[] difficulties = rows.get(0); // Difficulty row

            for (int colIndex = 1; colIndex < headers.length; colIndex++) {
                String questionType = headers[colIndex];
                String difficulty = difficulties[colIndex];

                List<String> columnData = new ArrayList<>();
                columnData.add(difficulty);

                for (int rowIndex = 2; rowIndex < rows.size(); rowIndex++) {
                    String[] row = rows.get(rowIndex);
                    columnData.add(colIndex < row.length ? row[colIndex] : ""); // Handle missing data
                }

                minigameData.put(questionType, columnData);
            }

            // Store the parsed data
            CSVDataStore.getInstance().setMinigameData(minigameData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the parental information from the parentalInfo.csv file and parses its content.
     */
    public static void readParentalInfo() {
        String parentalInfoPath = "Backend/CSV/parentalInfo.csv"; // Path to the CSV file
        parseParentalInfo(parentalInfoPath);
    }

    /**
     * Parses the parentalInfo.csv file to extract a boolean array and stores it in the CSVDataStore.
     *
     * @param csvPath The path to the parentalInfo.csv file.
     */

    public static void parseParentalInfo(String csvPath) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvPath))) {
            if (br == null) {
                System.out.println("CSV file not found: " + csvPath);
                return;
            }

            // Read the header row
            String[] headers = br.readLine().split(",", -1);

            // Read the boolean values from the second row
            String[] boolRow = br.readLine().split(",", -1);
            boolean[] boolArray = new boolean[boolRow.length];

            // Convert the values to a boolean array
            for (int i = 0; i < boolRow.length; i++) {
                boolArray[i] = boolRow[i].trim().equalsIgnoreCase("TRUE");
            }

            // Store the boolean array in CSVDataStore
            CSVDataStore.getInstance().setParentalInfo(boolArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Writes a boolean array to the parentalInfo.csv file, updating its second row.
     *
     * @param booleanValues The array of boolean values to write to the file.
     */
    public static void writeParentalInfo(boolean[] booleanValues) {
        String parentalInfoPath = "Backend/CSV/parentalInfo.csv";

        try {
            // Read all rows into memory
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(parentalInfoPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Ensure there is a second row to update
            if (lines.size() < 2) {
                throw new IOException("Invalid parentalInfo.csv structure: missing second row for boolean values.");
            }

            // Update the second row with the new boolean values
            if (booleanValues != null && booleanValues.length > 0) {
                String[] boolStrings = new String[booleanValues.length];
                for (int i = 0; i < booleanValues.length; i++) {
                    boolStrings[i] = booleanValues[i] ? "TRUE" : "FALSE";
                }
                lines.set(1, String.join(",", boolStrings)); // Update the second row (index 1)
            }

            // Write all rows back to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(parentalInfoPath))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            CSVDataStore.getInstance().setParentalInfo(booleanValues);

        } catch (IOException e) {
            System.err.println("Error updating boolean values in parentalInfo.csv");
            e.printStackTrace();
        }

    }

    private static String timeInfoPath = "Backend/CSV/timeInfo.csv";

    /**
     * Reads the time information from the timeInfo.csv file and parses its content.
     */
    public static void readTimeInfo() {
        parseTimeInfo(timeInfoPath);
    }

    /**
     * Parses the timeInfo.csv file to extract start and end times, and stores them in the CSVDataStore.
     *
     * @param csvPath The path to the timeInfo.csv file.
     */

    public static void parseTimeInfo(String csvPath) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvPath))) {
            if (br == null) {
                System.out.println("CSV file not found: " + csvPath);
                return;
            }

            // Read the header row
            String[] headers = br.readLine().split(",", -1);
            if (!headers[0].equalsIgnoreCase("StartTime") || !headers[1].equalsIgnoreCase("EndTime")) {
                System.err.println("Invalid timeInfo.csv structure. Headers must be StartTime, EndTime.");
                return;
            }

            // Read the data row
            String[] timeRow = br.readLine().split(",", -1);
            if (timeRow.length != 2) {
                System.err.println("Invalid timeInfo.csv structure. Data row must have exactly two columns.");
                return;
            }

            // Parse the values
            int startTime = Integer.parseInt(timeRow[0].trim());
            int endTime = Integer.parseInt(timeRow[1].trim());

            // Store the values in CSVDataStore
            CSVDataStore.getInstance().setStartTime(startTime);
            CSVDataStore.getInstance().setEndTime(endTime);

            System.out.println("Successfully loaded time information: StartTime = " + startTime + ", EndTime = " + endTime);

        } catch (Exception e) {
            System.err.println("Error parsing timeInfo.csv");
            e.printStackTrace();
        }
    }

    /**
     * Updates the start and end time information in the timeInfo.csv file and stores the values in CSVDataStore.
     *
     * @param startTime The start time to be written to the file.
     * @param endTime   The end time to be written to the file.
     */
    public static void writeTimeInfo(int startTime, int endTime) {
        try {
            // Prepare the updated content
            List<String> lines = new ArrayList<>();
            lines.add("StartTime,EndTime");
            lines.add(startTime + "," + endTime);

            // Write content to the file
            Files.write(
                    Paths.get(timeInfoPath),
                    lines,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.SYNC
            );

            // Update the data store
            CSVDataStore.getInstance().setStartTime(startTime);
            CSVDataStore.getInstance().setEndTime(endTime);

            System.out.println("Successfully updated time information: StartTime = " + startTime + ", EndTime = " + endTime);

        } catch (IOException e) {
            System.err.println("Error updating timeInfo.csv");
            e.printStackTrace();
        }
    }

    private static String timePlayPath = "Backend/CSV/timePlay.csv";
    // Methods for timePlay.csv

    /**
     * Reads the play time and session count from the timePlay.csv file and updates the corresponding values in CSVDataStore.
     * It assumes the file has the structure where the second line contains the total play time and session count values.
     *
     * The method skips the header row, then reads the data from the second row, parses it into a long for total play time
     * and an integer for session count, and updates the CSVDataStore.
     */
    public static void readTimePlay() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(timePlayPath))) {
            br.readLine(); // Skip header row
            String line = br.readLine();
            if (line != null) {
                String[] values = line.split(",", -1);
                long totalPlayTime = Long.parseLong(values[0].trim());
                int sessionCount = Integer.parseInt(values[1].trim());
                CSVDataStore.getInstance().setTotalPlayTime(totalPlayTime);
                CSVDataStore.getInstance().setSessionCount(sessionCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the total play time and session count to the timePlay.csv file and updates the corresponding values in CSVDataStore.
     *
     * @param totalPlayTime The total play time to be written to the file.
     * @param sessionCount  The session count to be written to the file.
     *
     * The method creates or overwrites the file with a header ("TotalPlayTime,SessionCount") followed by the provided
     * values. It then updates the CSVDataStore with the new values for total play time and session count.
     */
    public static void writeTimePlay(long totalPlayTime, int sessionCount) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("TotalPlayTime,SessionCount");
            lines.add(totalPlayTime + "," + sessionCount);
            Files.write(Paths.get(timePlayPath), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            CSVDataStore.getInstance().setTotalPlayTime(totalPlayTime);
            CSVDataStore.getInstance().setSessionCount(sessionCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the total play time and session count by adding the specified additional play time.
     * This method reads the current play time and session count from CSVDataStore, adds the specified additional play time
     * to the current total, and then writes the updated values back to the `timePlay.csv` file.
     *
     * @param additionalPlayTime The additional play time to add to the current total play time.
     */
    public static void updateTimePlay(long additionalPlayTime) {
        readTimePlay();
        long currentPlayTime = CSVDataStore.getInstance().getTotalPlayTime();
        int currentSessionCount = CSVDataStore.getInstance().getSessionCount();
        writeTimePlay(currentPlayTime + additionalPlayTime, currentSessionCount);
    }

    /**
     * Updates the session count by incrementing it by 1 when a new game is played.
     * This method reads the current play time and session count from CSVDataStore, then increments the session count by 1,
     * while keeping the current total play time unchanged. The updated values are written back to the `timePlay.csv` file.
     */
    public static void updateTimePlayWhenNewGame() {
        readTimePlay();
        long currentPlayTime = CSVDataStore.getInstance().getTotalPlayTime();
        int currentSessionCount = CSVDataStore.getInstance().getSessionCount();
        writeTimePlay(currentPlayTime, currentSessionCount + 1);
    }

}
