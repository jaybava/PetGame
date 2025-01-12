package Backend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author      Jay Bava <jbava@uwo.ca>
 * @version     1.9                 (current version number of program)
 * @since       1.2          (the version of the package this class was first added to)
 */

/**
 * PetManager class manages the pet data and accessories in the game, including
 * retrieving and updating pet attributes such as health, hunger, sleep, and happiness.
 * It also handles the management of pet images and accessories.
 */

public class PetManager {

    /**
     * The singleton instance of the PetManager class.
     * This ensures that only one instance of PetManager is used throughout the application.
     */
    private static PetManager instance;

    /**
     * A cache that stores images of pet accessories and states,
     * allowing for quick retrieval without reloading the images multiple times.
     */
    private Map<String, Image> imageCache = new HashMap<>();

    /**
     * The ID or identifier of the currently selected pet.
     * This field holds the value that represents the pet being interacted with.
     */
    public String currPet;

    /**
     * Retrieves the data associated with a specific pet based on its ID.
     * @param petID The unique identifier for the pet.
     * @return A String array containing pet data, or null if the pet ID is invalid.
     */
    public String[] getPetData(int petID){
        return switch (petID){
            case 1 -> CSVDataStore.getInstance().getShrek();
            case 2 -> CSVDataStore.getInstance().getToothless();
            case 3 -> CSVDataStore.getInstance().getPuss();
            default -> null;
        };
    }

    /**
     * Retrieves the singleton instance of PetManager.
     * @return The PetManager instance.
     */
    public static PetManager getInstance(){
        if(instance == null){
            instance = new PetManager();
        }
        return instance;
    }

    /**
     * Retrieves the number of coins available for the pet identified by the pet ID.
     * @param petID The unique identifier for the pet.
     * @return The number of coins the pet currently has.
     */
    public int getCoins(int petID){
        String[] petData = getPetData(petID);
        return Integer.parseInt(petData[5]);
    }

    /**
     * Retrieves the level of the pet identified by the pet ID.
     * @param petID The unique identifier for the pet.
     * @return The pet's current level.
     */
    public int getLevel(int petID){
        String[] petData = getPetData(petID);
        return Integer.parseInt(petData[9]);
    }

    /**
     * Attempts to purchase an accessory for a specific pet if the pet has enough coins.
     * @param petID The unique identifier for the pet.
     * @param accessory The name of the accessory to purchase.
     * @return True if the purchase was successful, false if the pet does not have enough coins.
     */
    public boolean purchaseAccessory(int petID, String accessory){
        String[] petData = getPetData(petID);
        int cost = getAccessoryCost(accessory);
        int coins = Integer.parseInt(petData[5]);

        if (coins >= cost){
            petData[5] = String.valueOf(coins - cost);
            setAccessoryOwned(petID, accessory, true);

            // Write the updated data back to the CSV
            switch (petID) {
                case 1 -> CSVDataStore.getInstance().setShrek(petData);
                case 2 -> CSVDataStore.getInstance().setToothless(petData);
                case 3 -> CSVDataStore.getInstance().setPuss(petData);
            }

            CSVController.writePetInfo(petID);
            return true;
        }
        return false;
    }

    /**
     * Equips an accessory for a specific pet.
     * @param petID The unique identifier for the pet.
     * @param accessory The name of the accessory to equip.
     */
    public void equipAccessory(int petID, String accessory){
        String[] petData = getPetData(petID);
        petData[20] = accessory;
        CSVController.writePetInfo(petID);

        UpdateService.getInstance().notifyListeners();
    }

    /**
     * Retrieves an array of booleans indicating which accessories are owned by the pet.
     * @param petID The unique identifier for the pet.
     * @return A boolean array indicating which accessories are owned.
     */
    public boolean[] getOwnedAccessory(int petID){
        String[] petData = getPetData(petID);
        boolean[] accessories = new boolean[9];
        for (int i = 0; i < accessories.length; i++){
            accessories[i] = "TRUE".equalsIgnoreCase(petData[11+i]);
        }
        return accessories;
    }

    /**
     * Retrieves the image associated with a specific accessory.
     * @param accessory The name of the accessory.
     * @return The image corresponding to the accessory, or null if no image is found.
     */
    public Image getAccessoryImage(String accessory){
        return imageCache.get(accessory);
    }

