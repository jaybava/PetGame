package Backend;
//import statements
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the eighth tutorial scene in the application.
 *
 * <p>This class manages the logic for displaying the pet image, disabling buttons initially,
 * and handling navigation to other scenes.</p>
 */
public class Tutorial8Controller {

    /**
     * The primary stage used for displaying the application's scenes.
     */
    private Stage primaryStage;

    /**
     * Sets the primary stage for the controller.
     *
     * @param primaryStage the primary stage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * The {@link ImageView} for displaying the selected pet's image.
     */
    @FXML
    private ImageView petImage;

    /**
     * The {@link Button} for geography-related actions.
     */
    @FXML
    private Button geographyButton;

    /**
     * The {@link Button} for math-related actions.
     */
    @FXML
    private Button mathButton;

    /**
     * The {@link Button} for English-related actions.
     */
    @FXML
    private Button englishButton;

    /**
     * The identifier for the selected pet.
     * 1 = Shrek, 2 = Toothless, 3 = Puss in Boots.
     */
    private int pet;

    private Image shrek;
    private Image toothless;
    private Image pussNBoots;

    /**
     * Sets the selected pet and updates the {@link ImageView} with the corresponding image.
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
     * <p>Loads the images for all pets and disables the game buttons initially.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));

        // Disable all game buttons initially
        geographyButton.setDisable(true);
        mathButton.setDisable(true);
        englishButton.setDisable(true);
    }

    /**
     * Navigates back to the seventh tutorial scene.
     *
     * <p>This method loads the seventh tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial7.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial7Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Advances to the ninth tutorial scene.
     *
     * <p>This method loads the ninth tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void next() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial9.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial9Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
