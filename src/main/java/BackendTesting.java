import java.io.File;
import java.util.*;

public class BackendTesting {
  public static void main(String[] args) {
    BackendTesting.deleteDatabaseFile();
    ParseDatabase.initializeDatabase();
    System.out.println();
    System.out.println();

    ParseDatabase.addGucciDress();
    // ParseDatabase.printItems();
    System.out.println();
    System.out.println();

    Map<String, String> entry = new HashMap<>();
    entry.put("colour", "red");
    entry.put("name", "gucci shorts with leather");
    entry.put("itemType", "shorts");
    entry.put("size", "L");
    entry.put("description", "there gucci shorts!");
    ParseDatabase.addClothingItem(entry);
    ParseDatabase.printItems();
    System.out.println();
    System.out.println();

    /*
    entry.put("colour", "blue");
    entry.put("name", "gucci shorts with leather");
    entry.put("itemType", "shorts");
    entry.put("size", "M"); // change size to medium
    entry.put("description", "there gucci shorts!");
    int id = 2; // 2 with empty database
    ParseDatabase.editClothingItem(id, entry);
    ParseDatabase.printItems();
    Testing.System.out.println();
    Testing.System.out.println();

    ParseDatabase.removeClothingItem(2);
    ParseDatabase.printItems();

     */

    // test levenstein algorithm
    // Testing.System.out.println("levenstein distance: " +
    //                   ParseDatabase.levenstein("apple", "apple"));

    // test search function
    // should show the "red short" and the "blue dress"
    Map<String, Object> filters = new HashMap<>();
    filters.put("name", "gucci");
    filters.put("colour", "blue");
    filters.put("size", "m");
    filters.put("id", 1);

    List<Map<String, Object>> searched = ParseDatabase.filterItems(filters);

    System.out.println("ITEMS FOUND FROM SEARCH:");
    if (!searched.isEmpty()) {
      for (Map<String, Object> item : searched) {
        for (Map.Entry<String, Object> entry2 : item.entrySet()) {
          System.out.println(entry2.getKey() + ": " + entry2.getValue());
        }
        System.out.println("-------------------");
      }
    } else {
      System.out.println("items not found");
    }
  }

  // deletes the database file!!
  // don't use this for functionality!!
  public static void deleteDatabaseFile() {
    File dbFile = new File("fashionDb.db");
    if (dbFile.exists()) {
      if (dbFile.delete()) {
        System.out.println("Database file deleted successfully.");
      } else {
        System.out.println("Failed to delete the database file.");
      }
    } else {
      System.out.println("Database file does not exist.");
    }
  }
}
