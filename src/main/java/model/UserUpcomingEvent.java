package model;

import java.util.ArrayList;

/**
 * stores information extracted from database about user upcoming events and tickets information.
 * It contains following attributes :-
 * event        : objects of Event class to store event information of the event to which the ticket belongs.
 * totalTickets : total tickets of the event.
 * ticketIdList : lists of all ticket ids associated to the event stored in UserUpcomingEvent object.
 * @author nilimajha
 */
public class UserUpcomingEvent {

    private Event event = new Event();
    private int totalTickets;
    private ArrayList<Integer> ticketIdList;

    /**
     * Constructor that initialises ticketIdList attribute.
     */
    public UserUpcomingEvent() {
        ticketIdList = new ArrayList<>();
    }

    /**
     * Constructor initialises attributes event and totalTickets with
     * given event object and totalTickets.
     * @param event
     * @param totalTickets
     */
    public UserUpcomingEvent(Event event, int totalTickets) {
        this.event = event;
        this.totalTickets = totalTickets;
    }

    /**
     * getter for class attribute ticketIdList.
     * @return ticketIdList
     */
    public ArrayList<Integer> getTicketIdList() {
        return ticketIdList;
    }

    /**
     * getter for class attribute event.
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * getter for class attribute totalTickets.
     * @return totalTickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * setter for class attribute event.
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * setter for class attribute totalTickets.
     * @param totalTickets
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * setter for class attribute ticketIdList.
     * @param ticketIdList
     */
    public void setTicketIdList(ArrayList<Integer> ticketIdList) {
        this.ticketIdList = ticketIdList;
    }

    /**
     * adds given value to the ArrayList ticketIdList.
     * @param ticketId
     */
    public void addIntoTicketIdList(int ticketId) {
        this.ticketIdList.add(ticketId);
    }
}
