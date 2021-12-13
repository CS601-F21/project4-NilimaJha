package jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import utils.JDBCConfig;
import utils.Utilities;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Example of using Apache DBCP ConnectionPool.
 * Taken from https://www.baeldung.com/java-connection-pooling
 */
public class DBCPDataSource {
    // Apache commons connection pool implementation
    private static BasicDataSource ds = new BasicDataSource();

    // This code inside the static block is executed only once: the first time the class is loaded into memory.
    // -- https://www.geeksforgeeks.org/static-blocks-in-java/
    static {
        JDBCConfig config = Utilities.readJDBCConfig();
        if(config == null) {
            System.exit(1);
        }
        ds.setUrl("jdbc:mysql://localhost:3308/" + config.getDatabase());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
    }

    /**
     * Return a Connection from the pool.
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){ }
}
