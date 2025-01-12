package Backend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.Instant;

/**
 * The `ParentalSettingController` class manages the Parental Settings screen,
 * where users can configure parental control settings, track playtime statistics, and revive pets.
 *
 * @author      Saqib Usman <susman25@uwo.ca>
 * @version     1.9
 * @since       1.5
 */
public class ParentalSettingController {

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /** The start time of the current session, used for tracking session playtime. */
    private Instant sessionStartTime;

    /** The total playtime accumulated in seconds. */
    private long totalPlayTime;

    /** The total number of play sessions. */
    private static int sessionCount;

    @FXML
    private AnchorPane background;

    @FXML
    private RadioButton math;

    @FXML
    private RadioButton english;

    @FXML
    private RadioButton geography;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio4;

    @FXML
    private RadioButton radio6;

    @FXML
    private Button toothlessRevive;

    @FXML
    private Button shrekRevive;

    @FXML
    private Button pussRevive;

    @FXML
    private Label totalPlayTimeLabel;

    @FXML
    private Label averagePlayTimeLabel;

    @FXML
    private Button resetPlayTimeButton;

    /**
     * Initializes the controller. Loads parental control data, sets up keyboard shortcuts,
     * and initializes pet revival actions.
     */
    @FXML
    public void initialize() {
        CSVController.readParentalInfo();
        loadParentalInfo();
        Platform.runLater(() -> {
            background.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    back();
                } else if (event.getCode() == KeyCode.P) {
                    limitations();
                }
            });
        });
        sessionStartTime = Instant.now();
        revivePet();
    }

    /**
     * Loads parental control settings and playtime statistics from the CSV data store.
     * Updates the UI elements with the loaded data.
     */
    public void loadParentalInfo() {
        CSVController.readParentalInfo();
        CSVController.readTimePlay();
        boolean[] parentalInfo = CSVDataStore.getInstance().getParentalInfo();
        math.setSelected(parentalInfo[0]);
        english.setSelected(parentalInfo[1]);
        geography.setSelected(parentalInfo[2]);
        radio2.setSelected(parentalInfo[3]);
        radio4.setSelected(parentalInfo[4]);
        radio6.setSelected(parentalInfo[5]);

        totalPlayTime = CSVDataStore.getInstance().getTotalPlayTime();
        sessionCount = CSVDataStore.getInstance().getSessionCount();

        updatePlaytimeLabels();
    }

    /**
     * Saves the current parental control settings and playtime statistics to the CSV data store.
     */
    private void saveParentalInfo() {
        boolean[] parentalInfo = CSVDataStore.getInstance().getParentalInfo();
        parentalInfo[0] = math.isSelected();
        parentalInfo[1] = english.isSelected();
        parentalInfo[2] = geography.isSelected();
        parentalInfo[3] = radio2.isSelected();
        parentalInfo[4] = radio4.isSelected();
        parentalInfo[5] = radio6.isSelected();

        CSVController.writeParentalInfo(parentalInfo);
        CSVController.writeTimePlay(totalPlayTime, sessionCount);
    }

    /**
     * Updates the playtime statistics displayed on the screen, including total playtime
     * and average playtime per session.
     */
    private void updatePlaytimeLabels() {
        totalPlayTimeLabel.setText("Total Play Time: " + formatTime(totalPlayTime));
        if (sessionCount > 0) {
            long averagePlayTime = totalPlayTime / sessionCount;
            averagePlayTimeLabel.setText("Average Play Time: " + formatTime(averagePlayTime));
        } else {
            averagePlayTimeLabel.setText("Average Play Time: 00:00:00");
        }
    }

    /**
     * Formats a time duration in seconds into a string in the format `HH:mm:ss`.
     *
     * @param seconds the time duration in seconds
     * @return the formatted time string
     */
    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * Resets the playtime statistics to zero and saves the changes to the CSV data store.
     */
    @FXML
    private void resetPlaytimeStats() {
        totalPlayTime = 0;
        sessionCount = 1;
        updatePlaytimeLabels();
        saveParentalInfo();
    }

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Increases the session count by one. Used for tracking the number of play sessions.
     */
    public static void increasesSessionCount() {
        sessionCount++;
    }

    /**
     * Handles the back button action. Saves the current settings and updates the
     * total playtime for the current session. Then navigates back to the main menu.
     */
    @FXML
    private void back() {
        saveParentalInfo();

        long sessionPlayTime = Duration.between(sessionStartTime, Instant.now()).getSeconds();
        totalPlayTime += sessionPlayTime;

        CSVController.updateTimePlay(sessionPlayTime);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MainMenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the Parental Limitations screen, allowing users to set time restrictions.
     */
    @FXML
    private void limitations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalLimitations.fxml"));
            Parent root = loader.load();

            Scene SceneforLimit = new Scene(root);
            SceneforLimit.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            ParentalLimitationsController LimitationsController = loader.getController();
            LimitationsController.setPrimaryStage(primaryStage);

            primaryStage.setScene(SceneforLimit);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Revives pets by setting their attributes (hunger, happiness, health, sleep, and experience) to maximum.
     * Updates the CSV data store when the corresponding revive button is clicked.
     */
    @FXML
    private void revivePet() {
        String[] shrekAr = CSVDataStore.getInstance().getShrek();
        String[] toothlessAr = CSVDataStore.getInstance().getToothless();
        String[] pussAr = CSVDataStore.getInstance().getPuss();

        shrekAr[3] = "100";  // Hunger
        shrekAr[4] = "100";  // Happiness
        shrekAr[8] = "100";  // Health
        shrekAr[6] = "100";  // Sleep
        shrekAr[7] = "100";  // Experience

        toothlessAr[3] = "100";  // Hunger
        toothlessAr[4] = "100";  // Happiness
        toothlessAr[8] = "100";  // Health
        toothlessAr[6] = "100";  // Sleep
        toothlessAr[7] = "100";  // Experience

        pussAr[3] = "100";  // Hunger
        pussAr[4] = "100";  // Happiness
        pussAr[8] = "100";  // Health
        pussAr[6] = "100";  // Sleep
        pussAr[7] = "100";  // Experience

        shrekRevive.setOnAction(actionEvent -> {
            CSVDataStore.getInstance().setShrek(shrekAr);
            CSVController.writePetInfo(1);
        });
        toothlessRevive.setOnAction(actionEvent -> {
            CSVDataStore.getInstance().setShrek(toothlessAr);
            CSVController.writePetInfo(2);
        });
        pussRevive.setOnAction(actionEvent -> {
            CSVDataStore.getInstance().setShrek(pussAr);
            CSVController.writePetInfo(3);
        });
    }
}
