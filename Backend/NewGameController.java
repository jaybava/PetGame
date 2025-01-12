package Backend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The `NewGameController` class manages the New Game screen,
 * allowing users to select a pet and proceed to the tutorial.
 *
 * @author      Sebastien Moroz <smoroz4@uwo.ca>
 * @version     1.9
 * @since       1.1
 */
public class NewGameController {

    /** The background anchor pane for handling key events. */
    @FXML
    private AnchorPane background;

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller.
     * Sets up an event handler to navigate back to the main menu when the Escape key is pressed.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    goToMainMenu();
                }
            });
        });
    }

    /**
     * Handles the selection of the pet "Puss".
     * Loads the tutorial scene and sets "Puss" as the selected pet.
     */
    @FXML
    private void pussSelected() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial1.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial1Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(3); // Set pet ID for Puss

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the selection of the pet "Shrek".
     * Loads the tutorial scene and sets "Shrek" as the selected pet.
     */
    @FXML
    private void shrekSelected() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial1.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial1Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(1); // Set pet ID for Shrek

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the selection of the pet "Toothless".
     * Loads the tutorial scene and sets "Toothless" as the selected pet.
     */
    @FXML
    private void toothlessSelected() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Tutorial1.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            Tutorial1Controller controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(2); // Set pet ID for Toothless

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the main menu.
     * Loads the main menu scene.
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
}
