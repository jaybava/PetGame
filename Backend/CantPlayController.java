package Backend;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller class for handling the "Can't Play" screen.
 * This screen is displayed when the player is restricted from playing the game.
 * The controller manages UI interactions and transitions back to the main menu.
 *
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9 (current version number of program)
 * @since       1.2 (the version of the package this class was first added to)
 */
public class CantPlayController {

    /** The primary stage of the application. */
    private Stage primaryStage;

    /** The background anchor pane of the "Can't Play" screen. */
    @FXML
    private AnchorPane background;

    /**
     * Sets the primary stage for the controller.
     *
     * @param primaryStage the primary stage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller.
     * Requests focus on the background and sets up an event handler
     * to listen for the ESCAPE key press.
     */
    @FXML
    public void initialize() {
        // Request focus on the background pane after the UI is loaded
        Platform.runLater(() -> background.requestFocus());

        // Add an event handler for the ESCAPE key
        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    // Handle ESCAPE key press if needed
                }
            });
        });
    }

    /**
     * Handles the back button action.
     * Navigates the user back to the Main Menu screen.
     */
    public void back() {
        try {
            // Load the Main Menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MainMenu.fxml"));
            Parent newGameRoot = loader.load();

            // Create and style the new scene
            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            // Get the MainMenuController and set the primary stage
            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            // Set the scene and show the stage
            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
