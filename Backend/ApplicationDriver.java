package Backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application driver for the "Paws, Scales, and Tales" game.
 * This class initializes the application, loads necessary resources,
 * and controls navigation between the main menu and parental controls screen.
 *
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.1                 (the version of the package this class was first added to)
 */
public class ApplicationDriver extends Application {

    /**
     * Starts the JavaFX application.
     * Determines the initial screen to display based on parental controls
     * and sets up the primary stage.
     *
     * @param primaryStage the primary stage for the application
     * @throws Exception if any error occurs during the initialization of the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Update playtime when a new game is started
        CSVController.updateTimePlayWhenNewGame();

        // Retrieve Shrek's data from the CSV datastore
        String[] shrek = CSVDataStore.getInstance().getShrek();
        System.out.println(shrek[1]);

        // Check parental control status
        if ("****".equals(shrek[1])) {
            try {
                // Load Parental Controls Input screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/ParentalControlsInput.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                ParentalControlsInputController controller = loader.getController();
                controller.setPrimaryStage(primaryStage);

                // Stop periodic updates while on the parental controls screen
                PeriodicUpdateService.getInstance().stopUpdating();

                // Set up the primary stage
                primaryStage.setScene(scene);
                primaryStage.setOnCloseRequest(event -> {
                    UpdateService.getInstance().removeAllListeners();
                    PeriodicUpdateService.getInstance().stopUpdating();
                    System.exit(0);
                });

                primaryStage.setResizable(false);
                primaryStage.setFullScreen(false);
                primaryStage.setTitle("Paws, Scales, and Tales");
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Load Main Menu screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MainMenu.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                MainMenuController mainMenuController = loader.getController();
                mainMenuController.setPrimaryStage(primaryStage);

                // Set up the primary stage
                primaryStage.setScene(scene);
                primaryStage.setOnCloseRequest(event -> {
                    UpdateService.getInstance().removeAllListeners();
                    PeriodicUpdateService.getInstance().stopUpdating();
                    System.exit(0);
                });

                primaryStage.setResizable(false);
                primaryStage.setFullScreen(false);
                primaryStage.setTitle("Paws, Scales, and Tales");
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main method for launching the JavaFX application.
     * Initializes necessary resources such as CSV data and preloaded images,
     * then launches the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Initialize pet data and preload resources
        CSVController.readPetInfo();
        PetManager petManager = PetManager.getInstance();
        petManager.preloadImages();

        System.out.println("CSV Controller initialized");
        launch();
    }
}
