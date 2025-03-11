// for testing (controller)

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    System.out.println();
    System.out.println();

    ParseDatabase.removeClothingItem(2);
    ParseDatabase.printItems();

     */

    // test levenstein algorithm
    System.out.println("levenstein distance: " +
                       ParseDatabase.levenstein("apple", "apple"));
  }

  // deletes the database file!!
  // don't use this for functionality!!
  private static void deleteDatabaseFile() {
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
