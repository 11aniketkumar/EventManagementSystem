import java.sql.*;

public class DBManager {
    // Other methods for database operations can be added here as well

    public static boolean loginDB(String username, String password) {
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;

        } catch (SQLException e) {
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
        }
        return false;
    }
}
