/**
 * The `MainMenuController` class manages the interactions and navigation of the main menu in the game.
 * It includes functionality for starting a new game, loading a game, accessing parental controls, and
 * handling playtime restrictions.
 *
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.7                 (the version of the package this class was first added to)
 */
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

import java.time.LocalTime;

public class MainMenuController {

    /** The start time when the game is allowed to be played. */
    private int startTime;

    /** The end time when the game is allowed to be played. */
    private int endTime;

    /** The primary stage for rendering scenes. */
    Stage primaryStage;

    /** The current local time. */
    LocalTime now = LocalTime.now();

    /** The current hour in the day. */
    int currentTime = now.getHour();

    @FXML
    AnchorPane background;

    /**
     * Initializes the main menu controller. Reads parental info and sets up key listeners.
     */
    @FXML
    public void initialize() {
        CSVController.readParentalInfo();
        Platform.runLater(() -> background.requestFocus());
        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    closeProgram();
                }
            });
        });
    }

    /**
     * Sets the primary stage for the controller.
     *
     * @param primaryStage the primary stage for the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gracefully exits the application when called.
     */
    @FXML
    private void closeProgram() {
        Platform.exit();  // Gracefully exits the application
        System.exit(0);   // Ensures the JVM terminates
    }

    /**
     * Determines whether the player is allowed to play based on time restrictions.
     *
     * @return true if the player is allowed to play, false otherwise
     */
    private boolean isAllowedToPlay() {
        CSVController.readTimeInfo();
        startTime = CSVDataStore.getInstance().getStartTime();
        endTime = CSVDataStore.getInstance().getEndTime();

        LocalTime now = LocalTime.now();
        int currentTime = now.getHour();

        if (startTime <= endTime) {
            return currentTime >= startTime && currentTime < endTime;
        } else {
            return currentTime >= startTime || currentTime < endTime;
        }
    }

    /**
     * Handles the action of starting a new game.
     * Navigates to the new game screen if allowed, otherwise loads a restriction screen.
     */
    @FXML
    private void handleNewGame() {
        String[] shrek = CSVDataStore.getInstance().getShrek();
        if (" ".equals(shrek[1])) {
            loadParentalControls();
        }
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
        if (!isAllowedToPlay()) {
            loadCantPlayScreen();
        }
    }

    /**
     * Handles the action of loading a saved game.
     * Navigates to the load game screen if allowed, otherwise loads a restriction screen.
     */
    @FXML
    private void handleLoadGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/LoadGame.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            LoadGameController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isAllowedToPlay()) {
            loadCantPlayScreen();
        }
    }

    /**
     * Navigates to the parental controls screen.
     */
    @FXML
    private void loadParentalControls() {
        CSVController.readParentalInfo();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalSettings.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            ParentalSettingController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.loadParentalInfo();

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the screen notifying the player they cannot play due to restrictions.
     */
    private void loadCantPlayScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/CantPlayScreen.fxml"));
            Parent cantPlayRoot = loader.load();

            Scene cantPlayScene = new Scene(cantPlayRoot);
            cantPlayScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            CantPlayController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(cantPlayScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the parental controls input screen.
     */
    @FXML
    private void inputScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalControlsInput2.fxml"));
            Parent inputRoot = loader.load();

            Scene inputScene = new Scene(inputRoot);
            inputScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            ParentalControlsController2 controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(inputScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
