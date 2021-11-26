package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to store
 * @author nilimajha
 */
public class User {
    private String userEmailId;
    private String userName;
    private String userAccessToken;
    private String dateOfBirth;
    private long phone;

    public User() {
    }

    public User(String userEmailId, String userName, String userAccessToken) {
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.userAccessToken = userAccessToken;
    }

    public User(ResultSet rs) throws SQLException {
        if (rs.next()) {
            if (!rs.wasNull()) {
                this.userEmailId = rs.getString("user_email_id");
            }
            if (!rs.wasNull()) {
                this.userName = rs.getString("user_name");
            }
            if (!rs.wasNull()) {
                this.userAccessToken = rs.getString("user_access_token");
            }
            if (!rs.wasNull()) {
                this.dateOfBirth = rs.getString("date_of_birth");
            }
            if (!rs.wasNull()) {
                this.phone = rs.getLong("phone");
            }
        } else {
            System.out.println("result set was empty...");
        }
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public long getPhone() {
        return phone;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public boolean isValid() {
        if (this.userEmailId == null || this.userName == null || this.userAccessToken == null) {
            return false;
        } else {
            return true;
        }
    }
}
