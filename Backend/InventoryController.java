package Backend;

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

import java.util.Arrays;

/**
 * Controller for managing the inventory screen of the pet management application.
 * Handles pet data display, accessory equip functionality, and navigation between screens.
 */
public class InventoryController implements UpdateListener {

    private Stage primaryStage;
    private PetManager petManager;
    private int pet;

    // FXML Elements
    @FXML
    private ImageView petImage;
    @FXML
    private Button shopButton;
    @FXML
    private Button useBlackHat, useBlackGlasses, useBlackBowtie, useSilverHat, useSilverGlasses, useSilverBowtie, useGoldHat, useGoldGlasses, useGoldBowtie;
    @FXML
    private ProgressBar hungerBar, sleepBar, healthBar, happinessBar, experienceBar;
    @FXML
    private Label levelLabel, coinsLabel;

    /**
     * Sets the primary stage for scene transitions.
     *
     * @param primaryStage the main stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the pet for this inventory screen and updates the pet data and accessory buttons.
     *
     * @param pet the ID of the pet to display
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet, petImage);
        displayPetData();
        updateAccessoryButtons();
    }

    /**
     * Initializes the inventory screen by setting up the shop icon and registering the update listener.
     */
    @FXML
    public void initialize() {
        // Set up the shop icon
        ImageView shopIcon = new ImageView(new Image(getClass().getResourceAsStream("/Scenes/images/storeIcon.png")));
        shopIcon.setFitWidth(32);  // Set icon width
        shopIcon.setFitHeight(32); // Set icon height
        shopButton.setGraphic(shopIcon);
        shopButton.setContentDisplay(ContentDisplay.TOP);

        // Get the instance of PetManager and register the update listener
        petManager = PetManager.getInstance();
        UpdateService.getInstance().addListener(this);
    }

    /**
     * Displays the pet's data (hunger, health, happiness, etc.) on the progress bars and labels.
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

            boolean[] accessories = new boolean[9]; // Array to store boolean values for accessories
            for (int i = 0; i < accessories.length; i++) {
                accessories[i] = "TRUE".equalsIgnoreCase(selectedPet[11 + i]);
            }
            System.out.println("Accessories Boolean Array: " + Arrays.toString(accessories));
        }
    }

    /**
     * Updates the accessibility of the accessory buttons based on the pet's owned accessories.
     */
    private void updateAccessoryButtons() {
        boolean[] ownedAccessories = petManager.getOwnedAccessory(pet);

        // Disable buttons for accessories the pet doesn't own
        useBlackGlasses.setDisable(!ownedAccessories[0]);
        useBlackHat.setDisable(!ownedAccessories[1]);
        useBlackBowtie.setDisable(!ownedAccessories[2]);
        useSilverGlasses.setDisable(!ownedAccessories[3]);
        useSilverHat.setDisable(!ownedAccessories[4]);
        useSilverBowtie.setDisable(!ownedAccessories[5]);
        useGoldGlasses.setDisable(!ownedAccessories[6]);
        useGoldHat.setDisable(!ownedAccessories[7]);
        useGoldBowtie.setDisable(!ownedAccessories[8]);
    }

    /**
     * Equips an accessory to the pet and updates its happiness and data.
     *
     * @param accessory the name of the accessory to equip
     */
    private void equipAccessory(String accessory) {
        petManager.equipAccessory(pet, accessory);
        petManager.increaseHappiness(pet, petManager.getHappinessBonus(accessory));
        displayPetData();
    }

    /**
     * Called when the pet data is updated to refresh the screen.
     *
     * @param toothlessData data for Toothless
     * @param shrekData data for Shrek
     * @param pussData data for Puss
     * @param parentalInfo additional information
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        displayPetData();
        updateAccessoryButtons();
        petManager.setPetImage(pet, petImage);
    }

    // Methods for handling accessory button presses

    @FXML
    private void handleBlackHat() {
        equipAccessory("blackHat");
    }

    @FXML
    private void handleBlackGlasses() {
        equipAccessory("blackGlasses");
    }

    @FXML
    private void handleBlackBowtie() {
        equipAccessory("blackBowtie");
    }

    @FXML
    private void handleSilverHat() {
        equipAccessory("silverHat");
    }

    @FXML
    private void handleSilverGlasses() {
        equipAccessory("silverGlasses");
    }

    @FXML
    private void handleSilverBowtie() {
        equipAccessory("silverBowtie");
    }

    @FXML
    private void handleGoldHat() {
        equipAccessory("goldHat");
    }

    @FXML
    private void handleGoldGlasses() {
        equipAccessory("goldGlasses");
    }

    @FXML
    private void handleGoldBowtie() {
        equipAccessory("goldBowtie");
    }

    /**
     * Navigates back to the gameplay screen.
     */
    @FXML
    private void back() {
        try {
            // Load the New Game FXML
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
     * Navigates to the shop screen.
     */
    @FXML
    private void shop() {
        try {
            // Load the Shop FXML
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
}
