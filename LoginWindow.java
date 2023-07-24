import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginWindow extends JFrame {
    public LoginWindow() {
        setTitle("Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JLabel heading = new JLabel("LOGIN");
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JButton btnLogin = new JButton("Login");
        JLabel registerLink = new JLabel("<html><u>Register Now</u></html>");
        
        // Set the link appearance
        registerLink.setForeground(Color.BLUE);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(registerLink, gbc);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputUsername = username.getText();
                String inputPassword = new String(password.getPassword());
        
                if (ValidationManager.validateLogin(inputUsername, inputPassword)) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login successful!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Invalid username or password.");
                }
            }
        });
        

        // Handle the click event for the register link
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showRegistrationWindow();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerLink.setText("<html><u><font color='blue'>Register Now</font></u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerLink.setText("<html><u>Register Now</u></html>");
            }
        });
    }

    private void showRegistrationWindow() {
        this.dispose();
        new RegisterWindow().setVisible(true);
    }
}
