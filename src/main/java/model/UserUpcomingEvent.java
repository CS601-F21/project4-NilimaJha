package model;

public class UserUpcomingEvent {
    private Event event = new Event();
    private int totalTickets;

    public UserUpcomingEvent() {
    }

    public UserUpcomingEvent(Event event, int totalTickets) {
        this.event = event;
        this.totalTickets = totalTickets;
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
}
