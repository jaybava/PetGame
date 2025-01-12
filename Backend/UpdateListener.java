package Backend;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9              (current version number of program)
 * @since       1.7          (the version of the package this class was first added to)
 */

public interface UpdateListener {
    void onUpdate(String[] toothlessData, String[] shrekData,String[] pussData, boolean[] parentalInfo);
}
