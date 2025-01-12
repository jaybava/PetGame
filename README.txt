Short description of the software and what it does
This game offers an engaging and educational experience for children, combining virtual pet care with interactive learning. Players perform tasks like feeding, exercising, grooming, and teaching their virtual pet while exploring subjects such as Math, Spelling, and Geography. Progress is rewarded with experience points and coins, which can be used to unlock better food, cosmetic items, and environment upgrades. Featuring familiar DreamWorks characters, the game provides an immersive, fun, and rewarding way for kids to learn and play.


List of the required libraries and third party tools required to run or build
To run or build this software, the following libraries and tools are required:
1. Java Development Kit (JDK)
   * Version: 11 or higher
   * Required for building and running Java applications.
2. JavaFX
   * Version: Compatible with your JDK (e.g., JavaFX 11 for JDK 11).
   * Used for building the graphical user interface (GUI).
   * Include JavaFX SDK in your project dependencies.
3. FXML (Part of JavaFX)
   * Required for handling the .fxml files used in the application.
4. CSV Handling Library
Detailed step-by-step guide to build/compile from source code.
Step 1: Download and Install the JDK
1. Visit the JDK Download Page:
   * Go to the official Oracle JDK website: https://www.oracle.com/java/technologies/javase-downloads.html
   * Alternatively, you can use OpenJDK from https://openjdk.org.
2. Choose the Correct Version:
   * Download the latest JDK version compatible with your system (e.g., JDK 11 or higher).
3. Download the Installer:
   * Select the installer for your operating system (Windows, macOS, or Linux).
4. Install the JDK:
   * Run the installer and follow the prompts.
   * Note the installation directory (e.g., C:\Program Files\Java\jdk-xx.x.x).
5. Set Up Environment Variables (Windows):
   * Go to "System Properties" > "Environment Variables."
   * Add JAVA_HOME with the path to your JDK installation (e.g., C:\Program Files\Java\jdk-xx.x.x).
   * Append ;%JAVA_HOME%\bin to the Path variable.
6. Verify Installation:
   * Open a terminal or command prompt.
   * Type java -version to confirm the JDK is installed correctly.
Step 2: Download and Set Up JavaFX
1. Visit the JavaFX Download Page:
   * Go to https://openjfx.io.
2. Download the JavaFX SDK:
   * Click "Download" and select the appropriate SDK for your operating system (e.g., Windows, macOS, Linux).
3. Extract the SDK:
   * Extract the downloaded ZIP file to a directory (e.g., C:\javafx-sdk-xx.x.x).
4. Set Up JavaFX Path in Your IDE:
   * If you're using an IDE like IntelliJ IDEA or Eclipse, configure the JavaFX library:
      * Add the JavaFX SDK directory to your project's library path.
      * Set the VM options to include the JavaFX modules:
      * --module-path "C:\javafx-sdk-xx.x.x\lib" --add-modules=javafx.controls,javafx.fxml
Step 3: Verify JavaFX Setup
1. Test a JavaFX Program:
   * Create a simple JavaFX application in your IDE or text editor.
   * Run the program to ensure JavaFX is working properly.
Step 4: Troubleshooting
* If you encounter issues, ensure:
   * The JAVA_HOME environment variable is correctly set.
   * The JavaFX module path is correctly specified in your IDE or build tool configuration.
   * Your JDK version matches the JavaFX SDK version.
Run the Application (PawsScalesAndTales.jar):
*Run it from the command line:
   * Open a terminal or command prompt.
   * Navigate to the directory where the .jar file is saved:
   * Run  java --module-path "path to javafx\openjfx-23.0.1_windows-x64_bin-sdk\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar PawsScalesAndTales.jar
User Guide for PAWS, SCALES and TALES
Welcome to the game! This guide will help you navigate the different options and features available so you can have the best experience possible. Once the game is open, you’ll see three main options:
1. New Game
Start fresh with a new character or adventure.
1. Start a New Adventure: Select this option if you’re just starting out or want to create a new game with a different character.
2. Choose Your Character: Pick your favorite character to begin.
3. Tutorial: After selecting your character, you’ll be guided through a tutorial to learn all the game functions and mechanics.
4. Load Game Screen: Once the tutorial is complete, you’ll land on the load game screen.
   * To return to the home screen from here, simply select Exit.
2. Load Game
Continue your journey where you left off.
1. Select Your Game: Choose the character and game you want to continue.
2. Gameplay Goals:
   * Care for Your Pet: Feed, play with, exercise, and bring your pet to the vet. Neglecting these tasks will lead to penalties in:
      * Health
      * Sleepiness
      * Hunger
      * Happiness levels
   * Minigames: Play exciting minigames to earn coins.
      * Use coins to buy gifts and outfits for your pet, which increase their happiness.
   * Shop: Visit the shop to buy items. Note:
      * You need sufficient coins and experience to purchase certain items.
3. Exit to Home Screen: Return to the home screen by selecting Exit.
3. Parental Controls
Once you’re in youll be prombted to set a password. Once set you will only be able to access the parental controls with this password. 
Customize the game for younger players or use special options.
1. Educational Mini-Games:
   * Adjust the subjects for educational mini-games.
   * Choose the difficulty level of questions.
2. Revive a Pet: Bring a deceased pet back to life with this option.
3. Exit to Home Screen: Return to the home screen by selecting Exit.
Tips for Success
* Balance Your Care: Keep an eye on your pet’s stats and ensure their needs are met.
* Earn and Spend Wisely: Play minigames often to earn coins and plan purchases in the shop strategically.
* Explore: Enjoy the different aspects of the game, from caring for your pet to customizing their appearance and playing educational games.
Happy gaming!