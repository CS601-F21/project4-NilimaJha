package model;

import javax.validation.constraints.NotEmpty;

public class Event {
    private int eventId;
    @NotEmpty
    private String eventName;
    @NotEmpty
    private String eventOrganizer;
    @NotEmpty
    private String eventDate;
    @NotEmpty
    private String eventLocation;
    @NotEmpty
    private String eventCategories;
    @NotEmpty
    private String eventDescription;
    @NotEmpty
    private int totalTickets;
    @NotEmpty
    private int ticketsAvailable;
    @NotEmpty
    private String eventStatus;

    private String eventCreatedOn;

    public Event() {
    }

    public Event(String eventName, String eventOrganizer, String eventDate,
                 String eventLocation, String eventCategories, String eventDescription,
                 int totalTickets, int ticketsAvailable, String eventStatus) {
        this.eventName = eventName;
        this.eventOrganizer = eventOrganizer;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventCategories = eventCategories;
        this.eventDescription = eventDescription;
        this.totalTickets = totalTickets;
        this.ticketsAvailable = ticketsAvailable;
        this.eventStatus = eventStatus;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
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

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
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
