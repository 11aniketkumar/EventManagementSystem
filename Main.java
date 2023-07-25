import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // new LoginWindow().setVisible(true);
                // new RegisterWindow().setVisible(true);
                new EventWindow().setVisible(true);
            }
        });
    }
}

// insert into user(usn, name, username, password, access_level)
 //   -> values('1va21ci005','Aniket','aniket','12345678',1);