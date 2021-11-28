package web.login;

import jdbc.DBCPDataSource;
import jdbc.JDBCConnectionPool;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import web.HttpServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static jdbc.JDBCConnectionPool.updateUserInfoTable;

//import static jdbc.JDBCConnectionPool.executeSelectFromUserInfoWhere;

@Controller
public class LoginController {

    @GetMapping("/")
    protected String login (HttpServletRequest req, Model model) {
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
//            try (Connection connection = DBCPDataSource.getConnection()){
            try {
                user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
            } catch(SQLException e) {
                e.printStackTrace();
            }
            if (user.isCompleteProfile() == true) {
                model.addAttribute("userName", user.getUserName());
                return "home";
            } else {
                model.addAttribute("user", user);
                return "completeProfile";
            }
        } else {
            // retrieve the code provided by Slack
            String code = req.getParameter(LoginServerConstants.CODE_KEY);
            if (req.getParameter(LoginServerConstants.CODE_KEY) == null) {
                return "redirect:/";
            } else {
                // generate the url to use to exchange the code for a token using "/openid.connect.token method".
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
                    try {
                        User userFromDB = JDBCConnectionPool.findUserFromUserInfoByEmailId(user.getUserEmailId());
                        if (!userFromDB.isValid()) {
                            JDBCConnectionPool.executeInsertInUserInfo(user);
                            model.addAttribute("user", user);
                            return "completeProfile";
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

    @PostMapping("/completeProfile")
    protected String completeProfile(HttpServletRequest req, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println(">>> USERS EMAIL: " + emailId);
        if (bindingResult.hasErrors()){
            System.out.println("*******HAS ERRORS*******");
            return "completeProfile";
        }
        if (emailId != null) {
            // already authed, no need to log in
            try {
                JDBCConnectionPool.updateUserInfoTable(emailId, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "redirect:/home";
        } else {
            // delete session if created  before redirect
            System.out.println("NOT logged in");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    protected String logout(HttpServletRequest req) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, invalidating session and sending to the login page.
            System.out.println("Logging out...");
            req.getSession().invalidate();
            return "redirect:/";
        } else {
            // not authed.
            return "notLoggedIn";
        }
    }
}
