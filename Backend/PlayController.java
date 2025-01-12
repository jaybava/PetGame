package Backend;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * PlayController handles the logic for the pet's play screen of the application.
 * It manages the pet's play animations, updates the pet's happiness, and transitions back to the gameplay screen.
 *
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9 (current version number of program)
 * @since       1.6 (the version of the package this class was first added to)
 */
public class PlayController {

    /** The main stage of the application. */
    private Stage primaryStage;

    /** The identifier for the selected pet. */
    private int pet;

    /** The ImageView used to display the pet's image during play. */
    @FXML
    private ImageView petImage;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the main stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the pet identifier and initializes the pet's play image and animations.
     *
     * @param pet an integer representing the selected pet.
     */
    public void setPet(int pet) {
        this.pet = pet;
        switch (pet) {
            case 1 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/shrekPlaying.gif")));
            case 2 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/toothlesPlay.gif")));
            case 3 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/pussbootsPlay.gif")));
        }
        adjustImageView();
        playAnimation();
    }

    /**
     * Adjusts the ImageView settings to fit within the container while maintaining the image's aspect ratio.
     */
    private void adjustImageView() {
        petImage.setFitWidth(739); // Replace with your container's width
        petImage.setPreserveRatio(true); // Maintain the image's aspect ratio
        petImage.setSmooth(true); // Enable smooth scaling
        petImage.setCache(true); // Cache the scaled image for better performance
    }

    /**
     * Plays the pet's play animation and updates the pet's happiness.
     * After a delay, transitions back to the gameplay screen.
     */
    private void playAnimation() {
        // Set the animation image based on the selected pet
        String[] selectedPet = switch (pet){
            case 1 -> CSVDataStore.getInstance().getShrek();
            case 2 -> CSVDataStore.getInstance().getToothless();
            case 3 -> CSVDataStore.getInstance().getPuss();
            default -> null;
        };

        if (selectedPet != null) {
            int happinessValue = Integer.parseInt(selectedPet[4]);
            happinessValue = Math.min(happinessValue + 25, 100); // Ensure the value doesn't exceed 100
            selectedPet[4] = String.valueOf(happinessValue); // Update happiness value in the array

            // Write back to CSV
            switch (pet) {
                case 1 -> CSVDataStore.getInstance().setShrek(selectedPet);
                case 2 -> CSVDataStore.getInstance().setToothless(selectedPet);
                case 3 -> CSVDataStore.getInstance().setPuss(selectedPet);
            }
            CSVController.writePetInfo(pet);
        }

        // Create a PauseTransition to wait for 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        // Set the action to transition back to the gameplay screen after 5 seconds
        pause.setOnFinished(event -> backToGameplay());

        // Start the pause timer
        pause.play();
    }

    /**
     * Transitions back to the gameplay screen, passing the current stage and pet data.
     */
    private void backToGameplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/GameplayScreen.fxml"));
            Parent gameplayRoot = loader.load();

            Scene gameplayScene = new Scene(gameplayRoot);
            gameplayScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            GameplayScreenController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(gameplayScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
