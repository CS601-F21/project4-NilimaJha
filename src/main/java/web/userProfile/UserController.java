package web.userProfile;

import jdbc.DBCPDataSource;
import jdbc.JDBCConnectionPool;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
                System.out.println(">>>>>++++++++++++++++++++++++++SQLException+++++++++++++++++++++++++++");
                e.printStackTrace();
            }
            return "viewProfile";
        } else {
            System.out.println("NOT logged in");
            return "redirect:/";
//            return "notLoggedIn";
        }
    }

}
