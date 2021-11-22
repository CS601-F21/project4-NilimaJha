package utils;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * A utility class.
 */
public class Utilities {

    public static final String configFileName = "jdbcConfig.json";

    /**
     * Read in the configuration file.
     * @return Config object
     */
    public static JDBCConfig readJDBCConfig() {

        JDBCConfig jdbcConfig = null;
        Gson gson = new Gson();
        try {
            jdbcConfig = gson.fromJson(new FileReader(configFileName), JDBCConfig.class);
        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return jdbcConfig;
    }
}
