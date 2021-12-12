package jdbc;

import model.Event;
import model.User;
import model.UserUpcomingEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with methods to perform operation on Events table.
 * @author nilimajha
 */
public class JDBCEventTableOperations {

    /**
     * A method to insert new event into events table.
     * @param event
     * @throws SQLException
     */
    public static void executeInsertIntoEvents(Event event) throws SQLException {
        String createNewEventSql = "INSERT INTO events " +
                "(event_name, event_organizer, event_date, " +
                "event_location, event_categories, event_description, " +
                "total_tickets, tickets_available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement createNewEventStm = connection.prepareStatement(createNewEventSql);
            createNewEventStm.setString(1, event.getEventName());
            createNewEventStm.setString(2, event.getEventOrganizerId());
            createNewEventStm.setString(3, event.getEventDate());
            createNewEventStm.setString(4, event.getEventLocation());
            createNewEventStm.setString(5, event.getEventCategories());
            createNewEventStm.setString(6, event.getEventDescription());
            createNewEventStm.setInt(7, event.getTotalTickets());
            createNewEventStm.setInt(8, event.getTotalTickets());
            createNewEventStm.executeUpdate();
        }
    }

    /**
     * A method get all events from events table where date is future date.
     * @throws SQLException
     */
    public static List<Event> findEventsFromEventsTable () throws SQLException {
        List<Event> eventList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_date >= CURRENT_DATE;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            eventList = listOfEventsFromResultSet(resultSet);
        }
        return eventList;
    }

    /**
     * A method get all events from events table where event organizerId matches the given ID .
     * @throws SQLException
     */
    public static List<Event> findEventsByOrganizerId (String eventOrganizer) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_organizer = ?;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            selectEventInfoSqlStmt.setString(1, eventOrganizer);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            eventList = listOfEventsFromResultSet(resultSet);
        }
        return eventList;
    }

    /**
     * method to make list of Event class object from result set obtained from DB.
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static List<Event> listOfEventsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        while (resultSet.next()) {
            String eventCreatorName = null;
            try {
                User user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(resultSet.getString("event_organizer"));
                eventCreatorName = user.getUserName();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Event event = new Event(resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getString("event_date"),
                    eventCreatorName,
                    resultSet.getString("event_location"),
                    resultSet.getString("event_categories"),
                    resultSet.getString("event_description"),
                    resultSet.getInt("total_tickets"),
                    resultSet.getInt("tickets_available"),
                    resultSet.getString("created_on"));
            eventList.add(event);
        }
        return eventList;
    }

    /**
     * A method to get event information from table whose
     * event-id matches the given event-id.
     * @throws SQLException
     */
    public static Event findEventByEventIdFromEventsTable (int eventId) throws SQLException {

        Event event = new Event();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_id = ?;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            selectEventInfoSqlStmt.setInt(1, eventId);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            event = new Event(resultSet);
            User user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(event.getEventOrganizerId());
            event.setEventOrganizerName(user.getUserName());
        }
        return event;
    }

    /**
     * method to get list of all the users upcoming ticket's event information with ticket ID.
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<UserUpcomingEvent> usersUpcomingTicketsWithEventInfo(String userId) throws SQLException {

        String selectUpcomingEventsSql = "SELECT events.event_id, event_name, event_date, event_categories, " +
                "tickets_list.ticket_id FROM events JOIN tickets_list ON events.event_id = tickets_list.event_id WHERE " +
                "tickets_list.ticket_owner_id = ? AND events.event_date >= CURRENT_DATE;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement selectUpcomingEventsSqlStm = connection.prepareStatement(selectUpcomingEventsSql);
            selectUpcomingEventsSqlStm.setString(1, userId);
            ResultSet resultSet = selectUpcomingEventsSqlStm.executeQuery();
            List<UserUpcomingEvent> userUpcomingEventsList = new ArrayList<>();
            List<Integer> eventIds = new ArrayList<>();
            if (resultSet.next()) {
                eventIds.add(resultSet.getInt("event_id"));
                UserUpcomingEvent userUpcomingEvent = new UserUpcomingEvent();
                Event event = new Event();
                event.setEventId(resultSet.getInt("event_id"));
                event.setEventName(resultSet.getString("event_name"));
                event.setEventDate(resultSet.getString("event_date"));
                event.setEventCategories(resultSet.getString("event_categories"));
                userUpcomingEvent.setEvent(event);
                userUpcomingEvent.setTotalTickets(1);
                userUpcomingEvent.addIntoTicketIdList(resultSet.getInt("ticket_id"));
                userUpcomingEventsList.add(userUpcomingEvent);
            }
            while (resultSet.next()) {
                if (eventIds.contains(resultSet.getInt("event_id"))) {
                    for (UserUpcomingEvent eachUserUpcomingEvent : userUpcomingEventsList) {
                        if (eachUserUpcomingEvent.getEvent().getEventId() == resultSet.getInt("event_id")) {
                            eachUserUpcomingEvent.setTotalTickets(eachUserUpcomingEvent.getTotalTickets() + 1);
                            eachUserUpcomingEvent.addIntoTicketIdList(resultSet.getInt("ticket_id"));
                        }
                    }
                } else {
                    eventIds.add(resultSet.getInt("event_id"));
                    UserUpcomingEvent userUpcomingEvent = new UserUpcomingEvent();
                    Event event = new Event();
                    event.setEventId(resultSet.getInt("event_id"));
                    event.setEventName(resultSet.getString("event_name"));
                    event.setEventDate(resultSet.getString("event_date"));
                    event.setEventCategories(resultSet.getString("event_categories"));
                    userUpcomingEvent.setEvent(event);
                    userUpcomingEvent.setTotalTickets(1);
                    userUpcomingEvent.addIntoTicketIdList(resultSet.getInt("ticket_id"));
                    userUpcomingEventsList.add(userUpcomingEvent);
                }
            }
            return userUpcomingEventsList;
        }
    }
}
