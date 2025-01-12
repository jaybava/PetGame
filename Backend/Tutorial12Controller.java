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
 * @author      Sebastien Moroz <smoroz4@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the twelfth tutorial scene in the application.
 *
 * <p>This class handles the display of the pet image, the item purchase buttons, and manages navigation
 * between tutorial scenes.</p>
 */
public class Tutorial12Controller {

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
     * Buttons for buying items in the shop.
     */
    @FXML
    private Button buyItemButton1;
    @FXML
    private Button buyItemButton2;
    @FXML
    private Button buyItemButton3;

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
     * <p>Loads the images for all pets and disables the item purchase buttons.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));

        buyItemButton1.setDisable(true);
        buyItemButton2.setDisable(true);
        buyItemButton3.setDisable(true);
    }

    /**
     * Navigates back to the eleventh tutorial scene.
     *
     * <p>This method loads the eleventh tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial11.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial11Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Advances to the thirteenth tutorial scene.
     *
     * <p>This method loads the thirteenth tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void next() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial13.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial13Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