    /**
     * Retrieves the cost of a specific accessory.
     * @param accessory The name of the accessory.
     * @return The cost of the accessory.
     */
    private int getAccessoryCost(String accessory) {
        return switch (accessory) {
            case "blackHat", "blackGlasses", "blackBowtie" -> 20;
            case "silverHat", "silverGlasses", "silverBowtie" -> 100;
            case "goldHat", "goldGlasses", "goldBowtie" -> 200;
            default -> 0;
        };
    }

    /**
     * Sets the ownership status of an accessory for a specific pet.
     * @param petId The unique identifier for the pet.
     * @param accessory The name of the accessory.
     * @param owned A boolean value indicating whether the accessory is owned by the pet (true) or not (false).
     */

    private void setAccessoryOwned(int petId, String accessory, boolean owned) {
        String[] petData = getPetData(petId);
        int index = switch (accessory) {
            case "blackGlasses" -> 11;
            case "blackHat" -> 12;
            case "blackBowtie" -> 13;
            case "silverGlasses" -> 14;
            case "silverHat" -> 15;
            case "silverBowtie" -> 16;
            case "goldGlasses" -> 17;
            case "goldHat" -> 18;
            case "goldBowtie" -> 19;
            default -> -1;
        };
        if (index != -1) petData[index] = owned ? "TRUE" : "FALSE";
    }

    /**
     * Private constructor for the PetManager class.
     * Initializes the PetManager instance and preloads the images into the cache.
     */
    private PetManager() {

        preloadImages();

    }

