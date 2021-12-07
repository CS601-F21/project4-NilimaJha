package model;

/**
 * stores ticket related data to be stored into and extract from database table named events.
 * It contains following attributes :-
 * ticketId             : id of ticket
 * eventId              : Id of event to which thicket belongs to.
 * ticketOwnerId        : email id of user to whom the tickets belongs to.
 * purchaseDate         : date on which the ticket was purchased.
 * totalTicketsSelected : stores the number of tickets when user buys for an event at a time.
 */
public class Tickets {

    private int ticketId;
    private int eventId;
    private String ticketOwnerId;
    private String purchaseDate;
    private int totalTicketsSelected;

    /**
     * Constructor
     */
    public Tickets() {
    }

    /**
     * getter for class attribute named totalTicketsSelected.
     * @param totalTicketsSelected
     */
    public Tickets(int totalTicketsSelected) {
        this.totalTicketsSelected = totalTicketsSelected;
    }

    /**
     * getter for class attribute named ticketId.
     * @return
     */
    public int getTicketId() {
        return ticketId;
    }

    /**
     * getter for class attribute named eventId.
     * @return
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * getter for class attribute named ticketOwnerId.
     * @return
     */
    public String getTicketOwnerId() {
        return ticketOwnerId;
    }

    /**
     * getter for class attribute named purchaseDate.
     * @return
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * getter for class attribute named totalTicketsSelected.
     * @return
     */
    public int getTotalTicketsSelected() {
        return totalTicketsSelected;
    }

    /**
     * setter for class attribute named ticketId.
     * @param ticketId
     */
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * setter for class attribute named eventId.
     * @param eventId
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * setter for class attribute named ticketOwnerId.
     * @param ticketOwnerId
     */
    public void setTicketOwnerId(String ticketOwnerId) {
        this.ticketOwnerId = ticketOwnerId;
    }

    /**
     * setter for class attribute named purchaseDate.
     * @param purchaseDate
     */
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * setter for class attribute named totalTicketsSelected.
     * @param totalTicketsSelected
     */
    public void setTotalTicketsSelected(int totalTicketsSelected) {
        this.totalTicketsSelected = totalTicketsSelected;
    }
}
