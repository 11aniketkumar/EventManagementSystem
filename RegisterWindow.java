import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends JFrame {
    private JTextField usnField, nameField, usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton btnRegister;

    public RegisterWindow() {
        setTitle("Register");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JLabel heading = new JLabel("REGISTER");
        usnField = new JTextField(20);
        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        roleComboBox = new JComboBox<>(new String[]{"Student", "Coordinator", "Faculty"});

        JLabel goBackLink = new JLabel("<html><u>Go Back to Login</u></html>");
        goBackLink.setForeground(Color.BLUE);
        goBackLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegister = new JButton("Register");

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
        add(new JLabel("USN:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(goBackLink, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5); // Increased space before btnRegister
        add(btnRegister, gbc);

        // Register ActionListener for the Register button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usn = usnField.getText();
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String role = roleComboBox.getSelectedItem().toString();
                int accessLevel = getAccessLevelFromRole(role);

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Passwords do not match!");
                    return;
                }

                // Call the insertUser method to save the user details into the database
                boolean isUserInserted = DBManager.insertUser(usn, name, username, password, accessLevel);

                if (isUserInserted) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "User registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Error occurred while registering the user.");
                }
            }
        });

        // Register action listener for the "Go Back to Login" link
        goBackLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goBackToLogin();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                goBackLink.setText("<html><u><font color='blue'>Go Back to Login</font></u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                goBackLink.setText("<html><u>Go Back to Login</u></html>");
            }
        });
    }

    // Map user role to access level (modify this according to your requirements)
    private int getAccessLevelFromRole(String role) {
        switch (role) {
            case "Student":
                return 1;
            case "Coordinator":
                return 2;
            case "Faculty":
                return 3;
            default:
                return 0;
        }
    }

    private void goBackToLogin() {
        // Close the current register window and show the login window
        this.dispose();
        new LoginWindow().setVisible(true);
    }
}



