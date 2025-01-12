package Backend;
//import statements
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9              (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the first tutorial scene in the application.
 *
 * <p>This class handles the initialization and behavior of the tutorial scene,
 * including setting the pet image, handling key events, and transitioning to
 * the next or previous scenes.</p>
 */
public class Tutorial1Controller {

    /**
     * The primary stage used to display the application's scenes.
     */
    private Stage primaryStage;

    /**
     * The background container for the tutorial scene.
     */
    @FXML
    private AnchorPane background;

    /**
     * The {@link ImageView} for displaying the selected pet.
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
     * Sets the selected pet and updates the {@link ImageView} accordingly.
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
     * <p>Loads the pet images, sets up key event handling, and ensures the
     * scene responds to the ESC key to go back.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));

        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    back();
                }
            });
        });
    }

    /**
     * Advances to the next tutorial scene.
     *
     * <p>This method loads the second tutorial FXML, sets the selected pet,
     * and displays the new scene.</p>
     */
    @FXML
    private void next() {
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
     * Returns to the new game selection scene.
     *
     * <p>This method loads the new game FXML and updates the primary stage
     * to display the new scene.</p>
     */
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/NewGame.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            NewGameController newGameController = loader.getController();
            newGameController.setPrimaryStage(primaryStage);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
