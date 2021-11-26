package web.login;

import jdbc.DBCPDataSource;
import jdbc.JDBCConnectionPool;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.HttpServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

//import static jdbc.JDBCConnectionPool.executeSelectFromUserInfoWhere;

@Controller
public class LoginController {

    @GetMapping("/")
    protected String login (HttpServletRequest req, Model model) throws IOException {
        // retrieve the ID of this session from request
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        String emailId = (String) req.getSession().getAttribute("emailId");
        if(emailId != null) {
            // already authenticated, no need to log in, redirecting to home.
            return "redirect:/home";
        } else {
            String state = sessionId;
            String nonce = LoginUtilities.generateNonce(state);
            // Generate url for request to Slack
            String url = LoginUtilities.generateSlackAuthorizeURL(HttpServer.loginWithSlackConfig.getClient_id(),
                    state,
                    nonce,
                    HttpServer.loginWithSlackConfig.getRedirect_url());

            model.addAttribute("url", url);
            return "login";
        }
    }

    @GetMapping("/home")
    protected String home(HttpServletRequest req, Model model) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            User user = null;
            try (Connection connection = DBCPDataSource.getConnection()){
                user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                model.addAttribute("userName", user.getUserName());
            } catch(SQLException e) {
                System.out.println(">>>>>++++++++++++++++++++++++++SQLException+++++++++++++++++++++++++++");
                e.printStackTrace();
            }
            return "home";
        } else {
            // retrieve the code provided by Slack
            String code = req.getParameter(LoginServerConstants.CODE_KEY);
            if (req.getParameter(LoginServerConstants.CODE_KEY) == null) {
                return "redirect:/";
            } else {
                // generate the url to use to exchange the code for a token:
                // After the user successfully grants your app permission to access their Slack profile,
                // they'll be redirected back to your service along with the typical code that signifies
                // a temporary access code. Exchange that code for a real access token using the
                // /openid.connect.token method.
                String url = LoginUtilities.generateSlackTokenURL(HttpServer.loginWithSlackConfig.getClient_id(),
                        HttpServer.loginWithSlackConfig.getClient_secret(), code,
                        HttpServer.loginWithSlackConfig.getRedirect_url());
                // Make the request to the token API
                String responseString = HTTPFetcher.doGet(url, null);
                Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);
                User user = LoginUtilities.verifyTokenResponse(response, sessionId);
                if (user == null) {
                    return "loginUnsuccessful";
                } else {
                    req.getSession().setAttribute("emailId", user.getUserEmailId());
                    System.out.println("Login Was Successful and emailId =" + user.getUserEmailId());
                    try {
                        User userFromDB = JDBCConnectionPool.findUserFromUserInfoByEmailId(user.getUserEmailId());
                        if (!userFromDB.isValid()) {
                            //add user data to db
                            JDBCConnectionPool.executeInsertInUserInfo(user);
                            model.addAttribute("userName", user.getUserName());
                        } else {
                            model.addAttribute("userName", userFromDB.getUserName());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return "home";
                }
            }
        }
    }

    @GetMapping("/logout")
    protected String logout(HttpServletRequest req) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println(emailId);
        if (emailId != null) {
            // already authed, invalidating session and sending to the login page.
            req.getSession().invalidate();
            return "redirect:/";
        } else {
            // not authed.
            return "notLoggedIn";
        }
    }
}
