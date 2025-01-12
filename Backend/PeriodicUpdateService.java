package Backend;

import javafx.application.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The `PeriodicUpdateService` class is a singleton responsible for periodically updating the stats
 * of a selected pet in the game. It reduces attributes like hunger, happiness, and sleep over time
 * and applies health penalties if any of these stats reach critical thresholds.
 *
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.7                 (the version of the package this class was first added to)
 */
public class PeriodicUpdateService {

    /** Singleton instance of the `PeriodicUpdateService` class. */
    private static PeriodicUpdateService instance;

    /** Scheduler for executing periodic tasks in a single-threaded environment. */
    private final ScheduledExecutorService scheduler;

    /** ID of the currently selected pet. -1 indicates no pet is selected. */
    private int selectedPetId = -1;

    /** Timestamp of the last update to avoid redundant updates. */
    private long lastUpdateTime = System.currentTimeMillis();

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes the scheduler for periodic tasks.
     */
    private PeriodicUpdateService() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Retrieves the singleton instance of the `PeriodicUpdateService` class.
     *
     * @return the singleton instance of `PeriodicUpdateService`
     */
    public static PeriodicUpdateService getInstance() {
        if (instance == null) {
            instance = new PeriodicUpdateService();
        }
        return instance;
    }

    /**
     * Sets the ID of the currently selected pet for periodic updates.
     *
     * @param petId the ID of the selected pet (1 for Shrek, 2 for Toothless, 3 for Puss)
     */
    public void setSelectedPetId(int petId) {
        this.selectedPetId = petId;
    }

    /**
     * Starts the periodic updates for the selected pet.
     * Updates are performed every 5 seconds.
     */
    public void startUpdating() {
        scheduler.scheduleAtFixedRate(this::updateSelectedPet, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Stops the periodic updates by shutting down the scheduler.
     */
    public void stopUpdating() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    /**
     * Updates the stats of the selected pet, reducing hunger, happiness, and sleep.
     * If any of these stats reach zero, health is reduced as a penalty.
     * Changes are written to the CSV and listeners are notified.
     */
    private void updateSelectedPet() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < 5100) { // Debounce to ensure updates are spaced out
            return;
        }
        lastUpdateTime = currentTime;

        if (selectedPetId == -1) {
            return; // No pet is selected
        }

        String[] selectedPet = null;

        // Fetch the selected pet's data
        synchronized (CSVDataStore.getInstance()) {
            switch (selectedPetId) {
                case 1 -> selectedPet = CSVDataStore.getInstance().getShrek();
                case 2 -> selectedPet = CSVDataStore.getInstance().getToothless();
                case 3 -> selectedPet = CSVDataStore.getInstance().getPuss();
            }
        }

        if (selectedPet != null) {
            // Decrease stats and apply penalties if necessary
            int hunger = Math.max(Integer.parseInt(selectedPet[3]) - 2, 0);
            int happiness = Math.max(Integer.parseInt(selectedPet[4]) - 2, 0);
            int sleep = Math.max(Integer.parseInt(selectedPet[6]) - 2, 0);

            // Apply health penalty if any stat reaches zero
            int health = Integer.parseInt(selectedPet[8]);
            if (hunger == 0 || happiness == 0 || sleep == 0) {
                health = Math.max(health - 2, 0);
            }

            // Update the pet's data
            selectedPet[3] = String.valueOf(hunger);
            selectedPet[4] = String.valueOf(happiness);
            selectedPet[6] = String.valueOf(sleep);
            selectedPet[8] = String.valueOf(health);

            // Write updated data to the CSV
            CSVController.writePetInfo(selectedPetId);

            // Notify listeners about the update
            Platform.runLater(UpdateService.getInstance()::notifyListeners);
        }
    }
}
