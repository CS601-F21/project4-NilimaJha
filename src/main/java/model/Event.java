package model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private int eventId;
    @NotEmpty
    @NotNull
    private String eventName;
    private String eventOrganizerId;
    private String eventOrganizerName;
    @NotEmpty (message = "Wrong event Date")
    private String eventDate;
    @NotEmpty (message = "Wrong event Location")
    private String eventLocation;
    @NotEmpty (message = "Wrong event Categories")
    private String eventCategories;
    @NotEmpty (message = "Wrong event Description")
    private String eventDescription;
    
    @Digits(integer = 4, fraction = 0 ,message = "wrong total ticket")
    private int totalTickets;
    private int ticketsAvailable;
    private String eventStatus;
    private String eventCreatedOn;

    public Event() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");
        LocalDateTime now = LocalDateTime.now();
        this.eventCreatedOn = dtf.format(now);
        this.eventStatus = "active";
        System.out.println("Date :" + this.eventCreatedOn);
    }

    public Event(int eventId, String eventName, String eventDate, String eventOrganizerName, String eventLocation,
                  String eventCategories, String eventDescription,
                 int totalTickets, int ticketsAvailable, String eventStatus, String eventCreatedOn) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventOrganizerName = eventOrganizerName;
        this.eventLocation = eventLocation;
        this.eventCategories = eventCategories;
        this.eventDescription = eventDescription;
        this.totalTickets = totalTickets;
        this.ticketsAvailable = ticketsAvailable;
        this.eventStatus = eventStatus;
        this.eventCreatedOn = eventCreatedOn;

    }

    public Event(ResultSet rs) throws SQLException {
        if (rs.next()) {
            if (!rs.wasNull()) {
                this.eventId = rs.getInt("event_id");
                System.out.println("eventId :" + eventId);
            }
            if (!rs.wasNull()) {
                this.eventName = rs.getString("event_name");
                System.out.println("eventName :" + eventName);
            }
            if (!rs.wasNull()) {
                this.eventOrganizerId = rs.getString("event_organizer");
                System.out.println("eventOrganizer :" + eventOrganizerId);
            }
            if (!rs.wasNull()) {
                this.eventDate = rs.getString("event_date");
                System.out.println("eventDate :" + eventDate);
            }
            if (!rs.wasNull()) {
                this.eventLocation = rs.getString("event_location");
                System.out.println("eventLocation :" + eventLocation);
            }
            if (!rs.wasNull()) {
                this.eventCategories = rs.getString("event_categories");
                System.out.println("eventCategories :" + eventCategories);
            }
            if (!rs.wasNull()) {
                this.eventDescription = rs.getString("event_description");
                System.out.println("eventDescription :" + eventDescription);
            }
            if (!rs.wasNull()) {
                this.totalTickets = rs.getInt("total_tickets");
                System.out.println("totalTickets :" + totalTickets);
            }
            if (!rs.wasNull()) {
                this.ticketsAvailable = rs.getInt("tickets_available");
                System.out.println("ticketsAvailable :" + ticketsAvailable);
            }
            if (!rs.wasNull()) {
                this.eventStatus = rs.getString("event_status");
                System.out.println("eventStatus :" + eventStatus);
            }
            if (!rs.wasNull()) {
                this.eventCreatedOn = rs.getString("created_on");
                System.out.println("eventCreatedOn :" + eventCreatedOn);
            }
        } else {
            System.out.println("No Event with given EventId");
        }

    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventOrganizerId() {
        return eventOrganizerId;
    }

    public String getEventOrganizerName() {
        return eventOrganizerName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventCategories() {
        return eventCategories;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public String getEventCreatedOn() {
        return eventCreatedOn;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventOrganizerId(String eventOrganizer) {
        this.eventOrganizerId = eventOrganizer;
    }

    public void setEventOrganizerName(String eventOrganizerName) {
        this.eventOrganizerName = eventOrganizerName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventCategories(String eventCategories) {
        this.eventCategories = eventCategories;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public void setEventCreatedOn(String eventCreatedOn) {
        this.eventCreatedOn = eventCreatedOn;
    }
}
