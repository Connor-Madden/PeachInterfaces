import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FrontEndRough {
  public static void main(String[] args) {
    ParseDatabase.initializeDatabase();
    ParseDatabase.addGucciDress(); // NOTE: no longer included in initializeDatabase()
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("\n=== Clothing Catalog Menu ===");
      System.out.println("1. View Clothing Items");
      System.out.println("2. Add Clothing Item");
      System.out.println("3. Edit Clothing Item");
      System.out.println("4. Remove Clothing Item");
      System.out.println("5. Exit");
      System.out.print("Please choose an option: ");

      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
      case 1:
        // View Clothing Items
        List<Map<String, Object>> clothingItems =
            ParseDatabase.getClothingItems();
        if (clothingItems.isEmpty()) {
          System.out.println("No clothing items found.");
        } else {
          // Display items with their actual IDs
          for (Map<String, Object> item : clothingItems) {
            int id = (int)item.get("id");
            String name = (String)item.get("name");
            String color = (String)item.get("colour");
            System.out.println(id + " | " + name + " | " + color);
          }

          // Prompt for ID to view details
          System.out.print("Enter the ID of the item to view details: ");
          int idPicked = scanner.nextInt();
          scanner.nextLine();

          // Find item by ID
          boolean found = false;
          for (Map<String, Object> item : clothingItems) {
            if ((int)item.get("id") == idPicked) {
              System.out.println(item);
              found = true;
              break;
            }
          }
          if (!found) {
            System.out.println("No item found with ID: " + idPicked);
          }
        }
        break;

      case 2:
        // Add Clothing Item
        // FIX: ParseDatabase.addClothingItem(scanner);
        // functionality changed to:
        // addClothingItem(Hashmap<String,String> hmap), without the id parameter
        break;

      case 3:
        // Edit Clothing Item
        List<Map<String, Object>> editItems = ParseDatabase.getClothingItems();
        if (editItems.isEmpty()) {
          System.out.println("No items to edit.");
          break;
        }
        // Display items with IDs
        for (Map<String, Object> item : editItems) {
          System.out.println(item.get("id") + " | " + item.get("name") + " | " +
                             item.get("colour"));
        }
        System.out.print("Enter the ID of the item to edit: ");
        int editId = scanner.nextInt();
        scanner.nextLine();
        // FIX: ParseDatabase.editClothingItem(scanner, editId);
        // functionality changed to:
        // editClothingItem(int id, Hashmap<String,String> hmap)
        break;

      case 4:
        // Remove Clothing Item
        List<Map<String, Object>> removeItems =
            ParseDatabase.getClothingItems();
        if (removeItems.isEmpty()) {
          System.out.println("No items to remove.");
          break;
        }
        // Display items with IDs
        for (Map<String, Object> item : removeItems) {
          System.out.println(item.get("id") + " | " + item.get("name") + " | " +
                             item.get("colour"));
        }
        System.out.print("Enter the ID of the item to remove: ");
        int removeId = scanner.nextInt();
        scanner.nextLine();
        ParseDatabase.removeClothingItem(removeId);
        break;

      case 5:
        System.out.println("Goodbye!");
        scanner.close();
        System.exit(0);
        break;

      default:
        System.out.println("Invalid option.");
      }
    }
  }
}
