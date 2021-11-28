package model;

import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to store
 * @author nilimajha
 */

public class User {
    private String userEmailId;
    private String userAccessToken;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String dateOfBirth;

    @NotEmpty
    @Size(max = 10, min = 10, message = "Mobile number should be of 10 digits")
    @Pattern(regexp = "[0-9][0-9]{9}", message = "Mobile number is invalid!!")
    private String phone;
    private boolean completeProfile;

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
                System.out.println("#################################################");
                this.dateOfBirth = rs.getString("date_of_birth");
                System.out.println("###############################################"+dateOfBirth);
            }
            if (!rs.wasNull()) {
                this.phone = rs.getString("phone_number");
//                this.phone = rs.getLong("phone");
            }
            if (!rs.wasNull()) {
                this.completeProfile = rs.getBoolean("complete_profile");
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

    public String getPhone() {
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isCompleteProfile() {
        return completeProfile;
    }

    public void setCompleteProfile(boolean completeProfile) {
        this.completeProfile = completeProfile;
    }

    public boolean isValid() {
        if (this.userEmailId == null || this.userName == null || this.userAccessToken == null) {
            return false;
        } else {
            return true;
        }
    }
}
