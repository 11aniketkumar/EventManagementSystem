import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// import org.w3c.dom.events.Event;

public class DBManager {

    public static UserInfo loginDB(String username, String password) {
        try (Connection connection = MyConnection.getConnection()) {
            String sqlQuery = "SELECT id, USN, name, username, password, access_level FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String usn = resultSet.getString("USN");
                        String name = resultSet.getString("name");
                        int accessLevel = resultSet.getInt("access_level");
                        return new UserInfo(id, usn, name, username, password, accessLevel);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public static boolean insertUser(String USN, String name, String username, String password, int accessLevel) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO user (USN, name, username, password, access_level) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, USN);
            statement.setString(2, name);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setInt(5, accessLevel);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<EventObj> getAllEvents() {
        List<EventObj> eventsList = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM event";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int eventId = resultSet.getInt("e_id");
                String eventName = resultSet.getString("e_name");
                Date eventDate = resultSet.getDate("datetime");
                String eventVenue = resultSet.getString("venue");
                String eventDescription = resultSet.getString("description");
                int numRegistrations = resultSet.getInt("registered");

                EventObj event = new EventObj(eventId, eventName, eventDate, eventVenue, eventDescription, numRegistrations);
                eventsList.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventsList;
    }

    public static int insertEvent(String eventName, String eventDate, String eventVenue, String eventDescription) {
        try (Connection connection = MyConnection.getConnection()) {
            String insertQuery = "INSERT INTO event (e_name, datetime, venue, description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, eventName);
                statement.setString(2, eventDate);
                statement.setString(3, eventVenue);
                statement.setString(4, eventDescription);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Get the generated e_id for the newly inserted event
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1); // Return the generated e_id
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if the event insertion failed
    }

    public static void createRegisteredColumn(int eventId) {
        try (Connection con = MyConnection.getConnection();
             Statement stmt = con.createStatement();) {
            // Use ALTER TABLE query to add a new column with the name "e_" followed by eventId with INT data type in the 'registered' table
            String query = "ALTER TABLE registered ADD COLUMN `e_" + eventId + "` INT DEFAULT 0";
            stmt.executeUpdate(query);
            System.out.println("Column 'e_" + eventId + "' added to 'registered' table successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred while adding column 'e_" + eventId + "' to 'registered' table.");
            e.printStackTrace();
        }
    }

    public static void createFeedbackColumn(int eventId) {
        try (Connection con = MyConnection.getConnection();
             Statement stmt = con.createStatement();) {
            // Use ALTER TABLE query to add a new column with the name "e_" followed by eventId with VARCHAR data type in the 'feedback' table
            String query = "ALTER TABLE feedback ADD COLUMN `e_" + eventId + "` VARCHAR(100) DEFAULT '0'";
            stmt.executeUpdate(query);
            System.out.println("Column 'e_" + eventId + "' added to 'feedback' table successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred while adding column 'e_" + eventId + "' to 'feedback' table.");
            e.printStackTrace();
        }
    }

}
