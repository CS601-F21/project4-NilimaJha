package model;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Size;

public class Tickets {

    private int ticketId;
    private int eventId;
    private String ticketOwnerId;
    private String purchaseDate;
    private int totalTicketsSelected;

    public Tickets() {
    }

    public Tickets(int totalTicketsSelected) {
        this.totalTicketsSelected = totalTicketsSelected;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTicketOwnerId() {
        return ticketOwnerId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public int getTotalTicketsSelected() {
        return totalTicketsSelected;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTicketOwnerId(String ticketOwnerId) {
        this.ticketOwnerId = ticketOwnerId;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setTotalTicketsSelected(int totalTicketsSelected) {
        this.totalTicketsSelected = totalTicketsSelected;
    }
}
