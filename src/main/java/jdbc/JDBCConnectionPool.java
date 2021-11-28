package jdbc;

import model.User;

import java.sql.*;

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
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param name
     * @param extension
     * @param email
     * @param startdate
     * @throws SQLException
     */
    public static void executeInsert(java.sql.Connection con, String name, int extension, String email, String startdate) throws SQLException {
        String insertContactSql = "INSERT INTO contacts (name, extension, email, startdate) VALUES (?, ?, ?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, name);
        insertContactStmt.setInt(2, extension);
        insertContactStmt.setString(3, email);
        insertContactStmt.setString(4, startdate);
        insertContactStmt.executeUpdate();
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @throws SQLException
     */
    public static void executeSelectAndPrint(java.sql.Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM contacts;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Name: %s\n", results.getString("name"));
            System.out.printf("Extension: %s\n", results.getInt("extension"));
            System.out.printf("Email: %s\n", results.getString("email"));
            System.out.printf("Start Date: %s\n", results.getString("startdate"));
        }
    }

    public static ResultSet executeSelectAllFromSessionInfoWhere(java.sql.Connection con, String sessionId) throws SQLException {
        String selectAllWhereSql = "SELECT * FROM session_info WHERE session=?;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllWhereSql);
        selectAllContactsStmt.setString(1, sessionId);
        ResultSet results = selectAllContactsStmt.executeQuery();
        if (!resultSetIsEmpty(results)) {
            return results;
        } else {
            return null;
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

    public static void executeSelectAndPrintForSession(java.sql.Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM session_info;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Session: %s\n", results.getString("session"));
            if (results.wasNull()) {
                System.out.printf("UserID: %s\n", null);
            } else {
                System.out.printf("UserID: %s\n", results.getString("userid"));
            }
        }
    }

    public static void main(String[] args){

        try (Connection connection = DBCPDataSource.getConnection()){
//            executeSelectAllFromSessionInfoWhere(connection, "abc");
//            String createSessionInfoTableSql = "CREATE TABLE session_info( session VARCHAR(100) PRIMARY KEY, userid VARCHAR(100) UNIQUE);";
//            PreparedStatement createSessionInfoTableStmt = connection.prepareStatement(createSessionInfoTableSql);
//            createSessionInfoTableStmt.executeUpdate();
//            ResultSet results = selectAllContactsStmt.executeUpdate();
//            executeInsert(connection, "Jose", 9985, "john", "2026-09-01");
//            executeSelectAndPrint(connection);
//            String insertContactSql = "INSERT INTO session_info (session) VALUES (?);";
//            PreparedStatement insertContactStmt = connection.prepareStatement(insertContactSql);
//            insertContactStmt.setString(1, "12345678abd");
//            insertContactStmt.executeUpdate();
            System.out.println("____________________");
//            executeInsertInUserInfo(connection, "nilimajha18aug@gmail.com", "nilima jha", "mnbvcxzasdfghjkl");
//            executeSelectFromUserInfoWhere(connection, "nilinajha0818@gmail.com");
//            executeSelectAllFromSessionInfoWhere(connection, "12345678abc");
            System.out.println("____________________");
//            executeSelectAndPrintForSession(connection);

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
