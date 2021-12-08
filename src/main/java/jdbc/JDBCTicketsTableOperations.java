package jdbc;

import model.Tickets;
import model.Transaction;
import web.login.LoginServerConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static jdbc.JDBCConnectionPool.generateTransactionDetailMessage;
import static jdbc.JDBCConnectionPool.insertIntoTransactionTable;

/**
 * Class with methods to perform operation on Tickets table.
 * @author nilimajha
 */
public class JDBCTicketsTableOperations {

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
}