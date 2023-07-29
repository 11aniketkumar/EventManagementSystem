import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class MyConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "project";
    private static final String USER = "root";
    private static final String PASSWORD = "Joker@11";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            createDatabaseIfNotExists(con);
            con = DriverManager.getConnection(JDBC_URL + DB_NAME, USER, PASSWORD);
            return con;
        } catch(Exception e) {
            System.out.println("Error received while trying to create a connection");
            // e.printStackTrace();
        }
        return null;
    }

    private static void createDatabaseIfNotExists(Connection con) {
        try {
            Statement stmt = con.createStatement();
            String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(createDBQuery);
            stmt.close();
        } catch(Exception e) {
            System.out.println("Error received while trying to create a connection");
            // e.printStackTrace();
        }
    }
}