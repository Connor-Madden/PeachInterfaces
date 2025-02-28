import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class FrontEndRough {
    public static void main(String[] args) {
        // Initialize database (create table and insert default values)
        ParseDatabase.initializeDatabase();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display the menu options
            System.out.println("\n=== Employee Catalog Menu ===");
            System.out.println("1. View Employees");
            System.out.println("2. Add Employee");
            System.out.println("3. Edit Employee");
            System.out.println("4. Remove Employee");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // View Employees
                    List<Map<String, Object>> employees = ParseDatabase.getEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees found.");
                    } else {
                        for (Map<String, Object> employee : employees) {
                            // Extract fields for formatted output
                            int id = (int) employee.get("id");
                            String lastName = (String) employee.get("last_name");
                            String firstName = (String) employee.get("first_name");
                            System.out.println(id + " | " + lastName + ", " + firstName);
                        }

                        System.out.print("Enter the index of the employee you'd like to view the full details of: ");
                        int idPicked = scanner.nextInt()-1;
                        System.out.println(employees.get(idPicked));

                    }
                    break;

                case 2:
                    // Add Employee
                    ParseDatabase.addEmployee(scanner);
                    break;

                case 3:
                    // Exit the program
                    System.out.print("Enter id of employee to edit: ");
                    int idChoice = scanner.nextInt();
                    ParseDatabase.editEmployee(scanner, idChoice);
                    break;
                case 4:
                    ParseDatabase.removeEmployee(scanner);
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
