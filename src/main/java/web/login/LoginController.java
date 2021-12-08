package web.login;

import jdbc.JDBCUserTableOperations;
import model.User;
import model.UserUpcomingEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import utils.Utilities;
import web.HttpServer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static jdbc.JDBCEventTableOperations.usersUpcomingTicketsWithEventInfo;


/**
 * Controller class that handles requests related to-
 * login
 * create account
 * complete profile
 * logout
 *
 * contains method that handles request with path
 * - /
 * - /home
 * - /completeProfile
 * - /logout
 *
 * @author nilimajha
 */
@Controller
public class LoginController {

    /**
     * handles GET request with path /
     * It checks if the user is already logged in
     * and sends login page to user.
     *
     * @param req
     * @param model
     * @return
     */
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

    /**
     * handles GET request with path /home
     * It first checks if the user is logged in, then
     * returns home page.
     *
     * @param req
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/home")
    protected String home(HttpServletRequest req, Model model) throws IOException {
        System.out.println("Inside /home");
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            User user = null;
            List<UserUpcomingEvent> userUpcomingEventList = null;
            try {
                user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                userUpcomingEventList = usersUpcomingTicketsWithEventInfo(user.getUserEmailId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
            if (!Utilities.isUserProfileComplete(emailId)) {
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                String tableCaption = "Upcoming Event and your total tickets of that event.";
                model.addAttribute("tableCaption", tableCaption);
                model.addAttribute("user", user);
                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "home";
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
                        User userFromDB = JDBCUserTableOperations.findUserFromUserInfoByEmailId(user.getUserEmailId());
                        if (!userFromDB.isValid()) {
                            JDBCUserTableOperations.executeInsertInUserInfoTable(user);
                            model.addAttribute("user", user);
                            return "completeProfile";
                        } else {
                            List<UserUpcomingEvent> userUpcomingEventList = null;
                            try {
                                userUpcomingEventList = usersUpcomingTicketsWithEventInfo(user.getUserEmailId());
                            } catch(SQLException e) {
                                e.printStackTrace();
                            }
                            String tableCaption = "Upcoming Event and your total tickets of that event.";
                            model.addAttribute("tableCaption", tableCaption);
                            model.addAttribute("user", userFromDB);
                            model.addAttribute("upcomingEventList", userUpcomingEventList);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return "home";
                }
            }
        }
    }

    /**
     * handles POST request with path /completeProfile
     * It first checks if the user is logged in, then
     * it updates user profile.
     *
     * @param req
     * @param user
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/completeProfile")
    protected String completeProfile(HttpServletRequest req,
                                     @Valid @ModelAttribute("user") User user,
                                     BindingResult bindingResult,
                                     Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println(">>> USERS EMAIL: " + emailId);
        if (emailId != null) {
            // already authed, no need to log in
            if (bindingResult.hasErrors()){
                System.out.println("*******HAS ERRORS*******");
                return "completeProfile";
            } else {
                try {
                    JDBCUserTableOperations.updateUserInfoTable(emailId, user);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "redirect:/home";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET request with path /logout
     * checks if the user is logged in,
     * if logged in then process logout by invalidating session.
     *
     * @param req
     * @return
     */
    @GetMapping("/logout")
    protected String logout(HttpServletRequest req) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, invalidating session and sending to the login page.
            System.out.println("Logging out...");
            req.getSession().invalidate();
            return "redirect:/";
        } else {
            return "notLoggedIn";
        }
    }
}