    /**
     * Preloads the images associated with pets and their accessories into the image cache.
     * This method ensures that all the images for the pets' states and accessories are ready for use.
     */
    public void preloadImages(){
        imageCache.put("shrekIdle", ResourceManager.getImage("/Scenes/images/shrekIdle.png"));
        imageCache.put("toothlessIdle", ResourceManager.getImage("/Scenes/images/toothlessIdle.png"));
        imageCache.put("pussIdle", ResourceManager.getImage("/Scenes/images/pussnbootsIdle.png"));

        // State images
        imageCache.put("shrekDead", ResourceManager.getImage("/Scenes/images/shrekDeathState.png"));
        imageCache.put("toothlessDead", ResourceManager.getImage("/Scenes/images/toothlessDeathState.png"));
        imageCache.put("pussDead", ResourceManager.getImage("/Scenes/images/pussnbootsDeathState.png"));

        imageCache.put("shrekHungry", ResourceManager.getImage("/Scenes/images/shrekHungryState.png"));
        imageCache.put("toothlessHungry", ResourceManager.getImage("/Scenes/images/toothlessHungryState.png"));
        imageCache.put("pussHungry", ResourceManager.getImage("/Scenes/images/pussnbootsHungryState.png"));

        imageCache.put("shrekSleep", ResourceManager.getImage("/Scenes/images/shrekSleepState.png"));
        imageCache.put("toothlessSleep", ResourceManager.getImage("/Scenes/images/toothlessSleepState.png"));
        imageCache.put("pussSleep", ResourceManager.getImage("/Scenes/images/pussnbootsSleepState.png"));

        imageCache.put("shrekAngry", ResourceManager.getImage("/Scenes/images/shrekAngryState.png"));
        imageCache.put("toothlessAngry", ResourceManager.getImage("/Scenes/images/toothlessAngryState.png"));
        imageCache.put("pussAngry", ResourceManager.getImage("/Scenes/images/pussnbootsAngryState.png"));

        // Black accessories
        imageCache.put("shrekBlackBowtie", ResourceManager.getImage("/Scenes/images/shrekBowtie.png"));
        imageCache.put("toothlessBlackBowtie", ResourceManager.getImage("/Scenes/images/toothlessBowtie.png"));
        imageCache.put("pussNBootsBlackBowtie", ResourceManager.getImage("/Scenes/images/pussnbootsBowtie.png"));
        imageCache.put("shrekBlackHat", ResourceManager.getImage("/Scenes/images/shrekHat.png"));
        imageCache.put("toothlessBlackHat", ResourceManager.getImage("/Scenes/images/toothlessHat.png"));
        imageCache.put("pussNBootsBlackHat", ResourceManager.getImage("/Scenes/images/pussnbootsHat.png"));
        imageCache.put("shrekBlackGlasses", ResourceManager.getImage("/Scenes/images/shrekGlasses.png"));
        imageCache.put("toothlessBlackGlasses", ResourceManager.getImage("/Scenes/images/toothlessGlasses.png"));
        imageCache.put("pussNBootsBlackGlasses", ResourceManager.getImage("/Scenes/images/pussnbootsGlasses.png"));

// Silver accessories
        imageCache.put("shrekSilverBowtie", ResourceManager.getImage("/Scenes/images/shrekBowtie2.png"));
        imageCache.put("toothlessSilverBowtie", ResourceManager.getImage("/Scenes/images/toothlessBowtie2.png"));
        imageCache.put("pussNBootsSilverBowtie", ResourceManager.getImage("/Scenes/images/pussnbootsBowtie2.png"));
        imageCache.put("shrekSilverHat", ResourceManager.getImage("/Scenes/images/shrekHat2.png"));
        imageCache.put("toothlessSilverHat", ResourceManager.getImage("/Scenes/images/toothlessHat2.png"));
        imageCache.put("pussNBootsSilverHat", ResourceManager.getImage("/Scenes/images/pussnbootsHat2.png"));
        imageCache.put("shrekSilverGlasses", ResourceManager.getImage("/Scenes/images/shrekGlasses2.png"));
        imageCache.put("toothlessSilverGlasses", ResourceManager.getImage("/Scenes/images/toothlessGlasses2.png"));
        imageCache.put("pussNBootsSilverGlasses", ResourceManager.getImage("/Scenes/images/pussnbootsGlasses2.png"));

// Gold accessories
        imageCache.put("shrekGoldBowtie", ResourceManager.getImage("/Scenes/images/shrekBowtie3.png"));
        imageCache.put("toothlessGoldBowtie", ResourceManager.getImage("/Scenes/images/toothlessBowtie3.png"));
        imageCache.put("pussNBootsGoldBowtie", ResourceManager.getImage("/Scenes/images/pussnbootsBowtie3.png"));
        imageCache.put("shrekGoldHat", ResourceManager.getImage("/Scenes/images/shrekHat3.png"));
        imageCache.put("toothlessGoldHat", ResourceManager.getImage("/Scenes/images/toothlessHat3.png"));
        imageCache.put("pussNBootsGoldHat", ResourceManager.getImage("/Scenes/images/pussnbootsHat3.png"));
        imageCache.put("shrekGoldGlasses", ResourceManager.getImage("/Scenes/images/shrekGlasses3.png"));
        imageCache.put("toothlessGoldGlasses", ResourceManager.getImage("/Scenes/images/toothlessGlasses3.png"));
        imageCache.put("pussNBootsGoldGlasses", ResourceManager.getImage("/Scenes/images/pussnbootsGlasses3.png"));

        imageCache.put("foodTier1", ResourceManager.getImage("/Scenes/images/foodTier1.png"));
        imageCache.put("foodTier2", ResourceManager.getImage("/Scenes/images/foodTier2.png"));
        imageCache.put("foodTier3", ResourceManager.getImage("/Scenes/images/foodTier3.png"));
    }

