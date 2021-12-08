package utils;

/**
 * A class to store the properties of the JSON configuration file.
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
     * Return the database property.
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return the username property.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password property.
     * @return
     */
    public String getPassword() {
        return password;
    }
}
