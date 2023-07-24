import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Joker@11";

    public static Connection getConnection() {
        try {
            // Register the driver class (optional for newer JDBC drivers)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // create and return connection
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure you have the MySQL connector JAR in the classpath.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
        return null;
    }
}
