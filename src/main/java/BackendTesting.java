// for testing (controller)

import java.util.HashMap;

public class BackendTesting {
  public static void main(String[] args) {
    ParseDatabase.initializeDatabase();
    ParseDatabase.addGucciDress();
    ParseDatabase.printItems();
    /*
    String id1 = String.valueOf(module.generateID());
    String id2 = String.valueOf(module.generateID());
    String[] person1 = {id1, "bob", "joe", "test@gmail.com", "2021-05-01"};
    String[] person2 = {id2, "sam", "joe", "test33@gmail.com", "2021-05-02"};
    module.addEntry(person1);
    module.addEntry(person2);

    module.printDataArray();
    System.out.println();
    module.printDataSQL();
    System.out.println("---------");

    HashMap<String, String> entry = new HashMap<>();
    entry.put("id", "123828");
    entry.put("first_name", "adrian");
    entry.put("last_name", "ramirez");
    entry.put("email", "emailhere@ieIIEj");
    entry.put("hire_date", "thedatehere");
    module.setEntry(1, entry);

    module.printDataArray();
    System.out.println();
    module.printDataSQL();
    System.out.println("---------");

    module.setElement(0, "last_name", "BOBBY");

    module.printDataArray();
    System.out.println();
    module.printDataSQL();
    System.out.println("---------");

     */
  }
}
