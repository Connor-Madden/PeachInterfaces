import java.sql.*;
import java.util.*;

public class ParseDatabase {
  private static final String URL = "jdbc:sqlite:fashionDb.db";
  // TODO: change hardcoded values: to use a single String array
  private static final String[] HashmapKeys = {
      "id", "name", "colour", "itemType", "size", "description"};

  public static void initializeDatabase() {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS ClothingItems ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "name TEXT, " // no longer unique: ask Adrian
                            + "colour TEXT, "
                            + "itemType TEXT, "
                            + "size TEXT, "
                            + "description TEXT);";

    String resetAutoIncrementSQL =
        "DELETE FROM sqlite_sequence WHERE name = 'ClothingItems';";
    String checkIfTableIsEmptySQL = "SELECT COUNT(*) FROM ClothingItems;";

    try (Connection connection = DriverManager.getConnection(URL);
         Statement statement = connection.createStatement()) {

      // Create table if it doesn't exist
      statement.executeUpdate(createTableSQL);
      System.out.println("Table created (if it didn't exist).");

      // Check if the table is empty
      ResultSet rs = statement.executeQuery(checkIfTableIsEmptySQL);
      if (rs.next() && rs.getInt(1) == 0) {
        // Reset the autoincrement value if the table is empty
        statement.executeUpdate(resetAutoIncrementSQL);
        System.out.println("Autoincrement reset for empty table.");
      }

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println(
          "Error: a SQLException has occured. (initializeDatabase)");
    }
  }

