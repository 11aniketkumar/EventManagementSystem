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

    // Getter methods
    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getNumRegistrations() {
        return numRegistrations;
    }
}
