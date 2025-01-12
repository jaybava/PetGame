package Backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.util.Arrays;

public class ShopController implements UpdateListener{
    /**
     * Controller for managing the shop interface and interactions with the pet.
     * Handles the display of pet data, the purchase of accessories, and the updating of pet status.
     */
    @FXML
    private ImageView hat1, glasses1, bowtie1, hat2, glasses2, bowtie2, hat3, glasses3, bowtie3, petImage;
    @FXML
    private Button buyItemButton1, buyItemButton2, buyItemButton3, buyItemButton4, buyItemButton5, buyItemButton6, buyItemButton7, buyItemButton8, buyItemButton9;
    @FXML
    private ProgressBar hungerBar, sleepBar, healthBar, happinessBar, experienceBar;
    @FXML
    private Label levelLabel, coinsLabel;

    Stage primaryStage;
    private int pet;
    private PetManager petManager;
    /**
     * Sets the primary stage for the scene.
     *
     * @param primaryStage the primary stage for the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller by loading accessory images and registering for update notifications.
     */

    public void initialize() {

        petManager = PetManager.getInstance();
        loadAccessoryImages();
        UpdateService.getInstance().addListener(this);
    }
    /**
     * Sets the pet to be displayed in the shop.
     *
     * @param pet the ID of the pet to be displayed
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet,petImage);
        displayPetData();
        setPetImageHover();
        disableButtons();
        updateAccesoryButtons();
    }
    /**
     * Displays the data of the selected pet, such as hunger, happiness, health, and level.
     */
    private void displayPetData(){
        String[] selectedPet = petManager.getPetData(pet);
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
     * Loads the images for the available accessories.
     */
    private void loadAccessoryImages(){
        hat1.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/blackHat.png")));
        glasses1.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/blackGlasses.png")));
        bowtie1.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/blackBowtie.png")));
        hat2.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/SilverTophat.png")));
        glasses2.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/SilverSunglasses.png")));
        bowtie2.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/silverBowtie.png")));
        hat3.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/goldTophat.png")));
        glasses3.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/goldSunglasses.png")));
        bowtie3.setImage(new Image(getClass().getResourceAsStream("/Scenes/images/goldBowtie.png")));
    }
    /**
     * Updates the buy buttons based on the accessories the pet has already purchased.
     */
    private void updateAccesoryButtons(){
        boolean[] ownedAccessories = petManager.getOwnedAccessory(pet);
        buyItemButton2.setDisable(ownedAccessories[0]);
        buyItemButton1.setDisable(ownedAccessories[1]);
        buyItemButton3.setDisable(ownedAccessories[2]);
        buyItemButton5.setDisable(ownedAccessories[3]);
        buyItemButton4.setDisable(ownedAccessories[4]);
        buyItemButton6.setDisable(ownedAccessories[5]);
        buyItemButton8.setDisable(ownedAccessories[6]);
        buyItemButton7.setDisable(ownedAccessories[7]);
        buyItemButton9.setDisable(ownedAccessories[8]);
    }


    /**
     * Disables the accessory purchase buttons based on the pet's coins and level.
     */

    private void disableButtons(){
        int coinsValue;
        int levelValue;
        String[] stats;
        stats = CSVDataStore.getInstance().getToothless();

        switch(pet){
            case 2:
                stats = CSVDataStore.getInstance().getToothless();
                break;
            case 1:
                stats = CSVDataStore.getInstance().getShrek();
                break;
            case 3:
                stats = CSVDataStore.getInstance().getPuss();
                break;
        }
        coinsValue = Integer.parseInt(stats[5]);
        levelValue = Integer.parseInt(stats[9]);


        if (coinsValue < 20  || levelValue < 5) {
            buyItemButton1.setDisable(true);
            buyItemButton2.setDisable(true);
            buyItemButton3.setDisable(true);
            buyItemButton4.setDisable(true);
            buyItemButton5.setDisable(true);
            buyItemButton6.setDisable(true);
            buyItemButton7.setDisable(true);
            buyItemButton8.setDisable(true);
            buyItemButton9.setDisable(true);
        }
        else if (coinsValue < 100 || levelValue < 15) {
            buyItemButton4.setDisable(true);
            buyItemButton5.setDisable(true);
            buyItemButton6.setDisable(true);
            buyItemButton7.setDisable(true);
            buyItemButton8.setDisable(true);
            buyItemButton9.setDisable(true);
        }
        else if (coinsValue < 200 || levelValue < 30) {
            buyItemButton7.setDisable(true);
            buyItemButton8.setDisable(true);
            buyItemButton9.setDisable(true);
        }
    }
    /**
     * Event handler for buying the black hat accessory.
     */
    @FXML
    private void buyBlackHat() {
        buyAccessory("blackHat");
    }
    /**
     * Event handler for buying the black glasses accessory.
     */
    @FXML
    private void buyBlackGlasses() {
        buyAccessory("blackGlasses");
    }

    /**
     * Event handler for buying the black bowtie accessory.
     */
    @FXML
    private void buyBlackBowtie() {
        buyAccessory("blackBowtie");
    }
    /**
     * Event handler for buying the silver hat accessory.
     */
    @FXML
    private void buySilverHat() {
        buyAccessory("silverHat");
    }
    /**
     * Event handler for buying the silver glasses accessory.
     */
    @FXML
    private void buySilverGlasses() {
        buyAccessory("silverGlasses");
    }
    /**
     * Event handler for buying the silver bowtie accessory.
     */
    @FXML
    private void buySilverBowtie() {
        buyAccessory("silverBowtie");
    }
    /**
     * Event handler for buying the gold hat accessory.
     */
    @FXML
    private void buyGoldHat() {
        buyAccessory("goldHat");
    }
    /**
     * Event handler for buying the gold glasses accessory.
     */
    @FXML
    private void buyGoldGlasses() {
        buyAccessory("goldGlasses");
    }
    /**
     * Event handler for buying the gold bowtie accessory.
     */
    @FXML
    private void buyGoldBowtie() {
        buyAccessory("goldBowtie");
    }
    /**
     * Attempts to purchase an accessory for the pet. If successful, updates pet data and buttons.
     *
     * @param accessory the name of the accessory to be purchased
     */
    private void buyAccessory(String accessory){
        if(petManager.purchaseAccessory(pet, accessory)){
            displayPetData();
            updateAccesoryButtons();
        } else {
            System.out.println("Not Enough Coins");
        }
    }
    /**
     * Sets up hover events for pet accessories, displaying the accessory on the pet image.
     */
    @FXML
    public void setPetImageHover() {
        String[] petData = petManager.getPetData(pet);


        // Hover events
        hat1.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("blackHat"))));
        glasses1.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("blackGlasses"))));
        bowtie1.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("blackBowtie"))));
        hat2.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("silverHat"))));
        glasses2.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("silverGlasses"))));
        bowtie2.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("silverBowtie"))));
        hat3.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("goldHat"))));
        glasses3.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("goldGlasses"))));
        bowtie3.setOnMouseEntered(event -> petImage.setImage(petManager.getAccessoryImage(petIdToPrefix(pet) + capitalize("goldBowtie"))));

        // Reset on exit
        hat1.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        glasses1.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        bowtie1.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        hat2.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        glasses2.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        bowtie2.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        hat3.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        glasses3.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));
        bowtie3.setOnMouseExited(event -> petManager.setPetImage(pet, petImage));

    }

    private String petIdToPrefix(int petId) {
        return switch (petId){
            case 1 -> "shrek";
            case 2 -> "toothless";
            case 3 -> "pussNBoots";
            default -> null;
        };
    }
    /**
     * Capitalizes the first letter of a string.
     *
     *
     * @return the capitalized string
     */
    private String capitalize(String input){
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    /**
     * Converts pet ID to prefix for accessory file naming.
     *
     *
     * @return the prefix used in accessory names
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        displayPetData();
        updateAccesoryButtons();
    }

    @FXML
    private void back(){
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
}