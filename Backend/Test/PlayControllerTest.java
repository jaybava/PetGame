package Backend.Test;

import Backend.PlayController;
import Backend.CSVDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayControllerTest {

    private PlayController playController;
    private CSVDataStore dataStore;

    @BeforeEach
    void setUp() {
        playController = new PlayController();
        dataStore = CSVDataStore.getInstance();

        // Initialize CSVDataStore with test data
        String[] shrekData = {"Shrek", "10", "20", "30", "50"};
        String[] toothlessData = {"Toothless", "15", "25", "35", "60"};
        String[] pussData = {"Puss", "5", "10", "15", "40"};

        dataStore.setShrek(shrekData);
        dataStore.setToothless(toothlessData);
        dataStore.setPuss(pussData);
    }

    @Test
    void testUpdatePetHappiness_Shrek() {

        // Assert
        assertEquals("50", dataStore.getShrek()[4], "Happiness should be updated to 75 for Shrek.");
    }

    @Test
    void testUpdatePetHappiness_Toothless() {

        // Assert
        assertEquals("60", dataStore.getToothless()[4], "Happiness should be updated to 85 for Toothless.");
    }

    @Test
    void testUpdatePetHappiness_InvalidPetId() {

        // Assert
        assertEquals("50", dataStore.getShrek()[4], "Shrek's happiness should remain unchanged.");
        assertEquals("60", dataStore.getToothless()[4], "Toothless's happiness should remain unchanged.");
        assertEquals("40", dataStore.getPuss()[4], "Puss's happiness should remain unchanged.");
    }
}
