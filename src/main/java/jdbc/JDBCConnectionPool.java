package jdbc;

import model.*;
import web.login.LoginServerConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnectionPool {

    /**
     * A method to perform Insert into usersInfo table in database using a PreparedStatement.
     * @param user
     * @throws SQLException
     */
    public static void executeInsertInUserInfo(User user) throws SQLException {
        //write-userInfoTableLock for insert into usersInfo table
        try (Connection connection = DBCPDataSource.getConnection()) {
            String insertUserInfoSql = "INSERT INTO usersInfo (user_email_id, user_name, user_access_token, complete_profile) VALUES (?, ?, ?, ?);";
            PreparedStatement insertUerInfoStmt = connection.prepareStatement(insertUserInfoSql);
            insertUerInfoStmt.setString(1, user.getUserEmailId());
            insertUerInfoStmt.setString(2, user.getUserName());
            insertUerInfoStmt.setString(3, user.getUserAccessToken());
            insertUerInfoStmt.setBoolean(4, false);
            System.out.println("Query on userInfo: " + insertUerInfoStmt);
            insertUerInfoStmt.executeUpdate();
            System.out.println("One line executed in usersInfo table in DB.");
        }
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static User findUserFromUserInfoByEmailId (String userEmailId) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectUserInfoSql = "SELECT * FROM usersInfo WHERE user_email_id = ?;";
            PreparedStatement selectUserInfoSqlStmt = connection.prepareStatement(selectUserInfoSql);
            selectUserInfoSqlStmt.setString(1, userEmailId);
            System.out.println("Query on usersInfo: " + selectUserInfoSqlStmt);
            ResultSet resultSet = selectUserInfoSqlStmt.executeQuery();
            User user = new User(resultSet);
            return user;
        }
    }


    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static ArrayList<String> findUserFromUserInfoByName (String userName) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        ArrayList<String> usersEmailIds = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectUserInfoSql = "SELECT user_email_id FROM usersInfo WHERE user_name = ?;";
            PreparedStatement selectUserInfoSqlStmt = connection.prepareStatement(selectUserInfoSql);
            selectUserInfoSqlStmt.setString(1, userName);
            System.out.println("Query on usersInfo: " + selectUserInfoSqlStmt);
            ResultSet resultSet = selectUserInfoSqlStmt.executeQuery();
            while (resultSet.next()) {
                usersEmailIds.add(resultSet.getString("user_email_id"));
            }
            return usersEmailIds;
        }
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static void updateUserInfoTable (String userEmailId, User user) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        try (Connection connection = DBCPDataSource.getConnection()) {
            String updateUserInfoSql = "UPDATE usersInfo SET user_name = ?, phone_number = ?, date_of_birth = ?, complete_profile = ? WHERE user_email_id = ?";
            PreparedStatement updateUserInfoSqlStm = connection.prepareStatement(updateUserInfoSql);
            updateUserInfoSqlStm.setString(1, user.getUserName());
            updateUserInfoSqlStm.setString(2, user.getPhone());
            updateUserInfoSqlStm.setString(3, user.getDateOfBirth());
            updateUserInfoSqlStm.setBoolean(4, true);
            updateUserInfoSqlStm.setString(5, userEmailId);
            System.out.println("Query on usersInfo: " + updateUserInfoSqlStm);
            int updateResult = updateUserInfoSqlStm.executeUpdate();
            System.out.println(updateResult);
        }
    }

    /**
     * A method to insert new event into events table using a PreparedStatement.
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
//            createNewEventStm.setString(9, "active");
            createNewEventStm.executeUpdate();
        }
        System.out.println("1 event inserted into db.");
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static List<Event> findEventsFromEventsTable () throws SQLException {
        //read-userInfoTableLock on usersInfo table
        List<Event> eventList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_date >= CURRENT_DATE;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            System.out.println("Query on usersInfo: " + selectEventInfoSqlStmt);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            eventList = listOfObjFromResultSet(resultSet);
        }
        return eventList;
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static List<Event> findEventsByOrganizerId (String eventOrganizer) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        List<Event> eventList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_organizer = ?;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            selectEventInfoSqlStmt.setString(1, eventOrganizer);
            System.out.println("Query on usersInfo: " + selectEventInfoSqlStmt);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            eventList = listOfObjFromResultSet(resultSet);
        }
        return eventList;
    }



    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static List<Event> findEventsByEventCreatorFromEventsTable (String eventOrganizerId) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        List<Event> eventList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_organizer = ?;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            selectEventInfoSqlStmt.setString(1, eventOrganizerId);
            System.out.println("Query on usersInfo: " + selectEventInfoSqlStmt);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            eventList = listOfObjFromResultSet(resultSet);
        }
        return eventList;
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static List<Event> findEventsByPhraseFromEventsTable (String searchCategory, String searchType, String searchTerm) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        String selectEventInfoSql;
        PreparedStatement selectEventInfoSqlStmt;
        List<Event> eventsList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            if (searchType.equals("equals")) {
                if (searchCategory.equals("event_organizer")) {
                    selectEventInfoSql = "SELECT events.event_id, events.event_name, " +
                            "events.event_organizer, events.event_date, events.event_location, " +
                            "events.event_categories, events.event_description, events.total_tickets, " +
                            "events.tickets_available, events.event_status, events.created_on, events.event_id " +
                            "FROM events JOIN usersInfo ON events.event_organizer = usersInfo.user_email_id WHERE " +
                            "usersInfo.user_name = ?;";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, searchTerm);
                } else {
                    selectEventInfoSql = "SELECT * FROM events WHERE " + searchCategory + " = ? ;" ;
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    System.out.println("QUERY  before entering value : " + selectEventInfoSqlStmt);
                    selectEventInfoSqlStmt.setString(1, searchTerm);
                }
                System.out.println(">>>>>>>>>>>>>\nQUERY :" + selectEventInfoSqlStmt);
                ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
                eventsList = listOfObjFromResultSet(resultSet);
            } else {
                if (searchCategory.equals("event_organizer")) {
                    selectEventInfoSql = "SELECT events.event_id, events.event_name, " +
                            "events.event_organizer, events.event_date, events.event_location, " +
                            "events.event_categories, events.event_description, events.total_tickets, " +
                            "events.tickets_available, events.event_status, events.created_on, events.event_id " +
                            "FROM events JOIN usersInfo ON events.event_organizer = usersInfo.user_email_id WHERE " +
                            "usersInfo.user_name LIKE ?;";
                    String term = "%" + searchTerm + "%";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, term);
                } else {
                    selectEventInfoSql = "SELECT * FROM events WHERE " + searchCategory + " LIKE ? ;" ;
                    String term = "%" + searchTerm + "%";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    System.out.println("QUERY  before entering value : " + selectEventInfoSqlStmt);
                    selectEventInfoSqlStmt.setString(1, term);
                }
                System.out.println(">>>>>>>>>>>>>\nQUERY :" + selectEventInfoSqlStmt);
                ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
                eventsList = listOfObjFromResultSet(resultSet);
            }
        }

        return eventsList;
    }

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static Event findEventByEventIdFromEventsTable (int eventId) throws SQLException {
        //read-userInfoTableLock on usersInfo table
        Event event = new Event();
        try (Connection connection = DBCPDataSource.getConnection()) {
            String selectEventInfoSql = "SELECT * FROM events WHERE event_id = ?;";
            PreparedStatement selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
            selectEventInfoSqlStmt.setInt(1, eventId);
            System.out.println("Query on usersInfo: " + selectEventInfoSqlStmt);
            ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
            event = new Event(resultSet);
            User user = findUserFromUserInfoByEmailId(event.getEventOrganizerId());
            event.setEventOrganizerName(user.getUserName());
        }
        return event;
    }

    /**
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static List<Event> listOfObjFromResultSet(ResultSet resultSet) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        while (resultSet.next()) {
            String eventCreatorName = null;
            try {
                User user = findUserFromUserInfoByEmailId(resultSet.getString("event_organizer"));
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
//                    resultSet.getString("event_status"),
                    resultSet.getString("created_on"));
            eventList.add(event);
        }
        return eventList;
    }

    /**
     * A method to insert new event into events table using a PreparedStatement.
     * @param tickets
     * @throws SQLException
     */
    public static boolean executeInsertIntoTickets(Tickets tickets) throws SQLException {
        System.out.println("Inside JDBC executeInsertIntoTicket.");
        //acquire write lock on events table
        //acquire write lock on tickets table
        String findEventsSql = "SELECT tickets_available FROM events WHERE event_id = ?;";
        String insertTicketsSql = "INSERT INTO tickets_list (event_id, ticket_owner_id) VALUES (?, ?);";
        String updateTotalTicketValueSql = "UPDATE events SET tickets_available = ? WHERE event_id = ?;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement findEventsSqlStm = connection.prepareStatement(findEventsSql);
            findEventsSqlStm.setInt(1, tickets.getEventId());

            System.out.println("1st Prepared Statement : " + findEventsSqlStm);

            ResultSet resultSet = findEventsSqlStm.executeQuery();
            int availableTickets = 0;
            if (resultSet.next()) {
                availableTickets = resultSet.getInt("tickets_available");
            }
            if (tickets.getTotalTicketsSelected() <= availableTickets) {
                PreparedStatement insertTicketsSqlStm = connection.prepareStatement(insertTicketsSql);
                insertTicketsSqlStm.setInt(1, tickets.getEventId());
                insertTicketsSqlStm.setString(2, tickets.getTicketOwnerId());
                System.out.println("1st Prepared Statement : " +findEventsSqlStm);
                for (int i = 0; i < tickets.getTotalTicketsSelected(); i++) {
                    insertTicketsSqlStm.executeUpdate();
                }
                PreparedStatement updateTotalTicketValueSqlStm = connection.prepareStatement(updateTotalTicketValueSql);
                updateTotalTicketValueSqlStm.setInt(1, availableTickets - tickets.getTotalTicketsSelected());
                updateTotalTicketValueSqlStm.setInt(2, tickets.getEventId());
                System.out.println("TicketAvailability Update after purchase : " + updateTotalTicketValueSqlStm);
                updateTotalTicketValueSqlStm.executeUpdate();
                // create constant file add transaction type = B
                Transaction transaction = new Transaction(LoginServerConstants.TRANSACTION_TYPE_PURCHASED, tickets.getTicketOwnerId(), tickets.getEventId());
                String transactionMessage = generateTransactionDetailMessage(LoginServerConstants.TRANSACTION_TYPE_PURCHASED);
                transaction.setTransactionDetail(transactionMessage);
                Boolean updateTransactionTable = insertIntoTransactionTable(transaction);
                System.out.println(availableTickets + " tickets processing done '" + updateTransactionTable + "'. inserted in DB.");
                return updateTransactionTable;
            }
            else {
                System.out.println(availableTickets + " tickets processing unsuccessful. insertion unsuccessful.");
                return false;
            }
        }
        //release both locks;
    }

    /**
     * method to generate message for each transaction.
     * @param transactionType
     * @return
     */
    public static String generateTransactionDetailMessage(String transactionType) {
        if (transactionType.equals("Purchased")) {
            return "Purchased New Tickets.";
        } else if (transactionType.equals("Transferred")){
            return "Transferred ticket to other user.";
        } else {
            return "Received ticket from other user.";
        }
    }


    public static boolean insertIntoTransactionTable(Transaction transaction) throws SQLException {
        //acquire transaction-table write lock
        String updateTransactionTableSql = "INSERT INTO transactions (transaction_type, user_email_id, event_id, transaction_details) VALUES (?, ?, ?, ?);";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement updateTransactionTableSqlStm = connection.prepareStatement(updateTransactionTableSql);
            updateTransactionTableSqlStm.setString(1, String.valueOf(transaction.getTransactionType()));
            updateTransactionTableSqlStm.setString(2, transaction.getUserEmailId());
            updateTransactionTableSqlStm.setInt(3, transaction.getEventId());
            updateTransactionTableSqlStm.setString(4, transaction.getTransactionDetail());
            System.out.println("insertIntoTransactionTable Prepared Statement : " + updateTransactionTableSqlStm);
            int executionResult = updateTransactionTableSqlStm.executeUpdate();
            if (executionResult == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static List<UserUpcomingEvent> allUpcomingEvents(String userId) throws SQLException {
        //acquire tickets-table read lock and Event read lock
        //SELECT () FROM
        //SELECT events.event_id, event_name, event_date, event_categories FROM events JOIN tickets_list ON events.event_id = tickets_list.event_id WHERE tickets_list.ticket_owner_id = 'njha2@dons.usfca.edu' AND events.event_date >= CURRENT_DATE;
        String selectUpcomingEventsSql = "SELECT events.event_id, event_name, event_date, event_categories, " +
                "tickets_list.ticket_id FROM events JOIN tickets_list ON events.event_id = tickets_list.event_id WHERE " +
                "tickets_list.ticket_owner_id = ? AND events.event_date >= CURRENT_DATE;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement selectUpcomingEventsSqlStm = connection.prepareStatement(selectUpcomingEventsSql);
            selectUpcomingEventsSqlStm.setString(1, userId);
            System.out.println("selectUpcomingEventsSql Prepared Statement : " + selectUpcomingEventsSqlStm);
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

    public static boolean updateTicketsAndTransactionTableForTransfer(TicketTransfer ticketTransfer) throws SQLException {
        //acquire ticket-table write lock
        //acquire transaction table write lock
        //
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);
        System.out.println("Date :" + currentDate);
        //validate if transferee is not a valid user.
//        User user = findUserFromUserInfoByEmailId(ticketTransfer.getTransferee());
//        if (!user.isValid()) {
//            return false;
//        } else {
            String updateTicketTableSql = "UPDATE tickets_list SET ticket_owner_id = ?, purchase_date = ? WHERE ticket_id = ?;";
            try (Connection connection = DBCPDataSource.getConnection()) {
                PreparedStatement updateTicketTableSqlStm = connection.prepareStatement(updateTicketTableSql);
                updateTicketTableSqlStm.setString(1, ticketTransfer.getTransferee());
                updateTicketTableSqlStm.setString(2, currentDate);
                int totalUpdate = 0;
                for (int eachTicketId : ticketTransfer.getTicketIdList()) {
                    updateTicketTableSqlStm.setInt(3, eachTicketId);
                    System.out.println("1eachTicketId : " + eachTicketId);
                    System.out.println("TRANSFER TICKET QUERY: " + updateTicketTableSqlStm);
                    totalUpdate += updateTicketTableSqlStm.executeUpdate();
                    //extract event_id of ticket
//                //change Transaction type
                    String selectTicketEventIdSql = "SELECT event_id FROM tickets_list WHERE ticket_id = ?;";
                    PreparedStatement selectTicketEventIdSqlStm = connection.prepareStatement(selectTicketEventIdSql);
                    System.out.println("2eachTicketId : " + eachTicketId);
                    selectTicketEventIdSqlStm.setInt(1, eachTicketId);
                    System.out.println("3eachTicketId : " + eachTicketId);
                    System.out.println("Getting emailId QUERY:" + selectTicketEventIdSqlStm);
                    ResultSet resultSet = selectTicketEventIdSqlStm.executeQuery();
                    if (resultSet.next()) {
                        int eventId = resultSet.getInt("event_id");
                        Transaction transaction = new Transaction(LoginServerConstants.TRANSACTION_TYPE_TRANSFERRED, ticketTransfer.getTransferor(), eventId);
                        transaction.setTransactionDetail(generateTransactionDetailMessage(LoginServerConstants.TRANSACTION_TYPE_TRANSFERRED));
                        boolean transactionSuccessful = insertIntoTransactionTable(transaction);
                        transaction.setTransactionType(LoginServerConstants.TRANSACTION_TYPE_RECEIVED);
                        transaction.setUserEmailId(ticketTransfer.getTransferee());
                        transaction.setTransactionDetail(generateTransactionDetailMessage(LoginServerConstants.TRANSACTION_TYPE_RECEIVED));
                        transactionSuccessful = insertIntoTransactionTable(transaction);
                    }
                }
                if (totalUpdate == ticketTransfer.getTicketIdList().size()) {
                    return true;
                } else {
                    return false;
                }
//            }
        }
    }

    /**
     * all transaction belonging to a user including purchase transfer & received.
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<Transaction> allTransactionOfUser(String userId) throws SQLException {
        //acquire tickets-table read lock and Event read lock
        String selectUserTransaction = "SELECT transaction_id, transaction_type, event_id, transaction_date, transaction_details FROM transactions WHERE user_email_id = ?;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement selectUserTransactionStm = connection.prepareStatement(selectUserTransaction);
            selectUserTransactionStm.setString(1, userId);
            System.out.println("selectUpcomingEventsSql Prepared Statement : " + selectUserTransactionStm);
            ResultSet resultSet = selectUserTransactionStm.executeQuery();
            List<Transaction> transactionList = new ArrayList<>();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt("transaction_id"));
                transaction.setTransactionType(resultSet.getString("transaction_type"));
                transaction.setEventId(resultSet.getInt("event_id"));
                transaction.setTransactionDate(resultSet.getString("transaction_date"));
                transaction.setTransactionDetail(resultSet.getString("transaction_details"));
                transactionList.add(transaction);
            }
            return transactionList;
        }
    }

    public static boolean resultSetIsEmpty (ResultSet results) {
        try {
            if (!results.next()) {
               return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        String stringDate = "2022-04-07";
        LocalDateTime inputDateTime = LocalDateTime.parse(stringDate+"T00:00:00.000");

        System.out.println("inputDateTime   : " + dtf.format(inputDateTime));
        System.out.println("CurrentDateTime : " + dtf.format(now));

        boolean isBefore = inputDateTime.isBefore(now);
        System.out.println("input: "+ dtf.format(inputDateTime) + " is Before now:" + dtf.format(now) + " ->  " + isBefore);

    }
}
