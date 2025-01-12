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

import java.util.*;

/**
 * The `MinigameQuestionController` class manages the user interface and logic for
 * presenting and validating questions in the minigame. It supports different
 * question categories (Math, English, Geography) and adapts to parental control
 * settings to filter questions by difficulty level.
 *
 * @author      Naween Sarwari <nsarwari@uwo.ca>
 * @version     1.9
 * @since       1.4
 */
public class MinigameQuestionController implements UpdateListener {

    /** The primary stage of the application, used for screen transitions. */
    private Stage primaryStage;

    /** The currently selected pet ID. */
    private int pet;

    /** Manager for handling pet-related functionality, such as setting images. */
    private PetManager petManager;

    /** The category of the question (0 for Math, 1 for English, 2 for Geography). */
    private int category;

    /** The currently selected question data. */
    private String[] randomQuestion;

    /** Parental control settings for allowed difficulty levels. */
    private boolean true2, true4, true6;

    @FXML
    private ImageView petImage;

    @FXML
    private ProgressBar hungerBar, sleepBar, healthBar, happinessBar, experienceBar;

    @FXML
    private Label levelLabel, coinsLabel, questionLabel;

    @FXML
    private Button shopButton, option1, option2, option3;

    /** Correct and incorrect answer positions for displaying options randomly. */
    private int correctAnswerPoition, wrongAnswerPoition1, wrongAnswerPoition2;

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the selected pet ID and updates the displayed image and stats.
     *
     * @param pet the ID of the selected pet (1 for Shrek, 2 for Toothless, 3 for Puss)
     */
    public void setPet(int pet) {
        this.pet = pet;
        petManager.setPetImage(pet, petImage);
        displayPetData();
    }

