package web;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.LoginWithSlackConfig;
import web.login.LoginServerConstants;

import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
public class HttpServer {

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

    public static void main(String[] args) {
        SpringApplication.run(HttpServer.class, args);
    }
}
