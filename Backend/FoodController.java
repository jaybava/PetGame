package Backend;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FoodController handles the logic for feeding a pet in the application.
 * It manages the pet's food actions, updates the pet's hunger level,
 * and transitions back to the gameplay screen.
 *
 * @author      Inderpreet Doad <idoad@uwo.ca>
 * @version     1.9 (current version number of program)
 * @since       1.6 (the version of the package this class was first added to)
 */
public class FoodController {

    /** The identifier for the selected pet. */
    private int petID;

    /** Buttons for different food items to feed the pet. */
    @FXML
    private Button useApple, useBanana, usePeach;

    /** Display the pet image while eating. */
    @FXML
    private ImageView petImage;

    /** Display images for the food items available to the pet. */
    @FXML
    private ImageView food1, food2, food3;

    /** The primary stage of the application. */
    private Stage primaryStage;

    /** The PetManager instance used to manage pet attributes. */
    private PetManager petManager;

    /** Progress bar to display the pet's hunger level. */
    @FXML
    private ProgressBar hungerBar;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the main stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the pet identifier and initializes the pet's image and other food-related information.
     *
     * @param petID an integer representing the selected pet.
     */
    public void setPet(int petID) {
        this.petID = petID;
        petManager = PetManager.getInstance();
        switch (petID) {
            case 1 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/shrekEating.gif")));
            case 2 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/toothlesEating.gif")));
            case 3 -> petImage.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/pussbootsEating.gif")));
        }
        playAnimation();
        updatePetInfo();
    }

    /**
     * Initializes the controller and sets up button actions for feeding the pet.
     */
    @FXML
    public void initialize() {
        petManager = PetManager.getInstance();

        // Set up button actions
        useApple.setOnAction(event -> feedPet(10)); // Feed with apple (food value: 10)
        useBanana.setOnAction(event -> feedPet(20)); // Feed with banana (food value: 20)
        usePeach.setOnAction(event -> feedPet(30)); // Feed with peach (food value: 30)

        // Set the images for different food tiers
        food1.setImage(petManager.getFoodImage("foodTier1"));
        food2.setImage(petManager.getFoodImage("foodTier2"));
        food3.setImage(petManager.getFoodImage("foodTier3"));
    }

    /**
     * Starts a pause transition animation to simulate the pet's feeding process.
     */
    private void playAnimation() {
        // Create a PauseTransition to wait for 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        // Start the pause timer
        pause.play();
    }

    /**
     * Feeds the pet and updates its hunger level based on the food value.
     *
     * @param foodValue the amount of food to feed the pet.
     */
    private void feedPet(int foodValue) {
        petManager.feedPet(petID, foodValue);
        updatePetInfo();
    }

    /**
     * Updates the pet's information such as hunger level and reflects it on the progress bar.
     */
    private void updatePetInfo() {
        String[] petData = petManager.getPetData(petID);
        if (petData != null) {
            int hunger = Integer.parseInt(petData[3]);
            hungerBar.setProgress(hunger / 100.0);
        }
    }

    /**
     * Transitions back to the gameplay screen.
     */
    @FXML
    private void back() {
        try {
            // Load the GameplayScreen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/GameplayScreen.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            GameplayScreenController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(petID);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
