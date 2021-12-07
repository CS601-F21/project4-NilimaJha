package model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * stores user related data to store into and extract from database table named userInfo.
 * It contains following attributes :-
 * userEmailId     : emailId of the user.
 * userAccessToken : access token of the user from slack.
 * userName        : name of user.
 * dateOfBirth     : date of birth of user.
 * phone           : phone number of user.
 * completeProfile : boolean variable to store value in the column complete_profile of userinfo table.
 *
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

    /**
     * Blank Constructor.
     */
    public User() {
    }

    /**
     * Constructor class to initialize
     * userEmailId, userName, userAccessToken
     * of the User class object.
     * @param userEmailId
     * @param userName
     * @param userAccessToken
     */
    public User(String userEmailId, String userName, String userAccessToken) {
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.userAccessToken = userAccessToken;
    }

    /**
     * Constructor to initialise User class object attributes with
     * ResultSet value.
     * @param rs
     * @throws SQLException
     */
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
                this.phone = rs.getString("phone_number");
            }
            if (!rs.wasNull()) {
                this.completeProfile = rs.getBoolean("complete_profile");
            }
        } else {
            System.out.println("result set was empty...");
        }
    }

    /**
     * getter for class attribute userEmailId.
     * @return userEmailId
     */
    public String getUserEmailId() {
        return userEmailId;
    }

    /**
     * getter for class attribute userName.
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * getter for class attribute userAccessToken.
     * @return userAccessToken
     */
    public String getUserAccessToken() {
        return userAccessToken;
    }

    /**
     * getter for class attribute dateOfBirth.
     * @return dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * getter for class attribute phone.
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setter for class attribute userEmailId.
     * @param userEmailId
     */
    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    /**
     * setter for class attribute userName.
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * setter for class attribute userAccessToken.
     * @param userAccessToken
     */
    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    /**
     * setter for class attribute dateOfBirth.
     * @param dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * setter for class attribute phone.
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * getter for class attribute completeProfile.
     * @return completeProfile
     */
    public boolean isCompleteProfile() {
        return completeProfile;
    }

    /**
     * setter for class attribute completeProfile.
     * @param completeProfile
     */
    public void setCompleteProfile(boolean completeProfile) {
        this.completeProfile = completeProfile;
    }

    /**
     * isValid() returns true if the user class object contains
     * values of userEmailId, userName and userAccessToken.
     * @return true or false
     */
    public boolean isValid() {
        if (this.userEmailId == null || this.userName == null || this.userAccessToken == null) {
            return false;
        } else {
            return true;
        }
    }
}
