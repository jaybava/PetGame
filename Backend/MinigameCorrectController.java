package Backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * The `MinigameCorrectController` class manages the UI and logic for the
 * "correct answer" screen in the minigame. It handles updating the selected pet's
 * stats (experience, level, and coins) based on the rewards for answering correctly.
 *
 * @author      Naween Sarwari <nsarwari@uwo.ca>
 * @version     1.9
 * @since       1.4
 */
public class MinigameCorrectController implements UpdateListener {

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /** The ID of the selected pet. */
    private int pet;

    /** Manager for handling pet-related functionality, such as setting images. */
    private PetManager petManager;

    /** The question data for the correct answer, including rewards. */
    private String[] randomQuestion;

    @FXML
    private ImageView petImage;

    @FXML
    private ProgressBar hungerBar;

    @FXML
    private ProgressBar sleepBar;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private ProgressBar happinessBar;

    @FXML
    private ProgressBar experienceBar;

    @FXML
    private Label levelLabel;

    @FXML
    private Label coinsLabel;

    @FXML
    private Label correctBox;

    @FXML
    private Button shopButton;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the selected pet and updates the displayed image.
     *
     * @param pet the ID of the selected pet (1 for Shrek, 2 for Toothless, 3 for Puss)
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet, petImage);
    }

    /**
     * Sets the question data and processes the rewards (experience and coins) for the correct answer.
     *
     * @param randomQuestion an array containing question data, including reward information
     */
    public void setRandomQuestion(String[] randomQuestion) {
        this.randomQuestion = randomQuestion;

        // Extract experience and coins rewards from the question
        int experienceReward = Integer.parseInt(randomQuestion[7]);
        int coinsReward = Integer.parseInt(randomQuestion[6]);

        // Update the pet's stats
        String[] selectedPet = switch (pet) {
            case 1 -> CSVDataStore.getInstance().getShrek();
            case 2 -> CSVDataStore.getInstance().getToothless();
            case 3 -> CSVDataStore.getInstance().getPuss();
            default -> null;
        };

        if (selectedPet != null) {
            // Update experience and coins
            int currentExperience = Integer.parseInt(selectedPet[7]);
            int currentLevel = Integer.parseInt(selectedPet[9]);

            int newExperience = currentExperience + experienceReward;
            if (newExperience > 100) {
                newExperience = newExperience % 100; // Reset experience if it exceeds 100
                currentLevel += 1;                   // Level up
            }
            selectedPet[7] = String.valueOf(newExperience);
            selectedPet[9] = String.valueOf(currentLevel);

            int currentCoins = Integer.parseInt(selectedPet[5]);
            selectedPet[5] = String.valueOf(currentCoins + coinsReward);

            // Save back to CSVDataStore
            switch (pet) {
                case 1 -> CSVDataStore.getInstance().setShrek(selectedPet);
                case 2 -> CSVDataStore.getInstance().setToothless(selectedPet);
                case 3 -> CSVDataStore.getInstance().setPuss(selectedPet);
            }

            // Write updates to CSV
            CSVController.writePetInfo(pet);

            // Refresh UI to reflect updated stats
            displayPetData();
        }

        // Display feedback to the user
        correctBox.setWrapText(true);
        correctBox.setText(("Congrats you got it right! You got awarded " + randomQuestion[7] +
                " experience and " + randomQuestion[6] + " coins").toUpperCase());
    }

    /**
     * Displays the stats of the selected pet, including hunger, sleep, health, happiness,
     * experience, level, and coins.
     */
    private void displayPetData() {
        String[] selectedPet = switch (pet) {
            case 1 -> CSVDataStore.getInstance().getShrek();
            case 2 -> CSVDataStore.getInstance().getToothless();
            case 3 -> CSVDataStore.getInstance().getPuss();
            default -> null;
        };

        if (selectedPet != null) {
            hungerBar.setProgress(Double.parseDouble(selectedPet[3]) / 100);
            happinessBar.setProgress(Double.parseDouble(selectedPet[4]) / 100);
            healthBar.setProgress(Double.parseDouble(selectedPet[8]) / 100);
            sleepBar.setProgress(Double.parseDouble(selectedPet[6]) / 100);
            experienceBar.setProgress(Double.parseDouble(selectedPet[7]) / 100);
            levelLabel.setText(selectedPet[9]);
            coinsLabel.setText(selectedPet[5]);
        }
    }

    /**
     * Handles updates to pet data.
     * Updates the displayed stats when notified of changes.
     *
     * @param toothlessData data for Toothless
     * @param shrekData     data for Shrek
     * @param pussData      data for Puss
     * @param parentalInfo  boolean array representing parental control settings
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        displayPetData();
    }

    /**
     * Initializes the controller, including setting up the shop button icon
     * and adding this controller as a listener to the `UpdateService`.
     */
    @FXML
    public void initialize() {
        // Create an ImageView for the shop icon
        ImageView shopIcon = new ImageView(new Image(getClass().getResourceAsStream("/Scenes/images/storeIcon.png")));
        shopIcon.setFitWidth(32);  // Set icon width
        shopIcon.setFitHeight(32); // Set icon height
        shopButton.setGraphic(shopIcon);
        shopButton.setContentDisplay(ContentDisplay.TOP);

        petManager = PetManager.getInstance();
        UpdateService.getInstance().addListener(this);
    }

    /**
     * Navigates back to the Gameplay screen.
     */
    @FXML
    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/GameplayScreen.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            GameplayScreenController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the Shop screen.
     */
    @FXML
    private void shop() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Shop.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            ShopController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to a new question in the minigame.
     */
    @FXML
    private void newQuestion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Minigame.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            MinigameController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
