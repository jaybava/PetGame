package Backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * The `ParentalControlsInputController` class handles the functionality of the Parental Controls input screen.
 * This screen allows users to set a new parental control password, which is validated and stored for future use.
 *
 *
 * @author      Saqib Usman <susman25@uwo.ca>
 * @version     1.9
 * @since       1.5
 */
public class ParentalControlsInputController {

    /** The primary stage of the application, used for screen transitions. */
    Stage primaryStage;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /** The password field where the user inputs their desired 4-digit password. */
    @FXML
    private PasswordField passwordField;

    /** Label to provide feedback to the user about the validity of their input. */
    @FXML
    private Label feedbackLabel;

    /** The submit button for validating and storing the parental control password. */
    @FXML
    private Button parentalSubmit;

    /**
     * Initializes the controller.
     * Sets up a listener on the password field to display the input dynamically as the user types.
     * This can be useful for debugging or feedback purposes.
     */
    @FXML
    public void initialize() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            feedbackLabel.setText(newValue); // Displays the input for demonstration purposes
        });
    }

    /**
     * Handles the submission of the parental control password.
     * Validates the input to ensure it is a 4-digit numeric value.
     * <ul>
     *   <li>If valid:
     *     <ul>
     *       <li>Stores the password for all pets in the `CSVDataStore`</li>
     *       <li>Updates the pet data in the CSV files</li>
     *       <li>Redirects the user to the Parental Settings screen</li>
     *     </ul>
     *   </li>
     *   <li>If invalid:
     *     <ul>
     *       <li>Displays a feedback message indicating the password must be 4 digits</li>
     *     </ul>
     *   </li>
     * </ul>
     */
    @FXML
    public void handleSubmit() {
        String input = passwordField.getText();

        if (input.matches("\\d{4}")) { // Validate the input is a 4-digit numeric value
            feedbackLabel.setText("Password accepted: ".toUpperCase() + input);
            feedbackLabel.setStyle("-fx-text-fill: green;"); // Green for valid input

            FileWatcher.setIsFileWatcherDisabled(true);
            String[] toothless = CSVDataStore.getInstance().getToothless();
            String[] shrek = CSVDataStore.getInstance().getShrek();
            String[] puss = CSVDataStore.getInstance().getPuss();

            // Update password for all pets
            toothless[1] = input;
            shrek[1] = input;
            puss[1] = input;

            // Log changes for debugging purposes
            System.out.println("Toothless in Handle Submit: " + Arrays.toString(toothless));
            System.out.println("Shrek in Handle Submit: " + Arrays.toString(shrek));
            System.out.println("Puss in Handle Submit: " + Arrays.toString(puss));

            // Update the `CSVDataStore` and write changes to CSV files
            CSVDataStore.getInstance().setToothless(toothless);
            CSVDataStore.getInstance().setShrek(shrek);
            CSVDataStore.getInstance().setPuss(puss);

            CSVController.writePetInfo(1); // Update Shrek's data
            CSVController.writePetInfo(2); // Update Toothless's data
            CSVController.writePetInfo(3); // Update Puss's data

            CSVController.readPetInfo(); // Reload pet data
            FileWatcher.setIsFileWatcherDisabled(false);

            try {
                // Load the Parental Settings screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalSettings.fxml"));
                Parent newGameRoot = loader.load();

                Scene newGameScene = new Scene(newGameRoot);
                newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                ParentalSettingController controller = loader.getController();
                controller.setPrimaryStage(primaryStage);

                primaryStage.setResizable(false);
                primaryStage.setFullScreen(false);

                primaryStage.setScene(newGameScene);
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Display feedback for invalid input
            feedbackLabel.setText("Invalid! Must be 4 digits.".toUpperCase());
            feedbackLabel.setStyle("-fx-text-fill: red;"); // Red for invalid input
        }
    }
}