package model;

import java.util.ArrayList;

public class UserUpcomingEvent {
    private Event event = new Event();
    private int totalTickets;
    private ArrayList<Integer> ticketIdList = new ArrayList<>();

    public UserUpcomingEvent() {
    }

    public UserUpcomingEvent(Event event, int totalTickets) {
        this.event = event;
        this.totalTickets = totalTickets;
    }

    public ArrayList<Integer> getTicketIdList() {
        return ticketIdList;
    }

    public Event getEvent() {
        return event;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketIdList(ArrayList<Integer> ticketIdList) {
        this.ticketIdList = ticketIdList;
    }

    public void addIntoTicketIdList(int ticketId) {
        this.ticketIdList.add(ticketId);
    }
}
