import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.*;

/**
 * Test class for ParseDatabase functionality.
 * This class contains unit tests for various database operations including
 * initialization, CRUD operations, filtering, and searching.
 */
public class ParseDatabaseTest {
  /**
   * Sets up the test environment before each test case.
   * Deletes any existing database file and initializes a new one with sample data.
   */
  @Before
  public void setUp() {
    // Reset the database before each test
    BackendTesting.deleteDatabaseFile();
    ParseDatabase.initializeDatabase();
    ParseDatabase.addGucciDress(); // add the gucci dress for testing
  }

  /**
   * Cleans up after each test case if needed.
   * Currently empty as no specific cleanup is required.
   */
  @After
  public void tearDown() {
    // Clean up after each test if needed
  }

  /**
   * Tests the Levenshtein distance calculation.
   * Verifies that the algorithm correctly calculates the edit distance between strings.
   */
  @Test
  public void testLevenstein() {
    Assert.assertEquals(0, ParseDatabase.levenstein("test", "test"));
    Assert.assertEquals(1, ParseDatabase.levenstein("oest", "test"));
    Assert.assertEquals(3, ParseDatabase.levenstein("teetin", "test"));
  }

  /**
   * Tests database initialization.
   * Verifies that the database file is created during initialization.
   */
  @Test
  public void testInitializeDatabase() {
    File file = new File("fashionDb.db");
    assertTrue("Database file should exist after initialization", file.exists());
  }

  /**
   * Tests retrieval of clothing items from the database.
   * Verifies that the returned list is not empty and optionally prints items for visual verification.
   */
  @Test
  public void testGetClothingItems() {
    List<Map<String, Object>> items = ParseDatabase.getClothingItems();
    assertFalse("Clothing items list should not be empty", items.isEmpty());

    // Optional: Print items for visual verification
    items.forEach(item -> {
      item.forEach((key, value) -> System.out.println(key + ": " + value));
      System.out.println("-------------------");
    });
  }

  /**
   * Tests adding a new clothing item to the database.
   * Verifies that the newly added item can be retrieved from the database.
   */
  @Test
  public void testAddClothingItem() {
    Map<String, String> newClothing = new HashMap<>();
    newClothing.put("name", "New Dress");
    newClothing.put("colour", "Red");
    newClothing.put("itemType", "Dress");
    newClothing.put("size", "L");
    newClothing.put("description", "A new dress");
    ParseDatabase.addClothingItem(newClothing);

    boolean found = ParseDatabase.getClothingItems().stream()
            .anyMatch(item -> item.get("name").equals("New Dress"));
    assertTrue("New item should be found in the database", found);
  }

  /**
   * Tests editing an existing clothing item in the database.
   * Verifies that the updated item information is correctly stored.
   */
  @Test
  public void testEditClothingItem() {
    Map<String, String> updatedClothing = new HashMap<>();
    updatedClothing.put("name", "Updated Dress");
    updatedClothing.put("colour", "Green");
    updatedClothing.put("itemType", "Dress");
    updatedClothing.put("size", "S");
    updatedClothing.put("description", "An updated dress");
    ParseDatabase.editClothingItem(1, updatedClothing);

    boolean found = ParseDatabase.getClothingItems().stream()
            .anyMatch(item -> item.get("name").equals("Updated Dress"));
    assertTrue("Updated item should be found in the database", found);
  }

  /**
   * Tests removal of a clothing item from the database.
   * Verifies that the removed item is no longer present in the database.
   */
  @Test
  public void testRemoveClothingItem() {
    ParseDatabase.removeClothingItem(1);
    boolean found = ParseDatabase.getClothingItems().stream()
            .anyMatch(item -> item.get("id").equals(1));
    assertFalse("Removed item should not be found in the database", found);
  }

  /**
   * Tests filtering of clothing items with approximate matching.
   * Verifies that items can be found despite typos in the filter criteria.
   */
  @Test
  public void testFilterItems() {
    Map<String, Object> filters = new HashMap<>();
    filters.put("name", "guci");
    filters.put("colour", "bue");

    boolean found = ParseDatabase.filterItems(filters).stream()
            .anyMatch(item -> item.get("name").equals("Gucci Denim Mini Dress with Horsebit"));
    assertTrue("Filter should find the Gucci dress despite typos", found);
  }

  /**
   * Tests searching for clothing items with approximate matching.
   * Verifies that items can be found despite typos in the search query.
   */
  @Test
  public void testSearchItems() {
    boolean found = ParseDatabase.searchItems("xucci ble").stream()
            .anyMatch(item -> item.get("name").equals("Gucci Denim Mini Dress with Horsebit"));
    assertTrue("Search should find the Gucci dress despite typos", found);
  }

  /**
   * Tests the auto-increment reset functionality.
   * Verifies that the first item in the database has the correct ID.
   */
  @Test
  public void testResetAutoIncrement() {
    // This test might need adjustment based on your actual resetAutoIncrement implementation
    List<Map<String, Object>> items = ParseDatabase.getClothingItems();
    if (!items.isEmpty()) {
      assertEquals("First item should have ID 1", 1, items.get(0).get("id"));
    }
  }
}
