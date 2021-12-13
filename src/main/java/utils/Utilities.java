package utils;

import com.google.gson.Gson;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jdbc.JDBCUserTableOperations.findUserFromUserInfoByEmailId;

/**
 * A utility class.
 */
public class Utilities {

    /**
     * Read in the JDBC configuration file and creates JDBCConfig object.
     * @return JdbcConfig object
     */
    public static JDBCConfig readJDBCConfig() {

        JDBCConfig jdbcConfig = null;
        Gson gson = new Gson();
        //validate file exist at the location.
        try {
            jdbcConfig = gson.fromJson(new FileReader(Constants.JDBC_CONFIG_FILE_NAME), JDBCConfig.class);
        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return jdbcConfig;
    }

    /**
     * method to check if the user's profile is complete of not.
     * @param emailId
     * @return true or false
     */
    public static boolean isUserProfileComplete(String emailId) {
        User user = null;
        try {
            user = findUserFromUserInfoByEmailId(emailId);
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (user.isCompleteProfile()) {
            return true;
        }
        return false;
    }

    /**
     * method to check if the date in the string format is current date or past date.
     * @param date
     * @return true or false.
     */
    public static boolean presentOrFutureDate(String date) {
        LocalDateTime inputDateTime = LocalDateTime.parse(date+"T00:00:00.000");
        LocalDateTime currentDateTime = LocalDateTime.now();

        // comparing date provided by user with the current date.
        if (inputDateTime.isBefore(currentDateTime)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * method to return current date in specified format and in the form of string.
     * @return
     */
    public static String currentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * method to reformat string by
     * removing '_' from string and
     * making it separate words in the string.
     * @param searchCategory
     * @return
     */
    public static String searchCategory(String searchCategory) {
        String[] searchCategoryList = searchCategory.split("_");
        String searchCategoryText = "";
        for (String s : searchCategoryList) {
            searchCategoryText += s;
            searchCategoryText += " ";
        }
        return searchCategoryText;
    }
}
