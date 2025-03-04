import java.sql.*;
import java.util.*;

public class ParseDatabase {
  private static final String URL = "jdbc:sqlite:fashionDb.db";

  public static void initializeDatabase() {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS ClothingItems ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "name TEXT UNIQUE, "
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

  public static void addClothingItem(Scanner scanner) {
    String insertSQL = "INSERT INTO ClothingItems (name, colour, itemType, "
                       + "size, description) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

      System.out.print("Enter item name: ");
      String name = scanner.nextLine().trim();

      System.out.print("Enter item color: ");
      String color = scanner.nextLine().trim();

      System.out.print("Enter item type: ");
      String type = scanner.nextLine().trim();

      System.out.print("Enter item size: ");
      String size = scanner.nextLine().trim();

      System.out.print("Enter item description: ");
      String description = scanner.nextLine().trim();

      pstmt.setString(1, name);
      pstmt.setString(2, color);
      pstmt.setString(3, type);
      pstmt.setString(4, size);
      pstmt.setString(5, description);
      pstmt.executeUpdate();
      System.out.println("Clothing item added successfully!");

    } catch (SQLException error) {
      error.printStackTrace();
      System.out.println(
          "Error: a SQLException has occured. (addClothingItem)");
    }
  }

  public static void editClothingItem(Scanner scanner, int id) {
    String updateSQL = "UPDATE ClothingItems SET name = ?, colour = ?, "
                       + "itemType = ?, size = ?, description = ? WHERE id = ?";

    try (Connection connection = DriverManager.getConnection(URL);
         PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
      scanner.nextLine();

      System.out.print("Enter new item name: ");
      String name = scanner.nextLine().trim();

      System.out.print("Enter new item color: ");
      String color = scanner.nextLine().trim();

      System.out.print("Enter new item type: ");
      String type = scanner.nextLine().trim();

      System.out.print("Enter new item size: ");
      String size = scanner.nextLine().trim();

      System.out.print("Enter new item description: ");
      String description = scanner.nextLine().trim();

      pstmt.setString(1, name);
      pstmt.setString(2, color);
      pstmt.setString(3, type);
      pstmt.setString(4, size);
      pstmt.setString(5, description);
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
