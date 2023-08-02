import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewEventWindow extends JFrame {
    private UserInfo userInfo;
    
    public NewEventWindow(UserInfo userInfo) {
        this.userInfo = userInfo;
        setTitle("New Event");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the navigation bar (sidebar)
        JPanel sidebar = createSidebar();

        // Create the "New Event" form
        JPanel newEventForm = createNewEventForm();

        // Create a heading panel for "New Event"
        JPanel headingPanel = new JPanel();
        headingPanel.setBackground(new Color(240, 240, 240));
        JLabel headingLabel = new JLabel("New Event");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingPanel.add(headingLabel);

        // Create an empty space panel below the form
        JPanel emptySpacePanel = new JPanel();
        emptySpacePanel.setBackground(new Color(240, 240, 240));

        // Create a vertical box layout to stack the components
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(20)); // Add some vertical spacing at the top
        mainBox.add(headingPanel);
        mainBox.add(Box.createVerticalStrut(20)); // Add some vertical spacing below the heading
        mainBox.add(newEventForm);
        mainBox.add(Box.createVerticalStrut(200)); // Increase the vertical space below the form
        mainBox.add(emptySpacePanel);

        // Add components to the main frame
        add(sidebar, BorderLayout.WEST);
        add(mainBox, BorderLayout.CENTER); // Use the mainBox instead of newEventForm directly
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(240, 240, 240)); // Light gray background
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Use Y_AXIS alignment

        JLabel heading = new JLabel("Tech-GO");
        heading.setFont(new Font("Arial", Font.BOLD, 40));
        heading.setForeground(Color.BLACK); // Black text color
        heading.setHorizontalAlignment(JLabel.CENTER);
        sidebar.add(heading);

        JButton homeBtn = createButton("Home");
        JButton newEventBtn = createButton("New Event"); // Add "New Event" button
        JButton logoutBtn = createButton("Log Out");

        // Add empty panel to create space below logout button
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        // Set the maximum width of each button to the width of the sidebar
        Dimension maxButtonSize = new Dimension(sidebar.getPreferredSize().width, 50);
        homeBtn.setMaximumSize(maxButtonSize);
        newEventBtn.setMaximumSize(maxButtonSize); // Set maximum size for "New Event" button
        logoutBtn.setMaximumSize(maxButtonSize);

        // Add action listener for "Home" button
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new AdminEventWindow(userInfo).setVisible(true); // Pass userInfo to the AdminEventWindow
            }
        });


        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginWindow().setVisible(true);
            }
        });

        sidebar.add(homeBtn);
        sidebar.add(newEventBtn); // Add "New Event" button to the sidebar
        sidebar.add(logoutBtn);
        sidebar.add(emptyPanel);

        return sidebar;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 100, 171)); // Purple background
        button.setForeground(Color.WHITE); // White text color
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Added padding to the button
        return button;
    }

    private JPanel createNewEventForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel eventNameLabel = new JLabel("Event Name:");
        JTextField eventNameField = new JTextField();
    
        JLabel eventDateLabel = new JLabel("Event Date (DD/MM/YYYY):");
        JTextField eventDateField = new JTextField();
    
        JLabel eventVenueLabel = new JLabel("Event Venue:");
        JTextField eventVenueField = new JTextField();
    
        JLabel eventDescriptionLabel = new JLabel("Event Description:");
        JTextArea eventDescriptionTextArea = new JTextArea();
    
        JButton createEventButton = new JButton("Create Event");
        createEventButton.setFont(new Font("Arial", Font.BOLD, 20));
        createEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve input values from the text fields
                String eventName = eventNameField.getText();
                String eventVenue = eventVenueField.getText();
                String eventDescription = eventDescriptionTextArea.getText();
                
                String eventDateInput = eventDateField.getText();
                String[] dateParts = eventDateInput.split("/");
                String eventDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
    
                // Insert the new event into the event table and get the generated e_id
                int eventId = DBManager.insertEvent(eventName, eventDate, eventVenue, eventDescription);
    
                // Check if the event was inserted successfully (eventId > 0)
                if (eventId > 0) {
                    // Create a column with e_id in the registered table
                    DBManager.createRegisteredColumn(eventId);
    
                    // Create a column with e_id in the feedback table
                    DBManager.createFeedbackColumn(eventId);
    
                    // Optionally, you can show a success message or close the window here
                    JOptionPane.showMessageDialog(NewEventWindow.this, "Event created successfully!");
                    dispose(); // Close the current window
                    new AdminEventWindow(userInfo).setVisible(true); // Pass userInfo to the AdminEventWindow
                } else {
                    JOptionPane.showMessageDialog(NewEventWindow.this, "Error occurred while creating the event.");
                }
            }
        });
    
        formPanel.add(eventNameLabel);
        formPanel.add(eventNameField);
        formPanel.add(eventDateLabel);
        formPanel.add(eventDateField);
        formPanel.add(eventVenueLabel);
        formPanel.add(eventVenueField);
        formPanel.add(eventDescriptionLabel);
        formPanel.add(new JScrollPane(eventDescriptionTextArea)); // Use a JScrollPane for multi-line description
        formPanel.add(new JLabel()); // Empty label to fill the grid space
        formPanel.add(createEventButton);
    
        return formPanel;
    }
    
    
}
