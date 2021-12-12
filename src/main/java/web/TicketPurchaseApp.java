package web;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.LoginWithSlackConfig;
import web.login.LoginServerConstants;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * spring boot application
 * @author nilimajha
 */
@SpringBootApplication
public class TicketPurchaseApp {

    public static LoginWithSlackConfig loginWithSlackConfig;
    static {
        // read the client id and secret from a config file
        Gson gson = new Gson();
        try {
            loginWithSlackConfig = gson.fromJson(
                    new FileReader(LoginServerConstants.SLACK_CONFIG_FILE_NAME), LoginWithSlackConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * main method.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TicketPurchaseApp.class, args);
    }
}
