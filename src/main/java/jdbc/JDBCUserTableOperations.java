package jdbc;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class with methods to perform operation on User table.
 * @author nilimajha
 */
public class JDBCUserTableOperations {

    /**
     * A method to insert
     * user_email_id,
     * user_name,
     * user_access_token,
     * complete_profile into usersInfo table in database.
     * @param user
     * @throws SQLException
     */
    public static void executeInsertInUserInfoTable(User user) throws SQLException {

        try (Connection connection = DBCPDataSource.getConnection()) {
            String insertUserInfoSql = "INSERT INTO usersInfo (user_email_id, user_name, user_access_token, complete_profile) VALUES (?, ?, ?, ?);";
            PreparedStatement insertUerInfoStmt = connection.prepareStatement(insertUserInfoSql);
            insertUerInfoStmt.setString(1, user.getUserEmailId());
            insertUerInfoStmt.setString(2, user.getUserName());
            insertUerInfoStmt.setString(3, user.getUserAccessToken());
            insertUerInfoStmt.setBoolean(4, false);
            insertUerInfoStmt.executeUpdate();
        }
    }

    /**
     * A method to get information of user with given emailId.
     * @throws SQLException
     */
    public static User findUserFromUserInfoByEmailId (String userEmailId) throws SQLException {

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
     * A method to update row of userInfo table where emailId matches the given emailId.
     * @throws SQLException
     */
    public static void updateUserInfoTable (String userEmailId, User user) throws SQLException {

        try (Connection connection = DBCPDataSource.getConnection()) {
            String updateUserInfoSql = "UPDATE usersInfo SET user_name = ?, phone_number = ?, date_of_birth = ?, complete_profile = ? WHERE user_email_id = ?;";
            PreparedStatement updateUserInfoSqlStm = connection.prepareStatement(updateUserInfoSql);
            updateUserInfoSqlStm.setString(1, user.getUserName());
            updateUserInfoSqlStm.setString(2, user.getPhone());
            updateUserInfoSqlStm.setString(3, user.getDateOfBirth());
            updateUserInfoSqlStm.setBoolean(4, true);
            updateUserInfoSqlStm.setString(5, userEmailId);
            int updateResult = updateUserInfoSqlStm.executeUpdate();
        }
    }


}
