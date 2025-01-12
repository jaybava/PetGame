package Backend;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * The `ResourceManager` class provides a mechanism for caching and retrieving images
 * used in the application. This ensures efficient memory usage by avoiding redundant
 * image loading operations.
 *
 * @author      Sebastien Moroz <smoroz4@uwo.ca>
 * @version     1.9
 * @since       1.7
 */
public class ResourceManager {

    /** A static map that caches loaded images, keyed by their file paths. */
    static final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Retrieves an image from the cache or loads it if not already cached.
     *
     * <p>If the image specified by the path is not already in the cache, this method
     * loads the image and stores it in the cache for future use. If the image is
     * already cached, it is returned directly, avoiding the overhead of reloading.
     *
     * @param path the file path of the image, relative to the classpath
     * @return the loaded {@link Image} object corresponding to the specified path
     * @throws NullPointerException if the image resource cannot be found at the given path
     */
    public static Image getImage(String path) {
        return imageCache.computeIfAbsent(path, key -> new Image(ResourceManager.class.getResourceAsStream(key)));
    }
}
