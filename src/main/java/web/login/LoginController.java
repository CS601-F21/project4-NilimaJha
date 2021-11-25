package web.login;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.HttpServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/")
    protected String login (HttpServletRequest req, HttpServletResponse res, Model model) throws IOException {
        // retrieve the ID of this session from request
        String sessionId = req.getSession(true).getId();
        System.out.println("sessionID in /: " + sessionId);

        // determine whether the user is already authenticated
        String name = (String) req.getSession().getAttribute("name");
        System.out.println(name);
        if(name != null) {
            // already authed, no need to log in
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
            return response;
        } else {
            // retrieve the config info from the context
            String state = sessionId;
            String nonce = LoginUtilities.generateNonce(state);
            // Generate url for request to Slack
            String url = LoginUtilities.generateSlackAuthorizeURL(HttpServer.loginWithSlackConfig.getClient_id(),
                    state,
                    nonce,
                    HttpServer.loginWithSlackConfig.getRedirect_url());

            model.addAttribute("url", url);
            System.out.println("URL : " +url);
            System.out.println("Response sent for path /");
            return "login";
        }
    }

    @GetMapping("/home")
    protected String home(HttpServletRequest req, HttpServletResponse res, Model model) throws IOException {
        System.out.println("Response received from slack.");
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        System.out.println("SessionId in /home: " + sessionId);

        // determine whether the user is already authenticated
//        System.out.println("---------------------------------");
//        System.out.println((String) req.getSession().getId());
//        System.out.println("---------------------------------");

//        Enumeration<String> e = req.getSession().getAttributeNames();
//        for (String key : Collections.list(e))
//            System.out.println(key);

//        while(e.hasMoreElements()){
//            System.out.println(e.nextElement());
//            String param = (String) e.nextElement();
//        }

//        System.out.println();
//        System.out.println("---------------------------------");


        String name = (String) req.getSession().getAttribute("name");
        System.out.println(name);
        if (name != null) {
            // already authed, no need to log in
            return "home";
        } else {
            // retrieve the code provided by Slack
            String code = req.getParameter(LoginServerConstants.CODE_KEY);
            // generate the url to use to exchange the code for a token:
            // After the user successfully grants your app permission to access their Slack profile,
            // they'll be redirected back to your service along with the typical code that signifies
            // a temporary access code. Exchange that code for a real access token using the
            // /openid.connect.token method.
            String url = LoginUtilities.generateSlackTokenURL(HttpServer.loginWithSlackConfig.getClient_id(),
                    HttpServer.loginWithSlackConfig.getClient_secret(), code,
                    HttpServer.loginWithSlackConfig.getRedirect_url());
            System.out.println("1.Url created");
            // Make the request to the token API
            String responseString = HTTPFetcher.doGet(url, null);
            Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);
            for (Map.Entry<String, Object> entry : response.entrySet()) {
                System.out.println(">>Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }
            ClientInfo clientInfo = LoginUtilities.verifyTokenResponse(response, sessionId);
            //add user data to db
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Name :" +clientInfo.getName());
            System.out.println("email :" +clientInfo.getEmail());
            System.out.println("access-token :" +clientInfo.getAccessToken());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
            if(clientInfo == null) {
                return "loginunsuccessful";
            } else {
                req.getSession().setAttribute("name",clientInfo.getEmail());
                //if clientInfo.getEmail() exist in usersInfo table in db
                    //=>get user_name of the user with user_email_id = clientInfo.getEmail()
                    // => model.addAttribute("name", clientInfo.getName());
                //else add the user info in users_info table in database
                model.addAttribute("name", clientInfo.getName());
                return "home";
            }
        }
    }

    @GetMapping("/logout")
    protected String logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        String name = (String) req.getSession().getAttribute("name");
        System.out.println(name);
        if (name != null) {
            // already authed, no need to log in
            req.getSession().invalidate();
            System.out.println("Invalidate session.");
            return "redirect:/";
        } else {
            return "notloggedin";
        }
    }
}
