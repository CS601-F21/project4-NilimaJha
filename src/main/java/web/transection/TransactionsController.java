package web.transection;

import jdbc.JDBCConnectionPool;
import model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import utils.Utilities;
import web.login.LoginServerConstants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jdbc.JDBCEventTableOperations.usersUpcomingTicketsWithEventInfo;
import static jdbc.JDBCEventTableOperations.findEventByEventIdFromEventsTable;
import static jdbc.JDBCTicketsTableOperations.executeInsertIntoTickets;
import static jdbc.JDBCUserTableOperations.findUserFromUserInfoByEmailId;


/**
 * Controller class that handles requests related to transaction like.
 * but tickets
 * Show users all transactions
 * Process transfer of tickets.
 *
 * contains method that handles request with path
 * - /buyEventTicket/{eventId}
 * - /event/{eventId}/ticketCount/
 * - /user/tickets
 * - /user/transaction
 *
 * @author nilimajha
 */
@Controller
public class TransactionsController {

    /**
     * handles GET request with path /buyEventTicket/{eventId}
     * It first validates the user and
     * if the user's profile is completed then
     * it returns form from where ticket information to be bought
     * will be provided be user.
     *
     * @param req
     * @param eventId
     * @param model
     * @return
     */
    @GetMapping("/buyEventTicket/{eventId}")
    protected String buyTicketForm(HttpServletRequest req, @PathVariable("eventId") int eventId, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            System.out.println("eventId: "+ eventId);
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
                Event event = new Event();
                try {
                    event = findEventByEventIdFromEventsTable(eventId);

                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                Tickets tickets = new Tickets();
                tickets.setEventId(event.getEventId());
                tickets.setTicketOwnerId(emailId);
                model.addAttribute("event", event);
                model.addAttribute("tickets", tickets);
                System.out.println("Inside updateProfileShoeMethod method.  Model attribute set");
                return "buyTicketForm";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles POST request with path /event/{eventId}/ticketCount/
     * It first validates the user and
     * if the user's profile is completed then
     * it will process user request for tickets by updating database.
     *
     * @param req
     * @param eventId
     * @param tickets
     * @param model
     * @return
     */
    @PostMapping("/event/{eventId}/ticketCount/")
    protected String buyTicket(HttpServletRequest req, @PathVariable("eventId") int eventId, @ModelAttribute("tickets") Tickets tickets, Model model) {
        System.out.println("Inside /buyTicket Post");
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println("Inside /buyTicket post. Getting email id from session: "+ emailId);
        if (emailId != null) {
            System.out.println("session is not null");
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
                boolean insertSuccess = false;
                tickets.setTicketOwnerId(emailId);
                tickets.setEventId(eventId);
                try {
                    insertSuccess = executeInsertIntoTickets(tickets);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                String title;
                String message;
                if (insertSuccess) {
                    title = "Successful";
                    message = "Ticket Purchase Successful.";
                } else {
                    title = "Unsuccessful";
                    message = "Ticket Purchase was not Successful. Please retry.";
                }
                model.addAttribute("title", title);
                model.addAttribute("message", message);
                return "ticketPurchaseResponse";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET request with path /user/tickets
     * It first validates the user and
     * it returns a page with all the users tickets on it
     * from where user can also transfer tickets to other user.
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping("/user/tickets")
    protected String showAllTickets(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                User user = null;
                List<UserUpcomingEvent> userUpcomingEventList = null;
                TicketTransfer ticketTransfer = new TicketTransfer();
                try {
                    user = findUserFromUserInfoByEmailId(emailId);
                    userUpcomingEventList = usersUpcomingTicketsWithEventInfo(user.getUserEmailId());
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                String tableCaption = "Your all active tickets.";
                String title = "Ticket Transfer Page";
                model.addAttribute("title", title);
                model.addAttribute("tableCaption", tableCaption);
                model.addAttribute("user", user);
                model.addAttribute("upcomingEventList", userUpcomingEventList);
                model.addAttribute("ticketTransfer", ticketTransfer);

                return "transferTicketsPage";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles POST request with path /user/tickets
     * It first validates the user then
     * it processes users ticket transfer request.
     *
     * @param req
     * @param ticketTransfer
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/user/tickets")
    protected String transferTickets(HttpServletRequest req, @Valid @ModelAttribute("ticketTransfer") TicketTransfer ticketTransfer, BindingResult bindingResult, Model model) {
        System.out.println("1.....Inside Transfer Ticket Post.");
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println("2.....Inside Transfer Ticket Post.");
        if (emailId != null) {
            // already authed, no need to log in
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
//                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "completeProfile";
            } else {
                ticketTransfer.setTransferor(emailId);
                boolean transferSuccessful = false;
                try {
                    if (bindingResult.hasErrors() || ticketTransfer.getTransferee().equals(ticketTransfer.getTransferor())){
                        System.out.println("*******HAS ERRORS*******");
                        User user = findUserFromUserInfoByEmailId(ticketTransfer.getTransferee());
                        System.out.println("*******HAS ERRORS*******222");
                        List<UserUpcomingEvent> userUpcomingEventList = null;
                        userUpcomingEventList = usersUpcomingTicketsWithEventInfo(ticketTransfer.getTransferor());
                        System.out.println("*******HAS ERRORS*******333");
                        String tableCaption = "Your all active tickets.";
                        String title = "Ticket Transfer Page";
                        model.addAttribute("title", title);
                        model.addAttribute("tableCaption", tableCaption);
                        model.addAttribute("user", user);
                        model.addAttribute("upcomingEventList", userUpcomingEventList);
                        return "transferTicketsPage";
                    } else {
                        transferSuccessful = JDBCConnectionPool.updateTicketsAndTransactionTableForTransfer(ticketTransfer);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                if (transferSuccessful) {
                    model.addAttribute("pageHeader", LoginServerConstants.TICKET_TRANSFER_SUCCESS_PAGE_HEADER);
                    model.addAttribute("message", LoginServerConstants.TICKET_TRANSFER_SUCCESS_PAGE_MESSAGE);
                    return "successfulPage";
                } else {
                    model.addAttribute("pageHeader", LoginServerConstants.TICKET_TRANSFER_UNSUCCESSFUL_PAGE_HEADER);
                    model.addAttribute("message", LoginServerConstants.TICKET_TRANSFER_UNSUCCESSFUL_PAGE_MESSAGE);
                    return "unsuccessfulPage";
                }
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET request with path /user/transaction
     * It first validates the user then returns a page containing
     * that user's all transaction information.
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping("/user/transaction")
    protected String showAllTransaction(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            //extract transaction info
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("user", user);
                return "completeProfile";
            } else {
                List<Transaction> transactionList = new ArrayList<>();
                try {
                    transactionList = JDBCConnectionPool.allTransactionOfUser(emailId);
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                String title = "Transaction List.";
                String tableCaption = "Your Transaction List.";
                model.addAttribute("title", title);
                model.addAttribute("tableCaption", tableCaption);
                model.addAttribute("transactionList", transactionList);
                return "viewTransactions";
            }
        } else {
            return "redirect:/";
        }
    }

}
