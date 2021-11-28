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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static jdbc.JDBCConnectionPool.updateUserInfoTable;

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
            User user = null;
            try (Connection connection = DBCPDataSource.getConnection()) {
                user = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                model.addAttribute("userName", user.getUserName());
                model.addAttribute("userEmailId", user.getUserEmailId());
                model.addAttribute("dateOfBirth", user.getDateOfBirth());
                model.addAttribute("phoneNumber", user.getPhone());
                //extract total number of events created by the user from events table.
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "viewProfile";
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
            try {
                User userFromDB = JDBCConnectionPool.findUserFromUserInfoByEmailId(emailId);
                model.addAttribute("user", userFromDB);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            return "updateProfile";
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
            try {
                updateUserInfoTable(emailId, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "redirect:/viewProfile";
        } else {
            return "redirect:/";
        }
    }
}
