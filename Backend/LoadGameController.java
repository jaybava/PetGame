package Backend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author      Sebastien Moroz <smoroz4@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.2          (the version of the package this class was first added to)
 */

/**
 * Controller for managing the "Load Game" screen.
 * This class handles the interaction for selecting a pet to load from the saved game data,
 * disabling buttons based on availability, and navigating to the gameplay screen.
 */
public class LoadGameController {

    private Stage primaryStage;
    @FXML
    private AnchorPane background;
    @FXML
    private Button toothlessButton;
    @FXML
    private Button shrekButton;
    @FXML
    private Button pussButton;

    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage The main stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the load game screen, disabling pet selection buttons based on
     * saved game data from CSVDataStore.
     * Also sets up the key press event to return to the main menu on pressing ESC.
     */
    @FXML
    public void initialize() {
        // Access data from CSVDataStore
        String[] toothless = CSVDataStore.getInstance().getToothless();
        String[] shrek = CSVDataStore.getInstance().getShrek();
        String[] puss = CSVDataStore.getInstance().getPuss();

        // Disable buttons if corresponding pet data is unavailable
        if (!"TRUE".equals(toothless[0])) {
            toothlessButton.setDisable(true);
        }
        if (!"TRUE".equals(shrek[0])) {
            shrekButton.setDisable(true);
        }
        if (!"TRUE".equals(puss[0])) {
            pussButton.setDisable(true);
        }

        // Add ESC key listener to return to the main menu
        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    goToMainMenu();
                }
            });
        });
    }

    /**
     * Navigates to the main menu screen.
     */
    private void goToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();

            Scene mainMenuScene = new Scene(mainMenuRoot);
            mainMenuScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(mainMenuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the selection of the Puss pet. Sets the selected pet ID and navigates to the gameplay screen.
     */
    @FXML
    private void pussSelected() {
        PeriodicUpdateService.getInstance().setSelectedPetId(3);
        loadGameplayScreen(3);
    }

    /**
     * Handles the selection of the Shrek pet. Sets the selected pet ID and navigates to the gameplay screen.
     */
    @FXML
    private void shrekSelected() {
        PeriodicUpdateService.getInstance().setSelectedPetId(1);
        loadGameplayScreen(1);
    }

    /**
     * Handles the selection of the Toothless pet. Sets the selected pet ID and navigates to the gameplay screen.
     */
    @FXML
    private void toothlessSelected() {
        PeriodicUpdateService.getInstance().setSelectedPetId(2);
        loadGameplayScreen(2);
    }

    /**
     * Loads the gameplay screen for the selected pet.
     *
     * @param petId The ID of the selected pet to load.
     */
    private void loadGameplayScreen(int petId) {
        try {
            // Load the GameplayScreen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/GameplayScreen.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            GameplayScreenController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(petId);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

