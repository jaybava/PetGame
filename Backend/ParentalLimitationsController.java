package Backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The `ParentalLimitationsController` class manages the Parental Limitations screen.
 * This screen allows the user to set start and end times for restricted play,
 * ensuring gameplay can only occur during specified hours.
 *
 * @author      Saqib Usman <susman25@uwo.ca>
 * @version     1.9
 * @since       1.5
 */
public class ParentalLimitationsController {

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

    /** TextField for the start time of restricted play (0-23). */
    @FXML
    private TextField startTime;

    /** TextField for the end time of restricted play (0-23). */
    @FXML
    private TextField endTime;

    /** Label to provide feedback to the user about their input. */
    @FXML
    private Label feedbackLabel;

    /** Button to submit the parental control time limits. */
    @FXML
    private Button parentalSubmit;

    /**
     * Handles the submission of parental control time limits.
     * Validates the input values to ensure they are integers between 0 and 23
     * and represent valid hours. If valid, the time limits are saved to the CSV data store.
     * <ul>
     *   <li>If the input is valid:
     *     <ul>
     *       <li>Updates the start and end times in the CSV data store</li>
     *       <li>Provides visual feedback indicating success</li>
     *     </ul>
     *   </li>
     *   <li>If the input is invalid:
     *     <ul>
     *       <li>Displays an error message indicating the issue</li>
     *     </ul>
     *   </li>
     * </ul>
     */
    @FXML
    public void handleSubmit() {
        try {
            int start = Integer.parseInt(startTime.getText());
            int end = Integer.parseInt(endTime.getText());

            if (start < 0 || start > 24 || end < 0 || end > 24) {
                throw new NumberFormatException("Invalid time range");
            }

            // Save the time limits to the CSV file
            saveTimeLimitsToCSV(start, end);

            feedbackLabel.setText("Times updated: " + start + " to " + end);
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Invalid input. Please enter valid hours (0-23).");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Saves the validated time limits to the CSV file using the `CSVController`.
     *
     * @param start the start time for restricted play
     * @param end   the end time for restricted play
     */
    private void saveTimeLimitsToCSV(int start, int end) {
        CSVController.writeTimeInfo(start, end);
    }

    /**
     * Handles the back button action.
     * Navigates the user back to the Parental Settings screen.
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalSettings.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            ParentalSettingController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}