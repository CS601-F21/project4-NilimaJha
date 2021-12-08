package utils;

/**
 * A class to store the properties of the JSON configuration file.
 * @author nilimajha
 */
public class JDBCConfig {

    private String database;
    private String username;
    private String password;

    /**
     * Config class constructor.
     * @param database
     * @param username
     * @param password
     */
    public JDBCConfig(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for class attribute named database.
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Getter for class attribute named username.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for class attribute named password.
     * @return
     */
    public String getPassword() {
        return password;
    }
}
