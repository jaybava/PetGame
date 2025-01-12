package Backend;
//import statements
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the third tutorial scene in the application.
 *
 * <p>This class handles the functionality of the third tutorial scene, including
 * displaying the selected pet image, navigating to the previous and next scenes,
 * and initializing scene-specific resources.</p>
 */
public class Tutorial3Controller {

    /**
     * The primary stage used to display the application's scenes.
     */
    private Stage primaryStage;

    /**
     * The {@link ImageView} for displaying the selected pet image.
     */
    @FXML
    private ImageView petImage;

    /**
     * The selected pet identifier.
     * 1 = Shrek, 2 = Toothless, 3 = Puss in Boots.
     */
    private int pet;

    private Image shrek;
    private Image toothless;
    private Image pussNBoots;

    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage the primary stage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the selected pet and updates the {@link ImageView} to display the corresponding image.
     *
     * @param pet the identifier for the selected pet
     */
    public void setPet(int pet) {
        this.pet = pet;
        switch (pet) {
            case 1:
                petImage.setImage(shrek);
                break;
            case 2:
                petImage.setImage(toothless);
                break;
            case 3:
                petImage.setImage(pussNBoots);
                break;
        }
    }

    /**
     * Initializes the controller.
     *
     * <p>Loads the images for all available pets so they can be displayed
     * dynamically when the pet selection is updated.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));
    }

    /**
     * Navigates back to the second tutorial scene.
     *
     * <p>This method loads the second tutorial FXML file, sets the selected pet,
     * and updates the primary stage to display the new scene.</p>
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial2.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial2Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Advances to the fourth tutorial scene.
     *
     * <p>This method loads the fourth tutorial FXML file, sets the selected pet,
     * and updates the primary stage to display the new scene.</p>
     */
    @FXML
    private void next() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial4.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial4Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
