package Backend;
//import statements
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the ninth tutorial scene in the application.
 *
 * <p>This class manages the logic for displaying the pet image, handling inventory actions,
 * and navigating between tutorial scenes.</p>
 */
public class Tutorial9Controller {

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
     * The {@link Button} for accessing the inventory.
     */
    @FXML
    private Button inventoryButton;

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
     * <p>Loads the images for all pets and sets up the inventory button with an icon.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));

        // Set up the inventory button with an icon
        ImageView inventoryIcon = new ImageView(new Image(getClass().getResourceAsStream("/Scenes/images/inventoryIcon.png")));
        inventoryIcon.setFitWidth(32);  // Set icon width
        inventoryIcon.setFitHeight(32); // Set icon height
        inventoryButton.setGraphic(inventoryIcon);
        inventoryButton.setContentDisplay(ContentDisplay.TOP);
    }

    /**
     * Navigates back to the eighth tutorial scene.
     *
     * <p>This method loads the eighth tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial8.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial8Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Advances to the tenth tutorial scene.
     *
     * <p>This method loads the tenth tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void next() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial10.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial10Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
