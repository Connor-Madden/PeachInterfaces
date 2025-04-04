import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for the UserDatabase class. These tests focus on ensuring the proper
 * functionality of methods related to database clearing, JSON to hashmap conversion,
 * and hashmap to JSON conversion.
 */
public class UserDatabaseTest {

    private UserDatabase db;

    /**
     * Sets up the test environment by initializing a new UserDatabase instance
     * and clearing any existing data in the database.
     */
    @Before
    public void setUp() {
        db = new UserDatabase();
        db.clearDatabase();
    }

    /**
     * Test if the clearDatabase() method successfully removes all users and their
     * favourite items from the database. After clearing, the database should be empty.
     */
    @Test
    public void testClearDatabase() {
        // Add some favourite items for testing
        db.addFavourite("user1", 100);
        db.addFavourite("user2", 200);

        // Clear the database
        db.clearDatabase();

        // Convert the database to a hashmap and check if it is empty
        Map<String, java.util.List<Integer>> map = db.jsonToHashmap();
        assertTrue("Expected the database to be empty", map.isEmpty());
    }

    /**
     * Test the interaction between the jsonToHashmap and hashmapToJson methods.
     * This test ensures that data can be correctly read from JSON (jsonToHashmap),
     * modified (adding a favourite), and written back to JSON (hashmapToJson).
     */
    @Test
    public void testJsonToHashmapAndHashmapToJson() {
        // Add a favourite item for a user
        db.addFavourite("reader", 777);

        // Read the database into a hashmap
        Map<String, java.util.List<Integer>> map = db.jsonToHashmap();

        // Assert that the map is not null and contains the expected user and favourite item
        assertNotNull("Map should not be null after read", map);
        assertTrue("Map should contain the user 'reader'", map.containsKey("reader"));
        assertTrue("User 'reader' should have 777 in their favourites", map.get("reader").contains(777));
    }
}
