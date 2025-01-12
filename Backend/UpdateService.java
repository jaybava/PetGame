package Backend;

import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9              (current version number of program)
 * @since       1.7          (the version of the package this class was first added to)
 */

/**
 * A service class that monitors changes to a specified directory and notifies listeners
 * when the pet info file is modified.
 *
 * <p>This class uses a {@link WatchService} to monitor a directory for changes. When the pet information CSV file is modified,
 * it debounces the updates and notifies all registered listeners with the updated data.</p>
 */
public class UpdateService {

    /**
     * The singleton instance of the UpdateService.
     */
    private static UpdateService instance;

    /**
     * List of listeners that are notified when the pet info file is modified.
     */
    private final List<UpdateListener> listeners = new ArrayList<>();

    /**
     * The WatchService used for monitoring file changes in a specified directory.
     */
    private WatchService watchService;

    /**
     * A Timer instance used to debounce the file update notifications.
     */
    private Timer debounceTimer;

    /**
     * Private constructor that initializes the WatchService to monitor changes to the pet info CSV file.
     */
    private UpdateService() {
        try {
            // Initialize the WatchService
            watchService = FileSystems.getDefault().newWatchService();

            // Specify the directory to monitor (adjust path as needed)
            Path dir = Paths.get("Backend/CSV");
            dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            // Start watching for file updates
            watchForUpdates();
            System.out.println("Monitoring directory: " + dir.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the singleton instance of the UpdateService.
     *
     * @return the singleton instance of the UpdateService
     */
    public static UpdateService getInstance() {
        if (instance == null) {
            instance = new UpdateService();
        }
        return instance;
    }

    /**
     * Adds a listener that will be notified when the pet info file is modified.
     *
     * @param listener the listener to add
     */
    public synchronized void addListener(UpdateListener listener) {
        listeners.add(listener);
        System.out.println("Added listener: " + listener);
    }

    /**
     * Removes all listeners from the UpdateService.
     */
    public synchronized void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Creates a shallow copy of the listener list to avoid concurrent modification issues.
     *
     * @return a copy of the listener list
     */
    private synchronized List<UpdateListener> getListenersCopy(){
        return new ArrayList<>(listeners);
    }

    /**
     * Notifies all registered listeners with the updated pet information from the CSV data store.
     */
    public void notifyListeners() {
        List<UpdateListener> listeners = getListenersCopy();
        Platform.runLater(() -> {
            for (UpdateListener listener : listeners) {
                System.out.println("Notifying listener: " + listener);
                listener.onUpdate(
                        CSVDataStore.getInstance().getToothless(),
                        CSVDataStore.getInstance().getShrek(),
                        CSVDataStore.getInstance().getPuss(),
                        CSVDataStore.getInstance().getParentalInfo()
                );
            }
        });
    }

    /**
     * Starts a separate thread to monitor the directory for updates to the pet info file.
     *
     * <p>This method continuously checks for changes in the specified directory and debounces updates
     * to avoid notifying listeners too frequently.</p>
     */
    private void watchForUpdates() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            Path changedFile = (Path) event.context();
                            if (changedFile.endsWith("petInfo.csv")) {

                                // If a debounce timer exists, cancel it before starting a new one
                                if (debounceTimer != null) {
                                    debounceTimer.cancel();
                                }

                                // Set up a new debounce timer
                                debounceTimer = new Timer();
                                debounceTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        synchronized (CSVDataStore.getInstance()) {
                                            // Read the updated pet information from the CSV file
                                            CSVController.readPetInfo();
                                            // Notify the listeners with the updated data
                                            notifyListeners();
                                        }
                                    }
                                }, 500); // 500 ms debounce time
                            }
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
