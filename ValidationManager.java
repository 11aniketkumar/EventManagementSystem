public class ValidationManager {
    public static boolean validateLogin(String username, String password) {
        // Perform basic validation checks here (e.g., password length, format, etc.)
        // If validation passes, call the DBManager method
        return DBManager.loginDB(username, password);
    }
}
