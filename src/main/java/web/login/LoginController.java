import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import utils.LoginWithSlackConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    @GetMapping("/")
    protected void login (HttpServletRequest req, HttpServletResponse res) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute("client_info_key");
        if(clientInfoObj != null) {
            // already authed, no need to log in
//            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            String response = "<!DOCTYPE html>\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "  <title>Log in with Slack</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n"+
                    "<h1>You have already been authenticated</h1>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
            res.getWriter().println(response);
//            resp.getWriter().println("<h1>You have already been authenticated</h1>");
//            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            return;
        }
        // retrieve the config info from the context
        LoginWithSlackConfig config = (LoginWithSlackConfig) req.getServletContext().getAttribute("config_key");
        String state = sessionId;
        String nonce = LoginUtilities.generateNonce(state);
        // Generate url for request to Slack
        String url = LoginUtilities.generateSlackAuthorizeURL(config.getClient_id(),
                state,
                nonce,
                config.getRedirect_url());

        resp.setStatus(HttpStatus.OK_200);
        PrintWriter writer = resp.getWriter();
        writer.println(LoginServerConstants.PAGE_HEADER);
        writer.println("<h1>Welcome to the Login with Slack Demo Application</h1>");
        writer.println("<a href=\""+url+"\"><img src=\"" + LoginServerConstants.BUTTON_URL +"\"/></a>");
        writer.println(LoginServerConstants.PAGE_FOOTER);

    }

}
