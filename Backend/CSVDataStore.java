package Backend;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The `CSVDataStore` class serves as a singleton that acts as an in-memory data store
 * for all data read from and written to CSV files. It provides thread-safe methods to
 * access and update data for pets, minigames, parental controls, and time-related statistics.
 *
 * <p>This class maintains:
 * <ul>
 *   <li>Pet information for Toothless, Shrek, and Puss</li>
 *   <li>Minigame data organized as a map of question types</li>
 *   <li>Parental control information</li>
 *   <li>Time-related data including start and end times, total playtime, and session count</li>
 * </ul>
 *
 * <p>The singleton pattern ensures there is only one instance of `CSVDataStore`
 * throughout the application.
 *
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.7                 (the version of the package this class was first added to)
 */
public class CSVDataStore {

    /** The singleton instance of the `CSVDataStore` class. */
    private static CSVDataStore instance;

    /** Array to store Toothless's pet data. */
    private String[] toothless;

    /** Array to store Shrek's pet data. */
    private String[] shrek;

    /** Array to store Puss's pet data. */
    private String[] puss;

    /** Map to store minigame data, categorized by question types. */
    private Map<String, List<String>> minigameData;

    /** Array to store parental control boolean flags. */
    private boolean[] parentalInfo;

    /** The start time for restricted play. */
    private int startTime;

    /** The end time for restricted play. */
    private int endTime;

    /** The total playtime in seconds. */
    private long totalPlayTime;

    /** The total number of sessions played. */
    private int sessionCount;

    /**
     * Private constructor to enforce the singleton pattern.
     * Prevents external instantiation.
     */
    private CSVDataStore() {}

    /**
     * Retrieves the singleton instance of the `CSVDataStore` class.
     *
     * @return the singleton instance of `CSVDataStore`
     */
    public static CSVDataStore getInstance() {
        if (instance == null) {
            instance = new CSVDataStore();
        }
        return instance;
    }

    // ===== PET INFO METHODS =====

    /**
     * Retrieves the data for Toothless.
     *
     * @return an array of strings representing Toothless's data
     */
    public synchronized String[] getToothless() {
        return toothless;
    }

    /**
     * Updates the data for Toothless.
     *
     * @param toothless an array of strings representing the new data for Toothless
     */
    public synchronized void setToothless(String[] toothless) {
        this.toothless = toothless;
    }

    /**
     * Retrieves the data for Shrek.
     *
     * @return an array of strings representing Shrek's data
     */
    public synchronized String[] getShrek() {
        return shrek;
    }

    /**
     * Updates the data for Shrek.
     *
     * @param shrek an array of strings representing the new data for Shrek
     */
    public synchronized void setShrek(String[] shrek) {
        this.shrek = shrek;
    }

    /**
     * Retrieves the data for Puss.
     *
     * @return an array of strings representing Puss's data
     */
    public synchronized String[] getPuss() {
        return puss;
    }

    /**
     * Updates the data for Puss.
     *
     * @param puss an array of strings representing the new data for Puss
     */
    public synchronized void setPuss(String[] puss) {
        this.puss = puss;
    }

    // ===== MINIGAME DATA METHODS =====

    /**
     * Retrieves the minigame data.
     *
     * @return a map where the key is the question type and the value is a list of related data
     */
    public Map<String, List<String>> getMinigameData() {
        return minigameData;
    }

    /**
     * Updates the minigame data.
     *
     * @param minigameData a map where the key is the question type and the value is a list of related data
     */
    public void setMinigameData(Map<String, List<String>> minigameData) {
        this.minigameData = minigameData;
    }

    // ===== PARENTAL INFO METHODS =====

    /**
     * Retrieves the parental control information.
     *
     * @return an array of booleans representing parental control flags
     */
    public synchronized boolean[] getParentalInfo() {
        return parentalInfo;
    }

    /**
     * Updates the parental control information.
     *
     * @param parentalInfo an array of booleans representing new parental control flags
     */
    public synchronized void setParentalInfo(boolean[] parentalInfo) {
        this.parentalInfo = parentalInfo;
    }

    // ===== TIME DATA METHODS =====

    /**
     * Retrieves the start time for restricted play.
     *
     * @return the start time as an integer
     */
    public synchronized int getStartTime() {
        return startTime;
    }

    /**
     * Updates the start time for restricted play.
     *
     * @param startTime the new start time as an integer
     */
    public synchronized void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * Retrieves the end time for restricted play.
     *
     * @return the end time as an integer
     */
    public synchronized int getEndTime() {
        return endTime;
    }

    /**
     * Updates the end time for restricted play.
     *
     * @param endTime the new end time as an integer
     */
    public synchronized void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**
     * Retrieves the total playtime.
     *
     * @return the total playtime in seconds
     */
    public synchronized long getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * Updates the total playtime.
     *
     * @param totalPlayTime the new total playtime in seconds
     */
    public synchronized void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    /**
     * Retrieves the session count.
     *
     * @return the total number of sessions played
     */
    public synchronized int getSessionCount() {
        return sessionCount;
    }

    /**
     * Updates the session count.
     *
     * @param sessionCount the new session count
     */
    public synchronized void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
}