    /**
     * Sets the appropriate image for a pet based on its ID and equipped accessory.
     * If the pet has an accessory that it owns, the accessory image is set.
     * Otherwise, the pet's idle image is used.
     *
     * @param petId The unique identifier for the pet.
     * @param petImage The ImageView object where the pet's image will be set.
     */
    public void setPetImage(int petId, ImageView petImage) {
       String [] petData = getPetData(petId);
       if (petImage == null) {
           System.err.println("Invalid pet ID:" + petId);
       }

       String equippedAccessory = petData[20];
       boolean isAccessoryOwned = false;

       switch (equippedAccessory){
           case "blackHat" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[12]);
           case "blackGlasses" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[11]);
           case "blackBowtie" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[13]);
           case "silverHat" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[15]);
           case "silverGlasses" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[14]);
           case "silverBowtie" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[16]);
           case "goldHat" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[18]);
           case "goldGlasses" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[17]);
           case "goldBowtie" -> isAccessoryOwned = "TRUE".equalsIgnoreCase(petData[19]);
       }

       if (isAccessoryOwned && equippedAccessory != null) {
           Image accessoryImage = imageCache.get(petIdToPrefix(petId) + capitalize(equippedAccessory));
           if (accessoryImage != null) {
               petImage.setImage(accessoryImage);
               return;
           }
       }
       petImage.setImage(getIdleImage(petId));
    }

    /**
     * Converts a pet's ID into a corresponding prefix used for the pet's image file names.
     *
     * @param petId The unique identifier for the pet.
     * @return A string prefix for the pet's image name (e.g., "shrek", "toothless", "pussNBoots").
     */
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
     * @param input The string to capitalize.
     * @return The input string with the first letter capitalized, or the original string if empty.
     */
    private String capitalize(String input){
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Returns the idle image for a pet based on its ID.
     *
     * @param petId The unique identifier for the pet.
     * @return The idle image for the specified pet.
     */
    public Image getIdleImage(int petId){
        return switch (petId) {
            case 1 -> imageCache.get("shrekIdle");
            case 2 -> imageCache.get("toothlessIdle");
            case 3 -> imageCache.get("pussIdle");
            default -> null;
        };
    }

    /**
     * Retrieves the image associated with a pet's state.
     * The state can be one of the following: "dead", "hungry", "sleep", or "angry".
     * The pet's ID is used to determine which specific pet's state image to return.
     *
     * @param petId The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param state The state of the pet (e.g., "dead", "hungry", "sleep", "angry").
     * @return The image corresponding to the pet's state, or null if no image is found.
     */
    public Image getStateImage(int petId, String state) {
        String key = switch (state.toLowerCase()) {
            case "dead" -> switch (petId) {
                case 1 -> "shrekDead";
                case 2 -> "toothlessDead";
                case 3 -> "pussDead";
                default -> null;
            };
            case "hungry" -> switch (petId) {
                case 1 -> "shrekHungry";
                case 2 -> "toothlessHungry";
                case 3 -> "pussHungry";
                default -> null;
            };
            case "sleep" -> switch (petId) {
                case 1 -> "shrekSleep";
                case 2 -> "toothlessSleep";
                case 3 -> "pussSleep";
                default -> null;
            };
            case "angry" -> switch (petId) {
                case 1 -> "shrekAngry";
                case 2 -> "toothlessAngry";
                case 3 -> "pussAngry";
                default -> null;
            };
            default -> null;
        };
        return key != null ? imageCache.get(key) : null;
    }

    /**
     * Retrieves the image associated with a specific food item.
     *
     * @param foodKey The key representing a food item.
     * @return The image corresponding to the food item, or null if no image is found.
     */
    public Image getFoodImage(String foodKey){
        return imageCache.get(foodKey);
    }

    /**
     * Increases the happiness level of a pet by a specified increment.
     * The happiness is capped at 100.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param increment The amount by which to increase the pet's happiness.
     */
    public void increaseHappiness(int petID, int increment) {
        String[] petData = getPetData(petID);
        int happiness = Integer.parseInt(petData[4]); // Index 4 is happiness
        happiness = Math.min(happiness + increment, 100); // Cap happiness at 100
        petData[4] = String.valueOf(happiness);

        // Save the updated pet data
        switch (petID) {
            case 1 -> CSVDataStore.getInstance().setShrek(petData);
            case 2 -> CSVDataStore.getInstance().setToothless(petData);
            case 3 -> CSVDataStore.getInstance().setPuss(petData);
        }
        CSVController.writePetInfo(petID);
    }

    /**
     * Increases the sleep level of a pet by a specified increment.
     * The sleep value is capped at 100.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param increment The amount by which to increase the pet's sleep value.
     */
    public void increaseSleep(int petID, int increment) {
        String[] petData = getPetData(petID);
        int sleepValue = Integer.parseInt(petData[6]); // Sleep is at index 6
        sleepValue = Math.min(sleepValue + increment, 100); // Cap sleep at 100
        petData[6] = String.valueOf(sleepValue);

        // Save the updated pet data
        switch (petID) {
            case 1 -> CSVDataStore.getInstance().setShrek(petData);
            case 2 -> CSVDataStore.getInstance().setToothless(petData);
            case 3 -> CSVDataStore.getInstance().setPuss(petData);
        }
        CSVController.writePetInfo(petID);
        UpdateService.getInstance().notifyListeners(); // Notify other components of the change
    }

    /**
     * Decreases the sleep level of a pet by a specified decrement.
     * The sleep value is ensured not to go below 0.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param decrement The amount by which to decrease the pet's sleep value.
     */
    public void decreaseSleep(int petID, int decrement) {
        String[] petData = getPetData(petID);
        int sleepValue = Integer.parseInt(petData[6]); // Sleep is at index 6
        sleepValue = Math.max(sleepValue - decrement, 0); // Ensure sleep doesn't go below 0
        petData[6] = String.valueOf(sleepValue);

        // Save the updated pet data
        savePetData(petID, petData);
    }

    /**
     * Decreases the hunger level of a pet by a specified decrement.
     * The hunger value is ensured not to go below 0.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param decrement The amount by which to decrease the pet's hunger value.
     */
    public void decreaseHunger(int petID, int decrement) {
        String[] petData = getPetData(petID);
        int hungerValue = Integer.parseInt(petData[3]); // Hunger is at index 3
        hungerValue = Math.max(hungerValue - decrement, 0); // Ensure hunger doesn't go below 0
        petData[3] = String.valueOf(hungerValue);

        // Save the updated pet data
        savePetData(petID, petData);
    }

    /**
     * Increases the health of a pet by a specified increment.
     * The health is capped at 100.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param increment The amount by which to increase the pet's health value.
     */
    public void increaseHealth(int petID, int increment) {
        String[] petData = getPetData(petID);
        int healthValue = Integer.parseInt(petData[8]); // Health is at index 8
        healthValue = Math.min(healthValue + increment, 100); // Cap health at 100
        petData[8] = String.valueOf(healthValue);

        // Save the updated pet data
        savePetData(petID, petData);
    }

    /**
     * Saves the pet data after modifications to the appropriate storage and notifies other components.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param petData The updated pet data to be saved.
     */
    private void savePetData(int petID, String[] petData) {
        switch (petID) {
            case 1 -> CSVDataStore.getInstance().setShrek(petData);
            case 2 -> CSVDataStore.getInstance().setToothless(petData);
            case 3 -> CSVDataStore.getInstance().setPuss(petData);
        }
        CSVController.writePetInfo(petID);
        UpdateService.getInstance().notifyListeners(); // Notify other components of the change
    }

    /**
     * Retrieves the happiness bonus associated with an accessory.
     *
     * @param accessory The name of the accessory.
     * @return The happiness bonus associated with the accessory (e.g., 15, 25, 30, or 0 if not found).
     */
    public int getHappinessBonus(String accessory) {
        return switch (accessory) {
            case "blackHat", "blackGlasses", "blackBowtie" -> 15;
            case "silverHat", "silverGlasses", "silverBowtie" -> 25;
            case "goldHat", "goldGlasses", "goldBowtie" -> 30;
            default -> 0;
        };
    }

    /**
     * Feeds the pet, increasing its hunger level by a specified food value.
     * The hunger is capped at a maximum of 100.
     *
     * @param petID The unique identifier for the pet (1 = Shrek, 2 = Toothless, 3 = Puss).
     * @param foodValue The value by which to increase the pet's hunger (i.e., the amount of food the pet receives).
     */
    public void feedPet(int petID, int foodValue) {
        String[] petData = getPetData(petID);
        int currentHunger = Integer.parseInt(petData[3]); // Hunger is index 3
        currentHunger = Math.min(currentHunger + foodValue, 100); // Cap hunger at 100
        petData[3] = String.valueOf(currentHunger);

        // Save the updated data
        switch (petID) {
            case 1 -> CSVDataStore.getInstance().setShrek(petData);
            case 2 -> CSVDataStore.getInstance().setToothless(petData);
            case 3 -> CSVDataStore.getInstance().setPuss(petData);
        }
        CSVController.writePetInfo(petID);
        UpdateService.getInstance().notifyListeners(); // Notify other components of the change
    }

    public String getCurrPet(){

        return currPet;
    }
}
