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
 * The `MinigameIncorrectController` class manages the user interface and logic for
 * handling incorrect answers in the minigame. This class provides functionality
 * for displaying the current pet's stats, offering hints for the question, and
 * transitioning between different game scenes based on the user's actions.
 *
 * @author      Naween Sarwari <nsarwari@uwo.ca>
 * @version     1.9
 * @since       1.4
 */
public class MinigameIncorrectController implements UpdateListener {

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /** The ID of the selected pet. */
    private int pet;

    /** Manager for handling pet-related functionality, such as setting images. */
    private PetManager petManager;

    /** The question data, including options and hints. */
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
    private Label hintLabel;

    @FXML
    private Button shopButton;

    @FXML
    private Button option1;

    @FXML
    private Button option2;

    @FXML
    private Button option3;

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
     * Displays the current stats of the selected pet, including hunger, sleep, health,
     * happiness, experience, level, and coins.
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
     * Updates the displayed pet stats when notified of changes.
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
     * Sets the current question and updates the UI with the hint and answer options.
     *
     * @param randomQuestion the question data, including the hint and possible answers
     */
    public void setRandomQuestion(String[] randomQuestion) {
        this.randomQuestion = randomQuestion;

        hintLabel.setWrapText(true);
        hintLabel.setText(randomQuestion[2]); // Display the hint

        option1.setText(randomQuestion[3]);
        option2.setText(randomQuestion[4]);
        option3.setText(randomQuestion[5]);
    }

    /**
     * Initializes the controller, including setting up the shop button, assigning actions
     * to answer options, and adding this controller as a listener to the `UpdateService`.
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

        // Assign answer handling to buttons
        option1.setOnAction(event -> handleAnswer(option1.getText()));
        option2.setOnAction(event -> handleAnswer(option2.getText()));
        option3.setOnAction(event -> handleAnswer(option3.getText()));

        UpdateService.getInstance().addListener(this);
    }

    /**
     * Navigates to the shop screen.
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
     * Validates the selected answer and navigates to the appropriate screen
     * based on whether the answer is correct or incorrect.
     *
     * @param selectedAnswer the answer chosen by the user
     */
    private void handleAnswer(String selectedAnswer) {
        String correctAnswer = randomQuestion[3]; // Assume the first option is the correct answer
        if (selectedAnswer.equals(correctAnswer)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MinigameCorrectScreen.fxml"));
                Parent newGameRoot = loader.load();

                Scene newGameScene = new Scene(newGameRoot);
                newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                MinigameCorrectController controller = loader.getController();
                controller.setPrimaryStage(primaryStage);
                controller.setPet(pet);
                controller.setRandomQuestion(randomQuestion);

                primaryStage.setScene(newGameScene);
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/MinigameIncorrect.fxml"));
                Parent newGameRoot = loader.load();

                Scene newGameScene = new Scene(newGameRoot);
                newGameScene.getStylesheets().add(getClass().getResource("/Scenes/style.css").toExternalForm());

                MinigameIncorrectController controller = loader.getController();
                controller.setPrimaryStage(primaryStage);
                controller.setPet(pet);
                controller.setRandomQuestion(randomQuestion);

                primaryStage.setScene(newGameScene);
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
