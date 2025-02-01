import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class ParseDatabase {
    private static final String URL = "jdbc:sqlite:mydatabase.db"; // Path to your SQLite database

    // Method to initialize the database (create table and insert data if necessary)
    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "first_name TEXT, "
                + "last_name TEXT, "
                + "email TEXT, "
                + "hire_date TEXT"
                + ");";

        String insertDataSQL1 = "INSERT INTO employees (first_name, last_name, email, hire_date) "
                + "VALUES ('John', 'Doe', 'john.doe@example.com', '2021-05-01');";
        String insertDataSQL2 = "INSERT INTO employees (first_name, last_name, email, hire_date) "
                + "VALUES ('Jane', 'Doe', 'jane.doe@example.com', '2022-06-15');";

        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
            System.out.println("Table created (if it didn't exist).");

            String checkForDuplicatesSQL = "SELECT COUNT(*) FROM employees WHERE id=1";
            ResultSet resultSet = statement.executeQuery(checkForDuplicatesSQL);
            if (resultSet.getInt(1) == 0) {
                statement.executeUpdate(insertDataSQL1);
                System.out.println("John Doe inserted.");
            }

            resultSet = statement.executeQuery("SELECT COUNT(*) FROM employees WHERE id=2");
            if (resultSet.getInt(1) == 0) {
                statement.executeUpdate(insertDataSQL2);
                System.out.println("Jane Doe inserted.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch employees from the database
    public static List<Map<String, Object>> getEmployees() {
        List<Map<String, Object>> employeesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM employees;";

        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    rowMap.put(columnName, columnValue);
                }
                employeesList.add(rowMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    // Method to validate names using regex
    private static boolean isValidName(String name) {
        String nameRegex = "^[A-Za-z][A-Za-z\\s\\-]{1,}$";
        return Pattern.matches(nameRegex, name);
    }

    // Method to add a new employee to the database
    public static void addEmployee(Scanner scanner) {
        String insertSQL = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            String firstName, lastName, email, hireDate;

            do {
                System.out.print("Enter first name: ");
                firstName = scanner.nextLine().trim();
                if (!isValidName(firstName)) {
                    System.out.println("Invalid first name. Please try again.");
                }
            } while (!isValidName(firstName));

            do {
                System.out.print("Enter last name: ");
                lastName = scanner.nextLine().trim();
                if (!isValidName(lastName)) {
                    System.out.println("Invalid last name. Please try again.");
                }
            } while (!isValidName(lastName));

            System.out.print("Enter email: ");
            email = scanner.nextLine();

            System.out.print("Enter hire date (YYYY-MM-DD): ");
            hireDate = scanner.nextLine();

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, hireDate);
            pstmt.executeUpdate();
            System.out.println("Employee added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to edit an employee in the database
    public static void editEmployee(Scanner scanner, int id) {
        System.out.println("Editing employee with ID: " + id);
        scanner.nextLine(); // Consume newline
        String updateSQL = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, hire_date = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            String firstName, lastName, email, hireDate;

            do {
                System.out.print("Enter first name: ");
                firstName = scanner.nextLine().trim();
                if (!isValidName(firstName)) {
                    System.out.println("Invalid first name. Please try again.");
                }
            } while (!isValidName(firstName));

            do {
                System.out.print("Enter last name: ");
                lastName = scanner.nextLine().trim();
                if (!isValidName(lastName)) {
                    System.out.println("Invalid last name. Please try again.");
                }
            } while (!isValidName(lastName));

            System.out.print("Enter email: ");
            email = scanner.nextLine();

            System.out.print("Enter hire date (YYYY-MM-DD): ");
            hireDate = scanner.nextLine();

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, hireDate);
            pstmt.setInt(5, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("No employee found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
