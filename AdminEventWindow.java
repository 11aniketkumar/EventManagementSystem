import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.sql.SQLException;
import java.util.List;

public class AdminEventWindow extends JFrame {
    private UserInfo userInfo;

    public AdminEventWindow(UserInfo userInfo) {
        this.userInfo = userInfo;
        setTitle("Tech-GO (Admin)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the navigation bar (sidebar)
        JPanel sidebar = createAdminSidebar(userInfo);

        // Create the event sections
        JPanel eventSections = createEventSections();

        // Add eventSections panel to a JScrollPane for scrollability
        JScrollPane scrollPane = new JScrollPane(eventSections);

        // Add components to the main frame
        add(sidebar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createAdminSidebar(UserInfo userInfo) {
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
    
        // Add action listener for "New Event" button
        newEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NewEventWindow(userInfo).setVisible(true);
            }
        });
    
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

    private JPanel createEventSections() {
        JPanel eventSections = new JPanel();
        eventSections.setLayout(new GridLayout(0, 1));
        eventSections.setBackground(new Color(240, 240, 240)); // Light gray background

        // Retrieve all events from the database using DBManager.getAllEvents()
        List<EventObj> eventsList = DBManager.getAllEvents();

        // Add event sections dynamically
        for (EventObj event : eventsList) {
            JPanel eventSection = createEventSection(event);
            eventSections.add(eventSection);
        }

        return eventSections;
    }

    private JPanel createEventSection(EventObj event) {
        JPanel eventSection = new JPanel();
        eventSection.setLayout(new BorderLayout());
        eventSection.setBackground(Color.WHITE);
        eventSection.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 171), 1)); // Purple border

        String eventName = event.getEventName();
        String eventDate = event.getEventDate().toString();
        String eventVenue = event.getEventVenue();
        int numRegistrations = event.getNumRegistrations();
        String eventDetails = event.getEventDescription();

        JLabel eventNameLabel = new JLabel(eventName);
        eventNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        eventNameLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel eventDateLabel = new JLabel("Event Date: " + eventDate);
        eventDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel eventVenueLabel = new JLabel("Event Venue: " + eventVenue);
        eventVenueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel numRegistrationsLabel = new JLabel("No. of Registrations: " + numRegistrations);
        numRegistrationsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextArea eventDetailsTextArea = new JTextArea(eventDetails);
        eventDetailsTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        eventDetailsTextArea.setEditable(false);

        JButton removeBtn = new JButton("Remove");
        removeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        removeBtn.setFocusPainted(false);

        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int eventId = event.getEventId();
                int option = JOptionPane.showConfirmDialog(AdminEventWindow.this, "Are you sure you want to remove this event?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);
    
                if (option == JOptionPane.YES_OPTION) {
                    boolean isRemoved = DBManager.removeEvent(eventId);
    
                    if (isRemoved) {
                        // Remove the event section from the UI
                        eventSection.remove(eventSection);
                        eventSection.revalidate();
                        eventSection.repaint();
                        JOptionPane.showMessageDialog(AdminEventWindow.this, "Event removed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(AdminEventWindow.this, "Error occurred while removing the event.");
                    }
                }
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1));
        infoPanel.add(eventNameLabel);
        infoPanel.add(eventDateLabel);
        infoPanel.add(eventVenueLabel);
        infoPanel.add(numRegistrationsLabel);

        JPanel eventInfoPanel = new JPanel();
        eventInfoPanel.setLayout(new BorderLayout());
        eventInfoPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(removeBtn, BorderLayout.SOUTH);

        eventSection.add(eventInfoPanel, BorderLayout.NORTH);
        eventSection.add(eventDetailsTextArea, BorderLayout.CENTER);
        eventSection.add(bottomPanel, BorderLayout.SOUTH);

        return eventSection;
    }
    
}
