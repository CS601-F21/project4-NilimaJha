package web.userProfile;

import jdbc.JDBCUserTableOperations;
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
import java.sql.SQLException;

import static jdbc.JDBCEventTableOperations.findEventsByOrganizerId;
import static jdbc.JDBCUserTableOperations.*;

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

    /**
     * handles GET request with path /viewProfile
     * It first checks if the user is logged in, then
     * if the user's profile is completed and
     * if the above two condition is correct then it shows user profile information.
     *
     * @param req
     * @param model
     * @return completeProfile page or viewProfile page or redirect path
     */
    @GetMapping("/viewProfile")
    protected String viewProfile (HttpServletRequest req, Model model) {
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
                    user = findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                User user = null;
                try {
                    user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                    int totalEventsCreated = findEventsByOrganizerId(emailId).size();
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

    /**
     * handles GET request with path /updateProfile
     * It first checks if the user is logged in, then
     * if the user's profile is completed and
     * if the above two condition is correct,
     * it will return a form from where user can update profile information.
     *
     * @param req
     * @param model
     * @return
     */
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
                    user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                try {
                    User userFromDB = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
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

    /**
     * handles POST request with path /updateProfilePost
     * It first checks if the user is logged in, then
     * if the user's profile is completed and
     * if the above two condition is correct,
     * it will perform update operation on user information in the table
     * and then redirect to view profile page.
     *
     * @param req
     * @param user
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/updateProfilePost")
    protected String updateProfileSubmit(HttpServletRequest req, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println(">>>>>>>>>>>>>>>>>>" + user.getDateOfBirth());
        if (bindingResult.hasErrors()){
            System.out.println("Binding has error, " + user.getDateOfBirth());
            return "updateProfile";
        }
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
                try{
                    user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                try {
                    JDBCUserTableOperations.updateUserInfoTable(emailId, user);
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
