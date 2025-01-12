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
 * VetController handles the logic for the pet's vet visit screen of the application.
 * It manages the pet's vet image display, updates the pet's sleep level, and transitions back to the gameplay screen.
 *
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9 (current version number of program)
 * @since       1.6 (the version of the package this class was first added to)
 */
public class VetController {

    /** The main stage of the application. */
    private Stage primaryStage;

    /** The identifier for the selected pet. */
    private int pet;

    /** The PetManager instance used to manage pet attributes. */
    private PetManager petManager;

    /** The ImageView used to display the pet's image during the vet visit. */
    @FXML
    private ImageView petImage;

    /** The images used for different pet types during the vet visit. */
    private Image theVet1;
    private Image theVet2;
    private Image theVet3;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the main stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the pet identifier and initializes the pet's image for the vet visit.
     *
     * @param pet an integer representing the selected pet.
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager = PetManager.getInstance();
        switch (pet) {
            case 1 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/shrekVet.gif")));
            case 2 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/toothlesVet.gif")));
            case 3 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/pussbootsVet.gif")));
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
     * Plays the pet's vet visit animation and updates the pet's sleep level.
     * After a delay, transitions back to the gameplay screen.
     */
    private void playAnimation() {
        petManager.increaseSleep(pet, 30);

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
