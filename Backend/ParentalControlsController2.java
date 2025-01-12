package Backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * The `ParentalControlsController2` class manages the Parental Controls screen.
 * This screen allows the user to input a password and validates it against the stored password.
 * If the input is correct, the user is redirected to the Parental Settings screen; otherwise,
 * feedback is provided, and the user may be redirected to the Main Menu.
 *
 *
 * @author      Saqib Usman <susman25@uwo.ca>
 * @version     1.9
 * @since       1.5
 */
public class ParentalControlsController2 {

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

    /** The submit button for validating the parental control password. */
    @FXML
    private Button parentalSubmit;

    /** The password field where the user enters their password. */
    @FXML
    private PasswordField passwordField;

    /** Label to display feedback about the input password's validity. */
    @FXML
    private Label feedbackLabel;

    /**
     * Initializes the controller.
     * Sets up event listeners for the password field and the submit button.
     * Displays the current password input in the feedback label for demonstration purposes.
     */
    @FXML
    public void initialize() {
        System.out.println("HELLO THIS FUNCTION IS RUNNING WHEN I PRESS IT");
        // Set up listener to display password as the user types
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            feedbackLabel.setText(newValue); // Displays the input for demonstration purposes
        });
        handleSubmit();
    }

    /**
     * Handles the submission of the parental control password.
     * Validates the input password against the stored password.
     * If valid, redirects the user to the Parental Settings screen.
     * If invalid, provides feedback and redirects the user to the Main Menu screen.
     */
    @FXML
    public void handleSubmit() {
        parentalSubmit.setOnAction(event -> {
            String input = passwordField.getText();
            String storedPassword = ""; // Variable to hold the stored password

            try {
                // Retrieve the stored password from the CSV file
                String[] shrekAr = CSVDataStore.getInstance().getShrek(); // Fetch pet data
                storedPassword = shrekAr[1]; // Assuming the second element contains the password
            } catch (Exception e) {
                e.printStackTrace();
                feedbackLabel.setText("Error retrieving password.".toUpperCase());
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return; // Exit method if an error occurs while fetching the password
            }

            if (input.equals(storedPassword)) { // Validate the input password
                feedbackLabel.setText("Password accepted".toUpperCase());
                feedbackLabel.setStyle("-fx-text-fill: green;"); // Green for valid input

                try {
                    // Load Parental Controls Settings Screen
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalSettings.fxml"));
                    Parent settingsRoot = loader.load();

                    Scene settingsScene = new Scene(settingsRoot);
                    settingsScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                    ParentalSettingController controller = loader.getController();
                    controller.setPrimaryStage(primaryStage);

                    primaryStage.setScene(settingsScene);
                    primaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                feedbackLabel.setText("Invalid password. Try again.".toUpperCase());
                feedbackLabel.setStyle("-fx-text-fill: red;"); // Red for invalid input

                try {
                    // Load Main Menu Screen
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
        });
    }
}