  public static List<Map<String, Object>> getClothingItems() {
    List<Map<String, Object>> clothingList = new ArrayList<>();
    String selectQuery = "SELECT * FROM ClothingItems;";

    try (Connection connection = DriverManager.getConnection(URL);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(selectQuery)) {

      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();

      while (resultSet.next()) {
        Map<String, Object> rowMap = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
        }
        clothingList.add(rowMap);
      }

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println(
          "Error: a SQLException has occured. (getClothingItems)");
    }
    return clothingList;
  }

  // add a new piece of clothing to the database
  // ADMIN FUNCTION
  public static void addClothingItem(Map<String, String> newClothing) {
    // TODO: validate entered data before adding to database

    String insertSQL = "INSERT INTO ClothingItems (name, colour, itemType, "
                       + "size, description) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

      pstmt.setString(1, newClothing.get("name"));
      pstmt.setString(2, newClothing.get("colour"));
      pstmt.setString(3, newClothing.get("itemType"));
      pstmt.setString(4, newClothing.get("size"));
      pstmt.setString(5, newClothing.get("description"));
      pstmt.executeUpdate();
      System.out.println("Clothing item added successfully!");

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println(
          "Error: a SQLException has occured. (addClothingItem)");
    }
  }

  // edits a new piece of clothing in the database
  // ADMIN FUNCTION
  public static void editClothingItem(int id,
                                      Map<String, String> updatedClothing) {
    String updateSQL = "UPDATE ClothingItems SET name = ?, colour = ?, "
                       + "itemType = ?, size = ?, description = ? WHERE id = ?";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

      pstmt.setString(1, updatedClothing.get("name"));
      pstmt.setString(2, updatedClothing.get("colour"));
      pstmt.setString(3, updatedClothing.get("itemType"));
      pstmt.setString(4, updatedClothing.get("size"));
      pstmt.setString(5, updatedClothing.get("description"));
      pstmt.setInt(6, id);
      int rowsAffected = pstmt.executeUpdate();
      System.out.println(rowsAffected > 0
                             ? "Clothing item updated successfully!"
                             : "No item found with the given ID.");

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println(
          "Error: a SQLException has occured. (editClothingItem)");
    }
  }

  // removes a piece of clothing from the database
  // ADMIN FUNCTION
  public static void removeClothingItem(int id) {
    String checkSQL = "SELECT COUNT(*) FROM ClothingItems WHERE id = ?";
    String deleteSQL = "DELETE FROM ClothingItems WHERE id = ?";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
         PreparedStatement deleteStmt =
             connection.prepareStatement(deleteSQL)) {

      // Check if ID exists
      checkStmt.setInt(1, id);
      ResultSet resultSet = checkStmt.executeQuery();
      if (resultSet.next() && resultSet.getInt(1) > 0) {
        // Delete the item
        deleteStmt.setInt(1, id);
        int rowsAffected = deleteStmt.executeUpdate();
        System.out.println(rowsAffected > 0
                               ? "Clothing item removed successfully."
                               : "Failed to remove item.");

        // After deletion, reset the auto-increment to the highest id
        resetAutoIncrement(connection);
      } else {
        System.out.println("No item found with ID: " + id);
      }
    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println("Error: error removing item");
      System.out.println(
          "Error: a SQLException has occured. (removeClothingItem)");
    }
  }

  private static void resetAutoIncrement(Connection connection) {
    // Query to get the highest current ID value
    String maxIdQuery = "SELECT MAX(id) FROM ClothingItems";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(maxIdQuery)) {

      if (rs.next()) {
        int maxId = rs.getInt(1);
        // Reset the auto-increment value to the max id value
        String resetSQL =
            "UPDATE sqlite_sequence SET seq = ? WHERE name = 'ClothingItems'";
        try (PreparedStatement pstmt = connection.prepareStatement(resetSQL)) {
          pstmt.setInt(1, maxId);
          pstmt.executeUpdate();
          System.out.println("Auto-increment reset to: " + maxId);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error resetting auto-increment.");
    }
  }

  // Levenstein distance algorithm
  // uses Levenstein's algorithm to give a number based on the number of
  // operations needed to change a string into another
  public static int levenstein(String word1, String word2) {
    // create the matrix to fill with number of operation needed to change the
    // substring of the string
    int[][] operations = new int[word1.length() + 1][word2.length() + 1];
    for (int i = 0; i <= word1.length(); i++) {
      for (int j = 0; j <= word2.length(); j++) {
        if (i == 0) {
          operations[i][j] = j; // add the chars from word2
        } else if (j == 0) {
          operations[i][j] = i; // delete the chars from word1
        } else {
          int cost = 1;
          if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
            cost = 0;
          }

          operations[i][j] =
              Math.min(Math.min(operations[i - 1][j] + 1,  // delete
                                operations[i][j - 1] + 1), // insert
                       operations[i - 1][j - 1] + cost);   // replace
        }
      }
    }
    // return the bottom right element which represents the distance for the
    // whole string (the largest substring)
    return operations[word1.length()][word2.length()];
  }

  // filter algorithm
  // for searching:
  //    pass a HashMap <String, Object>
  //    put null/""/" " for a non-entered Object value
  public static List<Map<String, Object>>
  filterItems(Map<String, Object> filters) {
    List<Map<String, Object>> filtered = getClothingItems();

    for (Map<String, Object> item : getClothingItems()) {
      boolean wordMatches = false;
      // compare words with the same key (category/ attribute)
      for (String key : HashmapKeys) {
        // only search non-empty strings and non-nulls
        if (filters.get(key) != null &&
            !filters.get(key).toString().trim().isEmpty()) {

          boolean found = false;
          // for size and id keys it should be exact instead
          if (key.equals("size") || key.equals("id")) {
            if (filters.get(key).toString().toLowerCase().trim().equals(
                    item.get(key).toString().toLowerCase().trim())) {
              found = true;
            }

            // for every other key than size
          } else {
            for (String word2 :
                 filters.get(key).toString().toLowerCase().split(" ")) {
              for (String word1 :
                   item.get(key).toString().toLowerCase().split(" ")) {

                // if there is 1 or less typos add to the filtered list
                if (levenstein(word1.trim(), word2.trim()) <= 1) {
                  found = true;
                  break;
                }
              }
              if (found) {
                break;
              }
            }
          }
          // for each key:
          // if the the words don't match then remove the item
          if (!found) {
            filtered.remove(item);
            break;
          }
        }
      }
    }
    return filtered;
  }

  // Search algorithm
  // for searching:
  //    pass a sentence: "red new dress"
  public static List<Map<String, Object>> searchItems(String sentence) {
    List<Map<String, Object>> filtered = new ArrayList<>();

    for (Map<String, Object> item : getClothingItems()) {
      boolean found = false;
      for (Map.Entry<String, Object> entry : item.entrySet()) {
        // compare every word
        for (String word1 : sentence.toLowerCase().split(" ")) {
          for (String word2 :
               entry.getValue().toString().toLowerCase().split(" ")) {
            // if there is 1 or less typos add to the filtered list
            if (levenstein(word1.trim(), word2.trim()) <= 1) {
              filtered.add(item);
              found = true;
              break;
            }
          }
          if (found) {
            break;
          }
        }
        if (found) {
          break;
        }
      }
    }
    return filtered;
  }

  public static List<Map<String, Object>> searchItemsById(int id) {
    List<Map<String, Object>> results = new ArrayList<>();
    String selectQuery = "SELECT * FROM ClothingItems WHERE id = ?;";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {

      pstmt.setInt(1, id); // Set the ID parameter
      ResultSet resultSet = pstmt.executeQuery();

      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();

      while (resultSet.next()) {
        Map<String, Object> rowMap = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
        }
        results.add(rowMap);
      }

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println("Error: a SQLException has occurred. (searchItemsById)");
    }
    return results;
  }

  // TESTING FUNCTIONS #############################################//

  // this is just for backend testing
  // don't use this for functionality!!
  public static void printItems() {
    List<Map<String, Object>> items = getClothingItems();
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

  // adds a single gucci dress for testing
  // don't use this for functionality!!
  public static void addGucciDress() {
    String checkForDuplicatesSQL =
        "SELECT COUNT(*) FROM ClothingItems WHERE name = ?";
    String insertDataSQL = "INSERT INTO ClothingItems (id, name, colour, "
                           + "itemType, size, description) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL);
         Statement statement = connection.createStatement()) {

      // Check if the Gucci dress exists
      try (PreparedStatement pstmt =
               connection.prepareStatement(checkForDuplicatesSQL)) {
        pstmt.setString(1, "Gucci Denim Mini Dress with Horsebit");
        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next() && resultSet.getInt(1) == 0) {
          // Insert the Gucci dress since it doesn't exist
          try (PreparedStatement insertStmt =
                   connection.prepareStatement(insertDataSQL)) {
            insertStmt.setInt(1, 1); // Assign ID 1 explicitly
            insertStmt.setString(2, "Gucci Denim Mini Dress with Horsebit");
            insertStmt.setString(3, "Blue");
            insertStmt.setString(4, "Dress");
            insertStmt.setString(5, "M");
            insertStmt.setString(
                6, "Denim mini dress with a detachable leather belt "
                       + "featuring silver-toned Horsebit hardware.");
            insertStmt.executeUpdate();
            System.out.println("Sample clothing item inserted with ID: 1");
          }
        }
      }
    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println("Error: a SQLException has occured. (addGucciDress)");
    }
  }
}
