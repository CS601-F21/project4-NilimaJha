package web.userProfile;

import jdbc.DBCPDataSource;
import jdbc.JDBCConnectionPool;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import utils.Utilities;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static jdbc.JDBCConnectionPool.updateUserInfoTable;

/**
 * Controller class that handles request related to user like
 * view profile
 * update profile
 *
 * contains method that handles request with path
 * - /viewProfile
 * - /updateProfile
 * - /updateProfilePost
 * @author nilimajha
 */
@Controller
public class UserController {

    @GetMapping("/viewProfile")
    protected String viewProfile (HttpServletRequest req, Model model) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println(emailId);
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                User user = null;
                try (Connection connection = DBCPDataSource.getConnection()) {
                    user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                    int totalEventsCreated = JDBCConnectionPool.findEventsByOrganizerId(emailId).size();
                    model.addAttribute("user", user);
                    model.addAttribute("totalEventsCreated", totalEventsCreated);
                    //extract total number of events created by the user from events table.
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "viewProfile";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/updateProfile")
    protected String updateProfileShowForm(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                try {
                    User userFromDB = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                    model.addAttribute("user", userFromDB);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                return "updateProfile";
            }
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/updateProfilePost")
    protected String updateProfileSubmit(HttpServletRequest req, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (bindingResult.hasErrors()){
            return "updateProfile";
        }
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
//                User user = new User();
                try{
                    user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                try {
                    updateUserInfoTable(emailId, user);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "redirect:/viewProfile";
            }
        } else {
            return "redirect:/";
        }
    }
}
