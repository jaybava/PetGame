/**
 * The `FileWatcher` class provides a mechanism to monitor changes to specific files
 * and execute a callback when changes are detected. It uses a single-threaded executor
 * to handle file-watching tasks and supports enabling or disabling the watcher at runtime.
 *
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.7                 (the version of the package this class was first added to)
 */
package Backend;

import java.io.File;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileWatcher {

    /** Singleton instance of the `FileWatcher` class. */
    private static FileWatcher instance;

    /** Executor for running file-watching tasks in a separate thread. */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /** Flag indicating whether the file watcher is disabled. */
    private static volatile boolean isFileWatcherDisabled = true;

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private FileWatcher() {}

    /**
     * Retrieves the singleton instance of the `FileWatcher` class.
     *
     * @return the singleton instance of `FileWatcher`
     */
    public static FileWatcher getInstance() {
        if (instance == null) {
            instance = new FileWatcher();
        }
        return instance;
    }

    /**
     * Sets the state of the file watcher (enabled or disabled).
     *
     * @param disabled true to disable the file watcher, false to enable it
     */
    public static void setIsFileWatcherDisabled(boolean disabled) {
        isFileWatcherDisabled = disabled;
    }

    /**
     * Retrieves the current state of the file watcher.
     *
     * @return true if the file watcher is disabled, false otherwise
     */
    public static boolean isFileWatcherDisabled() {
        return isFileWatcherDisabled;
    }

    /**
     * Watches a specific file for modifications and triggers the provided callback when changes are detected.
     *
     * @param filePath     the path of the file to watch
     * @param onFileChange the callback to execute when the file changes
     */
    public void watchFile(String filePath, Runnable onFileChange) {
        executor.submit(() -> {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("File " + filePath + " does not exist");
                }

                Path path = Paths.get(filePath).getParent();
                WatchService watchService = FileSystems.getDefault().newWatchService();
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take(); // Wait for file changes
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changedFile = (Path) event.context();
                        if (changedFile.endsWith(filePath)) {
                            if (isFileWatcherDisabled) {
                                System.err.println("FileWatcher Disabled");
                                continue;
                            }
                            Thread.sleep(100);
                            onFileChange.run(); // Trigger the callback
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Stops the file-watching process and shuts down the executor service.
     */
    public void stopWatching() {
        executor.shutdownNow();
    }
}
