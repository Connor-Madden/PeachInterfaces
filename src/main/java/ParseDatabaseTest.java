import java.io.File;
import java.util.*;

public class ParseDatabaseTest {

  public static void main(String[] args) {

    testLevenstein();
    System.out.println();

    testInitializeDatabase();
    System.out.println();

    ParseDatabase.addGucciDress(); // add the gucci dress for testing

    testGetClothingItems();
    System.out.println();

    testAddClothingItem();
    System.out.println();

    testEditClothingItem();
    System.out.println();

    testRemoveClothingItem();
    System.out.println();

    testFilterItems();
    System.out.println();

    testSearchItems();
    System.out.println();

    testResetAutoIncrement();
    System.out.println();
  }

  private static void testGetClothingItems() {
    List<Map<String, Object>> items = ParseDatabase.getClothingItems();
    isEqual(items.isEmpty(), false, "getClothingItems1 (non-empty list)");
    if (!items.isEmpty()) {
      for (Map<String, Object> item : items) {
        for (Map.Entry<String, Object> entry : item.entrySet()) {
          System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("-------------------");
      }
    } else {
      System.out.println("items not found");
    }
  }

  private static void testAddClothingItem() {
    // add a test item
    Map<String, String> newClothing = new HashMap<>();
    newClothing.put("name", "New Dress");
    newClothing.put("colour", "Red");
    newClothing.put("itemType", "Dress");
    newClothing.put("size", "L");
    newClothing.put("description", "A new dress");
    ParseDatabase.addClothingItem(newClothing);

    // check if the item was added
    boolean found = false;
    for (Map<String, Object> item : ParseDatabase.getClothingItems()) {
      if (item.get("name").equals("New Dress")) {
        found = true;
        break;
      }
    }
    isEqual(found, true, "addClothingItem (item added)");
  }

  // updates the gucci dredd
  private static void testEditClothingItem() {
    Map<String, String> updatedClothing = new HashMap<>();
    updatedClothing.put("name", "Updated Dress");
    updatedClothing.put("colour", "Green");
    updatedClothing.put("itemType", "Dress");
    updatedClothing.put("size", "S");
    updatedClothing.put("description", "An updated dress");
    ParseDatabase.editClothingItem(1, updatedClothing);

    // check if the item was edited
    boolean found = false;
    for (Map<String, Object> item : ParseDatabase.getClothingItems()) {
      if (item.get("name").equals("Updated Dress")) {
        found = true;
        break;
      }
    }
    isEqual(found, true, "editClothingItem (item updated)");
  }

  // remove an item and then verify that its removed
  private static void testRemoveClothingItem() {
    ParseDatabase.removeClothingItem(1);
    boolean found = false;
    for (Map<String, Object> item : ParseDatabase.getClothingItems()) {
      if (item.get("id").equals(1)) {
        found = true;
        break;
      }
    }
    isEqual(found, false, "removeClothingItem (item removed)");
  }

  // check that filter is returns similar items
  private static void testFilterItems() {
    // remake the database file for cleaner testing
    BackendTesting.deleteDatabaseFile();
    ParseDatabase.initializeDatabase();
    ParseDatabase.addGucciDress();
    Map<String, Object> filters = new HashMap<>();
    filters.put("name", "guci");
    filters.put("colour", "bue");
    boolean found = false;
    for (Map<String, Object> item : ParseDatabase.filterItems(filters)) {
      if (item.get("name").equals("Gucci Denim Mini Dress with Horsebit")) {
        found = true;
        break;
      }
    }
    isEqual(found, true, "filterItems (item filtered)");
  }

  // test that search is finding the desired items
  private static void testSearchItems() {
    boolean found = false;
    for (Map<String, Object> item : ParseDatabase.searchItems("xucci ble")) {
      if (item.get("name").equals("Gucci Denim Mini Dress with Horsebit")) {
        found = true;
        break;
      }
    }
    isEqual(found, true, "searchItems (item searched)");
  }

  // tests whether the database initializes
  private static void testInitializeDatabase() {
    // delete the database file and see if its remade
    BackendTesting.deleteDatabaseFile();
    ParseDatabase.initializeDatabase();
    try {
      File file = new File("fashionDb.db");
      if (!file.exists()) {
        throw new Exception("failed to initializeDatabase");
      }
      System.out.println("initializeDatabase = true");
    } catch (Exception error) {
      System.out.println("initializeDatabase = false");
    }
  }

  // test reset auto increment with visual chedck
  private static void testResetAutoIncrement() {
    ParseDatabase.printItems();
    isEqual(ParseDatabase.getClothingItems().get(0).get("id"), 1,
            "resetAutoIncrement (id incremented)");
  }

  // tests the levenstein function
  private static void testLevenstein() {
    isEqual(ParseDatabase.levenstein("test", "test"), 0, "levenstein1");
    isEqual(ParseDatabase.levenstein("oest", "test"), 1, "levenstein2");
    isEqual(ParseDatabase.levenstein("teetin", "test"), 3, "levenstein3");
  }

  // for testing whether a result matches its expected
  private static void isEqual(Object result, Object expected, String testName) {
    System.out.println(testName + " = " + result.equals(expected));
  }
}
