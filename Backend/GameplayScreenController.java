package Backend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author      Sebastien Moroz <smoroz4@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.2          (the version of the package this class was first added to)
 */

/**
 * Controller class for managing the gameplay screen.
 * This controller handles the game's user interface, including pet status updates, button functionalities,
 * keyboard shortcuts, and navigation between different screens.
 */
public class GameplayScreenController implements UpdateListener {

    Stage primaryStage;
    private int pet;
    private PetManager petManager;
    private Instant sessionStartTime;
    private long totalPlayTime;

    @FXML
    private ImageView petImage;

    @FXML
    private Button back, shopButton, minigameButton, inventoryButton, playbutton, sleepbutton, feedbutton, exercisebutton, vetbutton;

    @FXML
    private ProgressBar hungerBar, sleepBar, healthBar, happinessBar, experienceBar;

    @FXML
    private Label levelLabel, coinsLabel;

    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage The main stage to be set.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the selected pet and updates the pet's data and image.
     *
     * @param pet The ID of the selected pet.
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet, petImage);
        displayPetData();
        PeriodicUpdateService.getInstance().setSelectedPetId(pet);
    }

    /**
     * Initializes the controller by setting up the pet manager, adding update listeners,
     * starting periodic updates, setting up button icons, and keyboard shortcuts.
     */
    @FXML
    public void initialize() {
        petManager = PetManager.getInstance();
        UpdateService.getInstance().addListener(this);
        PeriodicUpdateService.getInstance().startUpdating();

        playbutton.setContentDisplay(ContentDisplay.TOP);
        totalPlayTime = CSVDataStore.getInstance().getTotalPlayTime();
        sessionStartTime = Instant.now();
        setupButtonIcons();
        setupKeyboardShortcuts();
    }

    /**
     * Sets the icons for the buttons on the screen.
     */
    private void setupButtonIcons() {
        setupButtonIcon(shopButton, "/Scenes/images/storeIcon.png");
        setupButtonIcon(minigameButton, "/Scenes/images/miniGameIcon.png");
        setupButtonIcon(inventoryButton, "/Scenes/images/inventoryIcon.png");
    }

    /**
     * Sets an individual button's icon.
     *
     * @param button The button to set the icon for.
     * @param iconPath The path to the icon image.
     */
    private void setupButtonIcon(Button button, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        button.setGraphic(icon);
        button.setContentDisplay(ContentDisplay.TOP);
    }

    /**
     * Sets up keyboard shortcuts for game actions.
     * ESC key returns to the main menu, and other keys trigger specific actions (e.g., play, feed, etc.).
     */
    private void setupKeyboardShortcuts() {
        Platform.runLater(() -> {
            petImage.getScene().setOnKeyPressed(event -> {
                KeyCode code = event.getCode();
                if (code == KeyCode.ESCAPE) goToMainMenu();
                else if (code == KeyCode.V) vet();
                else if (code == KeyCode.P) play();
                else if (code == KeyCode.F) feed();
                else if (code == KeyCode.E) exercise();
                else if (code == KeyCode.S) sleep();
            });
        });
    }

    /**
     * Displays the selected pet's data on the screen (e.g., progress bars, level, coins).
     */
    private void displayPetData() {
        String[] selectedPet = petManager.getPetData(pet);

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
     * Handles pet state updates and dynamically disables or enables buttons based on the pet's state.
     *
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        displayPetData();
        String[] petData = petManager.getPetData(pet);
        handleState(petManager.getPetData(pet));

        if (!isStateOverridden(petData)) {
            petManager.setPetImage(pet, petImage);
        }
    }

    /**
     * Handles disabling buttons and setting alert images dynamically based on the pet's state.
     *
     * @param petData The data of the selected pet to determine the pet's state.
     */
    private void handleState(String[] petData) {
        boolean isHungerZero = "0".equals(petData[3]);
        boolean isSleepZero = "0".equals(petData[6]);
        boolean isHealthZero = "0".equals(petData[8]);
        boolean isHappinessZero = "0".equals(petData[4]);

        disableAllButtons();

        if (isHealthZero) {
            petImage.setImage(petManager.getStateImage(pet, "dead"));
        } else if (isSleepZero) {
            petImage.setImage(petManager.getStateImage(pet, "sleep"));
            sleepbutton.setDisable(false);
        } else if (isHungerZero) {
            petImage.setImage(petManager.getStateImage(pet, "hungry"));
            feedbutton.setDisable(false);
        } else if (isHappinessZero) {
            petImage.setImage(petManager.getStateImage(pet, "angry"));
            playbutton.setDisable(false);
            inventoryButton.setDisable(false);
        } else {
            enableAllButtons();
        }
    }

    /**
     * Determines if any pet state (hunger, sleep, health, or happiness) is overridden (i.e., zero).
     *
     * @param petData The data of the selected pet to check.
     * @return true if any state is overridden, false otherwise.
     */
    private boolean isStateOverridden(String[] petData) {
        return "0".equals(petData[3]) || "0".equals(petData[6]) || "0".equals(petData[8]) || "0".equals(petData[4]);
    }

    /**
     * Disables all the action buttons (shop, mini-game, feed, etc.).
     */
    private void disableAllButtons() {
        shopButton.setDisable(true);
        minigameButton.setDisable(true);
        inventoryButton.setDisable(true);
        sleepbutton.setDisable(true);
        exercisebutton.setDisable(true);
        feedbutton.setDisable(true);
        playbutton.setDisable(true);
        vetbutton.setDisable(true);
    }

    /**
     * Enables all the action buttons (shop, mini-game, feed, etc.).
     */
    private void enableAllButtons() {
        shopButton.setDisable(false);
        minigameButton.setDisable(false);
        inventoryButton.setDisable(false);
        sleepbutton.setDisable(false);
        exercisebutton.setDisable(false);
        feedbutton.setDisable(false);
        playbutton.setDisable(false);
        vetbutton.setDisable(false);
    }

    /**
     * Navigates to a different screen (e.g., Shop, Inventory, etc.) based on the provided FXML path.
     * Updates the total playtime and session duration.
     *
     * @param fxmlPath The path to the FXML file of the new screen.
     * @param controller The controller instance for the new screen.
     */
    private void navigateTo(String fxmlPath, Object controller) {
        long sessionPlayTime = Duration.between(sessionStartTime, Instant.now()).getSeconds();
        totalPlayTime += sessionPlayTime;
        CSVController.updateTimePlay(sessionPlayTime);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object loadedController = loader.getController();

            // Set the primary stage and pet for the new screen
            if (loadedController instanceof ShopController shopController) {
                shopController.setPrimaryStage(primaryStage);
                shopController.setPet(pet);
            } else if (loadedController instanceof InventoryController inventoryController) {
                inventoryController.setPrimaryStage(primaryStage);
                inventoryController.setPet(pet);
            } else if (loadedController instanceof MinigameController minigameController) {
                minigameController.setPrimaryStage(primaryStage);
                minigameController.setPet(pet);
            } else if (loadedController instanceof PlayController playController) {
                playController.setPrimaryStage(primaryStage);
                playController.setPet(pet);
            } else if (loadedController instanceof SleepController sleepController) {
                sleepController.setPrimaryStage(primaryStage);
                sleepController.setPet(pet);
            } else if (loadedController instanceof FoodController foodController) {
                foodController.setPrimaryStage(primaryStage);
                foodController.setPet(pet);
            } else if (loadedController instanceof VetController vetController) {
                vetController.setPrimaryStage(primaryStage);
                vetController.setPet(pet);
            } else if (loadedController instanceof ExerciseController exerciseController) {
                exerciseController.setPrimaryStage(primaryStage);
                exerciseController.setPet(pet);
            } else if (loadedController instanceof MainMenuController mainMenuController) {
                mainMenuController.setPrimaryStage(primaryStage);
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the shop screen.
     */
    @FXML
    private void shop() {
        navigateTo("/Scenes/Shop.fxml", new ShopController());
    }

    /**
     * Navigates to the main menu screen.
     */
    @FXML
    private void goToMainMenu() {
        UpdateService.getInstance().removeAllListeners();
        navigateTo("/Scenes/MainMenu.fxml", new MainMenuController());
    }

    /**
     * Navigates to the inventory screen.
     */
    @FXML
    private void inventory() {
        navigateTo("/Scenes/Inventory.fxml", new InventoryController());
    }

    /**
     * Navigates to the mini-game screen.
     */
    @FXML
    private void minigame() {
        navigateTo("/Scenes/Minigame.fxml", new MinigameController());
    }

    /**
     * Navigates to the play screen.
     */
    @FXML
    private void play() {
        navigateTo("/Scenes/Play.fxml", new PlayController());
    }

    /**
     * Navigates to the sleep screen.
     */
    @FXML
    private void sleep() {
        navigateTo("/Scenes/Sleep.fxml", new SleepController());
    }

    /**
     * Navigates to the feed screen.
     */
    @FXML
    private void feed() {
        navigateTo("/Scenes/Feed.fxml", new FoodController());
    }

    /**
     * Navigates to the vet screen.
     */
    @FXML
    private void vet() {
        navigateTo("/Scenes/Vet.fxml", new VetController());
    }

    /**
     * Navigates to the exercise screen.
     */
    @FXML
    private void exercise() {
        navigateTo("/Scenes/Exercise.fxml", new ExerciseController());
    }
}

