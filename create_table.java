import java.sql.Connection;
import java.sql.Statement;

public class create_table {
    public static void main(String[] args) {
        try {
            Connection con = MyConnection.getConnection();
            Statement stmt = con.createStatement();

            // Create user table
            String userTableQuery = "CREATE TABLE user (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "USN varchar(10) NOT NULL," +
                    "name varchar(30) NOT NULL," +
                    "username varchar(30) NOT NULL," +
                    "password varchar(30) NOT NULL," +
                    "access_level int," +
                    "PRIMARY KEY (id)" +
                    ");";
            stmt.executeUpdate(userTableQuery);

            // Create event table
            String eventTableQuery = "CREATE TABLE event (" +
                    "e_id int NOT NULL AUTO_INCREMENT," +
                    "e_name varchar(100) NOT NULL," +
                    "datetime datetime NOT NULL," +
                    "venue varchar(100) NOT NULL," +
                    "image varchar(100)," +
                    "description varchar(500)," +
                    "registered int," +
                    "PRIMARY KEY (e_id)" +
                    ");";
            stmt.executeUpdate(eventTableQuery);

            // Create registered table without event columns
            String registeredTableQuery = "CREATE TABLE registered (" +
                    "user_id int NOT NULL," +
                    "PRIMARY KEY (user_id)," +
                    "FOREIGN KEY (user_id) REFERENCES user(id)" +
                    ");";
            stmt.executeUpdate(registeredTableQuery);

            // Create feedback table without event columns
            String feedbackTableQuery = "CREATE TABLE feedback (" +
                    "user_id int NOT NULL," +
                    "PRIMARY KEY (user_id)," +
                    "FOREIGN KEY (user_id) REFERENCES user(id)" +
                    ");";
            stmt.executeUpdate(feedbackTableQuery);

            con.close();
            System.out.println("Tables created successfully.");
        } catch (Exception e) {
            System.out.println("Error received during table creation");
            // e.printStackTrace();
        }
    }
}
