package model;

import validation.EventDate;
import utils.Constants;

import javax.validation.constraints.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utils.Utilities.presentOrFutureDate;

/**
 * stores event related data to be stored into and extract from database table named events.
 * It contains following attributes :-
 * eventId            : id of the event.
 * eventName          : event name.
 * eventOrganizerId   : event organizer/creator email id.
 * eventOrganizerName : event organizer name.
 * eventDate          : date of event.
 * eventLocation      : location of event.
 * eventCategories    : category of event
 * eventDescription   : description of event.
 *
 * @author nilimajha
 */
public class Event {

    @NotEmpty (message = Constants.FIELD_NOT_EMPTY_ERROR_MESSAGE)
    @NotNull (message = Constants.FILED_NOT_NULL_ERROR_MESSAGE)
    @Size (max = Constants.EVENT_NAME_SIZE_CONSTRAINT,
            message = Constants.NAME_SIZE_ERROR_MESSAGE)
    private String eventName;

    @NotEmpty (message = Constants.FIELD_NOT_EMPTY_ERROR_MESSAGE)
    @EventDate
    private String eventDate;

    @NotEmpty (message = Constants.FIELD_NOT_EMPTY_ERROR_MESSAGE)
    @NotBlank (message = Constants.FIELD_NOT_BLANK_ERROR_MESSAGE)
    private String eventLocation;

    @NotEmpty (message = Constants.FIELD_NOT_EMPTY_ERROR_MESSAGE)
    @NotBlank (message = Constants.FIELD_NOT_BLANK_ERROR_MESSAGE)
    private String eventCategories;

    @NotBlank (message = Constants.FIELD_NOT_BLANK_ERROR_MESSAGE)
    @NotEmpty (message = Constants.FIELD_NOT_EMPTY_ERROR_MESSAGE)
    @Size (max = Constants.EVENT_DESCRIPTION_SIZE_CONSTRAINT,
            message = Constants.EVENT_DESCRIPTION_ERROR_MESSAGE)
    private String eventDescription;

    @Digits(integer = 4, fraction = 0 ,message = "wrong total ticket")
    private int totalTickets;

    private int eventId;
    private String eventOrganizerId;
    private String eventOrganizerName;
    private int ticketsAvailable;
    private String eventCreatedOn;
    private boolean isActive;

    /**
     * empty constructor.
     */
    public Event() {
    }

    /**
     * Constructor to initialise all the attributes of the class.
     * @param eventId
     * @param eventName
     * @param eventDate
     * @param eventOrganizerName
     * @param eventLocation
     * @param eventCategories
     * @param eventDescription
     * @param totalTickets
     * @param ticketsAvailable
     * @param eventCreatedOn
     */
    public Event(int eventId, String eventName, String eventDate, String eventOrganizerName,
                 String eventLocation, String eventCategories, String eventDescription,
                 int totalTickets, int ticketsAvailable, String eventCreatedOn) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventOrganizerName = eventOrganizerName;
        this.eventLocation = eventLocation;
        this.eventCategories = eventCategories;
        this.eventDescription = eventDescription;
        this.totalTickets = totalTickets;
        this.ticketsAvailable = ticketsAvailable;
        this.eventCreatedOn = eventCreatedOn;
        this.isActive = presentOrFutureDate(eventDate);

    }

    /**
     * constructor that initialises the event class object attribute using result data obtained from database.
     * @param rs
     * @throws SQLException
     */
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
                this.eventCreatedOn = rs.getString("created_on");
                System.out.println("eventCreatedOn :" + eventCreatedOn);
            }
            this.isActive = presentOrFutureDate(eventDate);
        } else {
            System.out.println("No Event with given EventId");
        }

    }

    /**
     * getter for class attribute eventId.
     * @return eventId
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * getter for class attribute eventName.
     * @return eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * getter for class attribute eventOrganizerId.
     * @return eventOrganizerId
     */
    public String getEventOrganizerId() {
        return eventOrganizerId;
    }

    /**
     * getter for class attribute eventOrganizerName.
     * @return eventOrganizerName
     */
    public String getEventOrganizerName() {
        return eventOrganizerName;
    }

    /**
     * getter for class attribute eventDate.
     * @return eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * getter for class attribute eventLocation.
     * @return eventLocation
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * getter for class attribute eventCategories.
     * @return eventCategories
     */
    public String getEventCategories() {
        return eventCategories;
    }

    /**
     * getter for class attribute eventDescription.
     * @return eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * getter for class attribute totalTickets.
     * @return totalTickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * getter for class attribute ticketsAvailable.
     * @return ticketsAvailable
     */
    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    /**
     * getter for class attribute eventCreatedOn.
     * @return eventCreatedOn
     */
    public String getEventCreatedOn() {
        return eventCreatedOn;
    }

    /**
     * method sets the value of isActive as true or false and returns its value.
     * @return isActive
     */
    public boolean isActive() {
        this.isActive = presentOrFutureDate(this.eventDate);
        return isActive;
    }

    /**
     * setter for class attribute eventId.
     * @return eventId
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * setter for class attribute eventName.
     * @return eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * setter for class attribute eventOrganizer.
     * @return eventOrganizer
     */
    public void setEventOrganizerId(String eventOrganizer) {
        this.eventOrganizerId = eventOrganizer;
    }

    /**
     * setter for class attribute eventOrganizerName.
     * @return eventOrganizerName
     */
    public void setEventOrganizerName(String eventOrganizerName) {
        this.eventOrganizerName = eventOrganizerName;
    }

    /**
     * setter for class attribute eventDate.
     * @return eventDate
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * setter for class attribute eventLocation.
     * @return eventLocation
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * setter for class attribute eventCategories.
     * @return eventCategories
     */
    public void setEventCategories(String eventCategories) {
        this.eventCategories = eventCategories;
    }

    /**
     * setter for class attribute eventDescription.
     * @return eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * setter for class attribute totalTickets.
     * @return totalTickets
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * setter for class attribute ticketsAvailable.
     * @return ticketsAvailable
     */
    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    /**
     * setter for class attribute eventCreatedOn.
     * @return eventCreatedOn
     */
    public void setEventCreatedOn(String eventCreatedOn) {
        this.eventCreatedOn = eventCreatedOn;
    }
}
