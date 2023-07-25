import java.util.Date;

public class EventObj {
    private int eventId;
    private String eventName;
    private Date eventDate;
    private String eventVenue;
    private String eventDescription;
    private int numRegistrations;

    public EventObj(int eventId, String eventName, Date eventDate, String eventVenue, String eventDescription, int numRegistrations) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventDescription = eventDescription;
        this.numRegistrations = numRegistrations;
    }

    // Add getters and setters for each field
    // For brevity, I'm not including them in this example.
}
