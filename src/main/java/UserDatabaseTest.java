import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UserDatabaseTest {

    private UserDatabase db;

    @Before
    public void setUp() {
        db = new UserDatabase();
        db.clearDatabase();
    }

    /**
     * Test if clearDatabase() removes all users and their favourite items.
     */
    @Test
    public void testClearDatabase() {
        db.addFavourite("user1", 100);
        db.addFavourite("user2", 200);

        db.clearDatabase();
        Map<String, java.util.List<Integer>> map = db.jsonToHashmap();
        assertTrue("Expected the database to be empty", map.isEmpty());
    }

    /**
     * Test jsonToHashmap and hashmapToJson interaction (read/write).
     */
    @Test
    public void testJsonToHashmapAndHashmapToJson() {
        db.addFavourite("reader", 777);
        Map<String, java.util.List<Integer>> map = db.jsonToHashmap();

        assertNotNull("Map should not be null after read", map);
        assertTrue("Map should contain the user 'reader'", map.containsKey("reader"));
        assertTrue("User 'reader' should have 777 in their favourites", map.get("reader").contains(777));
    }
}