    /**
     * Sets the category for the question.
     *
     * @param category the category ID (0 for Math, 1 for English, 2 for Geography)
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Displays the current stats of the selected pet, including hunger, sleep, health,
     * happiness, experience, level, and coins.
     */
    private void displayPetData(){
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
     * Updates the parental control settings to filter questions by difficulty level.
     */
    private void updateParentalInfo(){
        boolean[] parentalInfo = CSVDataStore.getInstance().getParentalInfo();
        if (parentalInfo != null) {
            true2 = parentalInfo[3];
            true4 = parentalInfo[4];
            true6 = parentalInfo[5];
        }
    }
    /**
     * Called when the pet stats or parental info are updated. Refreshes the pet stats
     * and parental control settings.
     *
     * @param toothlessData data for Toothless
     * @param shrekData     data for Shrek
     * @param pussData      data for Puss
     * @param parentalInfo  boolean array representing parental control settings
     */
    @Override
    public void onUpdate(String[] toothlessData, String[] shrekData, String[] pussData, boolean[] parentalInfo) {
        System.out.println("onUpdate called in MinigameQuestionController");
        displayPetData();
        updateParentalInfo();
    }
    /**
     * Initializes the controller, including setting up the shop button, loading
     * parental info, and assigning actions to the answer buttons.
     */
    @FXML
    public void initialize() {

        // Create an ImageView for the icon
        ImageView shopIcon = new ImageView(new Image(getClass().getResourceAsStream("/Scenes/images/storeIcon.png")));
        shopIcon.setFitWidth(32);  // Set icon width
        shopIcon.setFitHeight(32); // Set icon height
        shopButton.setGraphic(shopIcon);
        shopButton.setContentDisplay(ContentDisplay.TOP);
        CSVController.readMinigameInfo();
        CSVController.readParentalInfo();
        petManager = PetManager.getInstance();

        UpdateService.getInstance().addListener(this);

        // Add event handlers to buttons
        option1.setOnAction(event -> handleAnswer(option1.getText()));
        option2.setOnAction(event -> handleAnswer(option2.getText()));
        option3.setOnAction(event -> handleAnswer(option3.getText()));

        questionLabel.setWrapText(true);

        updateParentalInfo();
    }

    /**
     * Navigates back to the Minigame scene.
     *
     * <p>This method transitions the application to the Minigame screen by loading
     * the corresponding FXML file. It sets the primary stage and pet ID for the MinigameController.
     * If an exception occurs during the scene loading process, it will be printed to the console.
     *
     * @FXML This method is invoked via an associated UI control.
     */
    @FXML
    private void back() {
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
    /**
     * Navigates to the Shop scene.
     *
     * <p>This method transitions the application to the Shop screen by loading
     * the corresponding FXML file. It sets the primary stage and pet ID for the ShopController.
     * If an exception occurs during the scene loading process, it will be printed to the console.
     *
     * @FXML This method is invoked via an associated UI control.
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
     * Loads a random question based on the selected category.
     */
    public void loadQuestion() {
        switch (category) {
            case 0:
                loadMathQuestion();
                break;
            case 1:
                loadEnglishQuestion();
                break;
            case 2:
                loadGeographyQuestion();
                break;
        }
    }
    /**
     * Loads a random math question, filtered by parental control settings.
     */
    private void loadMathQuestion() {
        Map<String, List<String>> minigameData = CSVDataStore.getInstance().getMinigameData();
        List<String[]> mathQuestionsBucket = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : minigameData.entrySet()) {
            String questionType = entry.getKey();
            List<String> data = entry.getValue();

            if (questionType.contains("Math")) {
                String difficulty = data.get(0);

                System.out.println(difficulty);
                System.out.println(true2);
                if (true2 && "2".equals(difficulty)) {
                    mathQuestionsBucket.add(data.toArray(new String[0]));
                }
                else if (true4 && "4".equals(difficulty)) {
                    mathQuestionsBucket.add(data.toArray(new String[0]));
                }
                else if (true6 && "6".equals(difficulty)) {
                    mathQuestionsBucket.add(data.toArray(new String[0]));
                }
            }
        }

        if (!mathQuestionsBucket.isEmpty()) {
            Random random = new Random();
            randomQuestion = mathQuestionsBucket.get(random.nextInt(mathQuestionsBucket.size()));
        }

        displayQuestion();
    }
    /**
     * Loads a random English question, filtered by parental control settings.
     */
    private void loadEnglishQuestion() {
        Map<String, List<String>> minigameData = CSVDataStore.getInstance().getMinigameData();
        List<String[]> englishQuestionsBucket = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : minigameData.entrySet()) {
            String questionType = entry.getKey();
            List<String> data = entry.getValue();

            if (questionType.contains("English")) {
                String difficulty = data.get(0);

                if (true2 && "2".equals(difficulty)) {
                    englishQuestionsBucket.add(data.toArray(new String[0]));
                }
                else if (true4 && "4".equals(difficulty)) {
                    englishQuestionsBucket.add(data.toArray(new String[0]));
                }
                else if (true6 && "6".equals(difficulty)) {
                    englishQuestionsBucket.add(data.toArray(new String[0]));
                }
            }
        }

        if (!englishQuestionsBucket.isEmpty()) {
            Random random = new Random();
            randomQuestion = englishQuestionsBucket.get(random.nextInt(englishQuestionsBucket.size()));



            // Debugging output
            System.out.println("Selected Random English Question:");
            System.out.println(Arrays.toString(randomQuestion));
        }

        displayQuestion();
    }
    /**
     * Loads a random geography question, filtered by parental control settings.
     */
    private void loadGeographyQuestion() {
        Map<String, List<String>> minigameData = CSVDataStore.getInstance().getMinigameData();
        List<String[]> geographyQuestionsBucket = new ArrayList<>();


        for (Map.Entry<String, List<String>> entry : minigameData.entrySet()) {
            String questionType = entry.getKey();
            List<String> data = entry.getValue();

            if (questionType.contains("Geography")) {
                String difficulty = data.get(0);

                if (true2 && "2".equals(difficulty)) {
                    geographyQuestionsBucket.add(data.toArray(new String[0]));
                } else if (true4 && "4".equals(difficulty)) {
                    geographyQuestionsBucket.add(data.toArray(new String[0]));
                } else if (true6 && "6".equals(difficulty)) {
                    geographyQuestionsBucket.add(data.toArray(new String[0]));
                }
            }
        }

        if (!geographyQuestionsBucket.isEmpty()) {
            Random random = new Random();
            randomQuestion = geographyQuestionsBucket.get(random.nextInt(geographyQuestionsBucket.size()));



            // Debugging output
            System.out.println("Selected Random Geography Question:");
            System.out.println(Arrays.toString(randomQuestion));
        }

        displayQuestion();
    }
    /**
     * Displays the current question and its answer options in random order.
     */
    private void displayQuestion() {
        questionLabel.setText(randomQuestion[1]);
        correctAnswerPoition = (int)(Math.random() * 3) + 1;
        if (correctAnswerPoition == 1) {
            wrongAnswerPoition1 = 2;
            wrongAnswerPoition2 = 3;
            option1.setText(randomQuestion[3]);
            option2.setText(randomQuestion[4]);
            option3.setText(randomQuestion[5]);
        }
        else if (correctAnswerPoition == 2) {
            wrongAnswerPoition1 = 1;
            wrongAnswerPoition2 = 3;
            option1.setText(randomQuestion[4]);
            option2.setText(randomQuestion[3]);
            option3.setText(randomQuestion[5]);
        }
        else {
            wrongAnswerPoition1 = 1;
            wrongAnswerPoition2 = 2;
            option1.setText(randomQuestion[4]);
            option2.setText(randomQuestion[5]);
            option3.setText(randomQuestion[3]);
        }
    }
    /**
     * Validates the selected answer and navigates to the appropriate feedback screen.
     *
     * @param selectedAnswer the answer chosen by the user
     */

    private void handleAnswer(String selectedAnswer) {
        // Validate the selected answer
        String correctAnswer = randomQuestion[3];
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
