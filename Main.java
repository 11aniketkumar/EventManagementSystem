import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // new LoginWindow().setVisible(true);
                new RegisterWindow().setVisible(true);
            }
        });
    }
}