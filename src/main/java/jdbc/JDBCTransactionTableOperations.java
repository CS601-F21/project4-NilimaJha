package jdbc;

import model.*;
import utils.Constants;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jdbc.JDBCEventTableOperations.listOfEventsFromResultSet;
import static utils.Utilities.currentDate;

/**
 * Class with methods to perform operation on Transaction table.
 * @author nilimajha
 */
public class JDBCTransactionTableOperations {


    /**
     * method to insert into transaction table.
     * @param transaction
     * @return
     * @throws SQLException
     */
    public static boolean insertIntoTransactionTable(Transaction transaction) throws SQLException {
        String updateTransactionTableSql = "INSERT INTO transactions (transaction_type, user_email_id, event_id, transaction_details) VALUES (?, ?, ?, ?);";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement updateTransactionTableSqlStm = connection.prepareStatement(updateTransactionTableSql);
            updateTransactionTableSqlStm.setString(1, String.valueOf(transaction.getTransactionType()));
            updateTransactionTableSqlStm.setString(2, transaction.getUserEmailId());
            updateTransactionTableSqlStm.setInt(3, transaction.getEventId());
            updateTransactionTableSqlStm.setString(4, transaction.getTransactionDetail());
            int executionResult = updateTransactionTableSqlStm.executeUpdate();
            if (executionResult == 0) {
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * method to update tickets table when transfer is done.
     * @param ticketTransfer
     * @return
     * @throws SQLException
     */
    public static boolean updateTicketsAndTransactionTableForTransfer(TicketTransfer ticketTransfer) throws SQLException {
        String currentDate = currentDate();
        String updateTicketTableSql = "UPDATE tickets_list SET ticket_owner_id = ?, purchase_date = ? WHERE ticket_id = ?;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement updateTicketTableSqlStm = connection.prepareStatement(updateTicketTableSql);
            updateTicketTableSqlStm.setString(1, ticketTransfer.getTransferee());
            updateTicketTableSqlStm.setString(2, currentDate);
            int totalUpdate = 0;
            for (int eachTicketId : ticketTransfer.getTicketIdList()) {
                updateTicketTableSqlStm.setInt(3, eachTicketId);
                totalUpdate += updateTicketTableSqlStm.executeUpdate();

                String selectTicketEventIdSql = "SELECT event_id FROM tickets_list WHERE ticket_id = ?;";
                PreparedStatement selectTicketEventIdSqlStm = connection.prepareStatement(selectTicketEventIdSql);
                selectTicketEventIdSqlStm.setInt(1, eachTicketId);
                ResultSet resultSet = selectTicketEventIdSqlStm.executeQuery();
                if (resultSet.next()) {
                    int eventId = resultSet.getInt("event_id");
                    Transaction transaction = new Transaction(
                            Constants.TRANSACTION_TYPE_TRANSFERRED,
                            ticketTransfer.getTransferor(),
                            eventId);
                    transaction.setTransactionDetail(generateTransactionDetailMessage(
                            Constants.TRANSACTION_TYPE_TRANSFERRED));
                    boolean transactionSuccessful = insertIntoTransactionTable(transaction);
                    transaction.setTransactionType(Constants.TRANSACTION_TYPE_RECEIVED);
                    transaction.setUserEmailId(ticketTransfer.getTransferee());
                    transaction.setTransactionDetail(generateTransactionDetailMessage(
                            Constants.TRANSACTION_TYPE_RECEIVED));
                    transactionSuccessful = insertIntoTransactionTable(transaction);
                }
            }
            if (totalUpdate == ticketTransfer.getTicketIdList().size()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * all transaction belonging to a user including purchase transfer & received.
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<Transaction> allTransactionOfUser(String userId) throws SQLException {
        String selectUserTransaction = "SELECT transaction_id, transaction_type, event_id, transaction_date, transaction_details FROM transactions WHERE user_email_id = ?;";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement selectUserTransactionStm = connection.prepareStatement(selectUserTransaction);
            selectUserTransactionStm.setString(1, userId);
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

    /**
     * A method to perform select from userInfo table where user_email_id matches the given email_id.
     * A PrepareStatement is used to execute this query.
     * @throws SQLException
     */
    public static List<Event> findEventsByPhraseFromEventsTable (String searchCategory, String searchType, String searchTerm) throws SQLException {
        String selectEventInfoSql;
        PreparedStatement selectEventInfoSqlStmt;
        List<Event> eventsList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()) {
            if (searchType.equals("equals")) {
                if (searchCategory.equals("event_organizer")) {
                    selectEventInfoSql = "SELECT events.event_id, events.event_name, " +
                            "events.event_organizer, events.event_date, events.event_location, " +
                            "events.event_categories, events.event_description, events.total_tickets, " +
                            "events.tickets_available, events.created_on, events.event_id " +
                            "FROM events JOIN usersInfo ON events.event_organizer = usersInfo.user_email_id WHERE " +
                            "usersInfo.user_name = ?;";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, searchTerm);
                } else {
                    selectEventInfoSql = "SELECT * FROM events WHERE " + searchCategory + " = ? ;" ;
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, searchTerm);
                }
                ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
                eventsList = listOfEventsFromResultSet(resultSet);
            } else {
                if (searchCategory.equals("event_organizer")) {
                    selectEventInfoSql = "SELECT events.event_id, events.event_name, " +
                            "events.event_organizer, events.event_date, events.event_location, " +
                            "events.event_categories, events.event_description, events.total_tickets, " +
                            "events.tickets_available, events.created_on, events.event_id " +
                            "FROM events JOIN usersInfo ON events.event_organizer = usersInfo.user_email_id WHERE " +
                            "usersInfo.user_name LIKE ?;";
                    String term = "%" + searchTerm + "%";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, term);
                } else {
                    selectEventInfoSql = "SELECT * FROM events WHERE " + searchCategory + " LIKE ? ;" ;
                    String term = "%" + searchTerm + "%";
                    selectEventInfoSqlStmt = connection.prepareStatement(selectEventInfoSql);
                    selectEventInfoSqlStmt.setString(1, term);
                }
                ResultSet resultSet = selectEventInfoSqlStmt.executeQuery();
                eventsList = listOfEventsFromResultSet(resultSet);
            }
        }

        return eventsList;
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

}
