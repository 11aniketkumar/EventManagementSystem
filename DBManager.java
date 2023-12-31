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
             PreparedStatement userStatement = connection.prepareStatement(
                     "INSERT INTO user (USN, name, username, password, access_level) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS);
             PreparedStatement registeredStatement = connection.prepareStatement(
                     "INSERT INTO registered (user_id) VALUES (?)");
             PreparedStatement feedbackStatement = connection.prepareStatement(
                     "INSERT INTO feedback (user_id) VALUES (?)")) {
    
            userStatement.setString(1, USN);
            userStatement.setString(2, name);
            userStatement.setString(3, username);
            userStatement.setString(4, password);
            userStatement.setInt(5, accessLevel);
    
            // Insert the user into the "user" table
            int rowsAffected = userStatement.executeUpdate();
    
            // Check if the user was inserted successfully
            if (rowsAffected > 0) {
                // Retrieve the generated user_id for the new user
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
    
                    // Insert a row into the "registered" table
                    registeredStatement.setInt(1, userId);
                    registeredStatement.executeUpdate();
    
                    // Insert a row into the "feedback" table
                    feedbackStatement.setInt(1, userId);
                    feedbackStatement.executeUpdate();
    
                    return true;
                }
            }
    
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

    public static boolean registerEvent(int eventId, int userId) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE registered SET e_" + eventId + " = 1 WHERE user_id = ?")) {
    
            statement.setInt(1, userId);
    
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                // Update the registered count in the event table
                int currentRegistrationCount = getEventRegistrationCount(eventId);
                try (PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE event SET registered = ? WHERE e_id = ?")) {
    
                    updateStatement.setInt(1, currentRegistrationCount + 1);
                    updateStatement.setInt(2, eventId);
                    updateStatement.executeUpdate();
                }
    
                return true;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public static boolean isUserRegistered(int eventId, int userId) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT e_" + eventId + " FROM registered WHERE user_id = ?")) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int isRegistered = resultSet.getInt(1);
                    return isRegistered == 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int getEventRegistrationCount(int eventId) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT registered FROM event WHERE e_id = ?")) {
    
            statement.setInt(1, eventId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("registered");
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<EventObj> getRegisteredEvents(int userId) {
        List<EventObj> eventsList = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM event WHERE e_id IN (SELECT e_id FROM registered WHERE user_id = ? AND (e_1 = 1 OR e_2 = 1 OR e_3 = 1))")) {
    
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
    
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


    public static boolean unregisterEvent(int eventId, int userId) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE registered SET e_" + eventId + " = 0 WHERE user_id = ?")) {

            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Update the registered count in the event table
                int currentRegistrationCount = getEventRegistrationCount(eventId);
                try (PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE event SET registered = ? WHERE e_id = ?")) {

                    updateStatement.setInt(1, currentRegistrationCount - 1);
                    updateStatement.setInt(2, eventId);
                    updateStatement.executeUpdate();
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeEvent(int eventId) {
        try (Connection connection = MyConnection.getConnection();
             Statement statement = connection.createStatement()) {
    
            // Drop the column from the 'registered' table
            String dropRegisteredColumnSQL = "ALTER TABLE registered DROP COLUMN e_" + eventId;
            statement.executeUpdate(dropRegisteredColumnSQL);
    
            // Drop the column from the 'feedback' table (if it exists)
            String dropFeedbackColumnSQL = "ALTER TABLE feedback DROP COLUMN e_" + eventId;
            statement.executeUpdate(dropFeedbackColumnSQL);
    
            // Delete the event row from the 'event' table
            String deleteEventSQL = "DELETE FROM event WHERE e_id = " + eventId;
            statement.executeUpdate(deleteEventSQL);
    
            return true;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}
