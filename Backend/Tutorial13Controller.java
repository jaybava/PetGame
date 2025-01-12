package Backend;
//import statements
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author      Saqib Usman <susman25@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 */

/**
 * Controller for the thirteenth tutorial scene in the application.
 *
 * <p>This class handles the display of the selected pet image and allows the user to interact with the pet
 * by performing actions such as feeding, playing, and sleeping. It also manages the transition to the gameplay screen
 * and updates the pet's status in the data store.</p>
 */
public class Tutorial13Controller {

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
     * Buttons for interacting with the pet (sleep, play, feed).
     */
    @FXML
    private Button sleep;
    @FXML
    private Button play;
    @FXML
    private Button feed;

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
     * <p>Loads the images for all pets and prepares the interface for pet interaction.</p>
     */
    @FXML
    public void initialize() {
        shrek = new Image(getClass().getResourceAsStream("/Scenes/images/shrekIdle.png"));
        toothless = new Image(getClass().getResourceAsStream("/Scenes/images/toothlessIdle.png"));
        pussNBoots = new Image(getClass().getResourceAsStream("/Scenes/images/pussnbootsIdle.png"));
    }

    /**
     * Updates the selected pet's status in the CSV data store.
     *
     * <p>This method decreases hunger, happiness, and sleep levels, resets other status attributes,
     * and writes the updated pet data to the CSV file. It also notifies listeners about the update.</p>
     */
    private void updateSelectedPet() {
        String[] selectedPet = null;

        synchronized (CSVDataStore.getInstance()) {
            switch (pet) {
                case 2 -> selectedPet = CSVDataStore.getInstance().getToothless();
                case 1 -> selectedPet = CSVDataStore.getInstance().getShrek();
                case 3 -> selectedPet = CSVDataStore.getInstance().getPuss();
            }
        }

        if (selectedPet != null) {
            // Reset pet attributes
            int hunger = 100;
            int happiness = 100;
            int sleep = 100;
            int health = 100;
            int experience = 0;
            int level = 0;
            int coins = 0;

            // Update the selected pet's data
            selectedPet[3] = String.valueOf(hunger);
            selectedPet[4] = String.valueOf(happiness);
            selectedPet[5] = String.valueOf(coins);
            selectedPet[6] = String.valueOf(sleep);
            selectedPet[7] = String.valueOf(experience);
            selectedPet[8] = String.valueOf(health);
            selectedPet[9] = String.valueOf(level);
            selectedPet[20] = "DEFAULT";
            selectedPet[11] = "FALSE";
            selectedPet[12] = "FALSE";
            selectedPet[13] = "FALSE";
            selectedPet[14] = "FALSE";
            selectedPet[15] = "FALSE";
            selectedPet[16] = "FALSE";
            selectedPet[17] = "FALSE";
            selectedPet[18] = "FALSE";
            selectedPet[19] = "FALSE";

            // Write changes to CSV
            CSVController.writePetInfo(pet);

            // Notify listeners about the update
            Platform.runLater(UpdateService.getInstance()::notifyListeners);
        }
    }

    /**
     * Navigates back to the twelfth tutorial scene.
     *
     * <p>This method loads the twelfth tutorial FXML file, transfers the current pet selection,
     * and updates the primary stage with the new scene.</p>
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial12.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial12Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Advances to the gameplay screen after updating the pet's status.
     *
     * <p>This method updates the pet data, loads the gameplay screen FXML, and transfers the current pet selection
     * to the gameplay screen controller.</p>
     */
    @FXML
    private void next() {
        try {
            updateSelectedPet();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/GameplayScreen.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            GameplayScreenController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
