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
 * The `MinigameController` class manages the minigame screen in the application.
 * It allows users to access educational games (math, English, geography), navigate to the shop,
 * and view the selected pet's stats such as hunger, sleep, health, happiness, experience, level, and coins.
 *
 * @author      Naween Sarwari <nsarwari@uwo.ca>
 * @version     1.9
 * @since       1.4
 */
public class MinigameController implements UpdateListener {

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /** The ID of the selected pet. */
    private int pet;

    /** Manager for handling pet-related functionality, such as setting images. */
    private PetManager petManager;

    @FXML
    private ImageView petImage;

    @FXML
    private Button shopButton;

    @FXML
    private Button mathButton;

    @FXML
    private Button englishButton;

    @FXML
    private Button geographyButton;

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

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the selected pet and updates the screen with its stats and image.
     *
     * @param pet the ID of the selected pet (1 for Shrek, 2 for Toothless, 3 for Puss)
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet, petImage);
        displayPetData();
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
            sleepBar.setProgress(Double.parseDouble(selectedPet[6]) / 100);
            healthBar.setProgress(Double.parseDouble(selectedPet[8]) / 100);
            happinessBar.setProgress(Double.parseDouble(selectedPet[4]) / 100);
            experienceBar.setProgress(Double.parseDouble(selectedPet[7]) / 100);

            levelLabel.setText(selectedPet[9]);
            coinsLabel.setText(selectedPet[5]);
        }
    }

    /**
     * Handles updates to pet data and parental control settings.
     * Disables or enables minigame buttons based on the parental control settings.
     *
     * @param toothlessData data for Toothless
     * @param shrekData     data for Shrek
     * @param pussData      data for Puss
     * @param parentalInfo  boolean array representing parental control settings
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        System.out.println("onUpdate called");
        displayPetData();
        if (parentalInfo != null) {
            mathButton.setDisable(!parentalInfo[0]);
            englishButton.setDisable(!parentalInfo[1]);
            geographyButton.setDisable(!parentalInfo[2]);
        }
    }

    /**
     * Initializes the controller. Sets up the shop button icon, reads parental control settings,
     * and adds this controller as a listener to the `UpdateService`.
     */
    @FXML
    public void initialize() {
        // Create an ImageView for the shop icon
        ImageView shopIcon = new ImageView(new Image(getClass().getResourceAsStream("/Scenes/images/storeIcon.png")));
        shopIcon.setFitWidth(32);  // Set icon width
        shopIcon.setFitHeight(32); // Set icon height
        shopButton.setGraphic(shopIcon);
        shopButton.setContentDisplay(ContentDisplay.TOP);

        // Load parental info and initialize buttons
        CSVController.readParentalInfo();
        petManager = PetManager.getInstance();

        boolean[] parentalInfo = CSVDataStore.getInstance().getParentalInfo();
        mathButton.setDisable(!parentalInfo[0]);
        englishButton.setDisable(!parentalInfo[1]);
        geographyButton.setDisable(!parentalInfo[2]);

        // Add this controller as a listener
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
     * Starts the math minigame by navigating to the MinigameQuestion screen.
     */
    @FXML
    private void mathQuestion() {
        navigateToMinigame(0); // Category 0 is math
    }

    /**
     * Starts the English minigame by navigating to the MinigameQuestion screen.
     */
    @FXML
    private void englishQuestion() {
        navigateToMinigame(1); // Category 1 is English
    }

    /**
     * Starts the geography minigame by navigating to the MinigameQuestion screen.
     */
    @FXML
    private void geographyQuestion() {
        navigateToMinigame(2); // Category 2 is geography
    }

    /**
     * Navigates to the MinigameQuestion screen for a specific category.
     *
     * @param category the category of the minigame (0 for math, 1 for English, 2 for geography)
     */
    private void navigateToMinigame(int category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MinigameQuestion.fxml"));
            Parent newGameRoot = loader.load();

            Scene newGameScene = new Scene(newGameRoot);
            newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

            MinigameQuestionController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setPet(pet);
            controller.setCategory(category);
            controller.loadQuestion();

            primaryStage.setScene(newGameScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}