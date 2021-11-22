package utils;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputFilter;

public class Utilities {

    public static final String configFileName = "config.json";

    /**
     * Read in the configuration file.
     * @return
     */
    public static Config readConfig() {

        Config config = null;
        Gson gson = new Gson();
        try {
            config = gson.fromJson(new FileReader(configFileName), Config.class);
        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return config;
    }
}
