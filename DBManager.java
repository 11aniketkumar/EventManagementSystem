import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.events.Event;

public class DBManager {

    public static boolean loginDB(String username, String password) {
        try (Connection connection = MyConnection.getConnection()) {
            String sqlQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

}
