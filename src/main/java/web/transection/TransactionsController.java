package web.transection;

import jdbc.JDBCConnectionPool;
import model.Event;
import model.Tickets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.sql.SQLException;

import static jdbc.JDBCConnectionPool.executeInsertIntoEvents;

@Controller
public class TransactionsController {

    @GetMapping("/buyEventTicket/{eventId}")
    protected String buyTicketForm(HttpServletRequest req, @PathVariable("eventId") int eventId, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            System.out.println("eventId: "+ eventId);
            // already authed, no need to log in
            Event event = new Event();
            try {
//                event = JDBCConnectionPool.findEventByEventIdFromEventsTable(Integer.parseInt(eventId));
                event = JDBCConnectionPool.findEventByEventIdFromEventsTable(eventId);

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            Tickets tickets = new Tickets();
            tickets.setEventId(event.getEventId());
            tickets.setTicketOwnerId(emailId);
//            System.out.println("##############################################");
//            System.out.println("Event Id            : " + event.getEventId());
//            System.out.println("Event Name          : " + event.getEventName());
//            System.out.println("Event Organizer Id  : " + event.getEventOrganizerId());
//            System.out.println("Event Organizer Name: " + event.getEventOrganizerName());
//            System.out.println("Event Date          : " + event.getEventDate());
//            System.out.println("Event Created On    : " + event.getEventCreatedOn());
//            System.out.println("___________ ____________ _____________________________");
//            System.out.println("Ticket Id              :" + tickets.getTicketId());
//            System.out.println("Ticket Event Id        :" + tickets.getEventId());
//            System.out.println("Ticket Owner Id        :" + tickets.getTicketOwnerId());
//            System.out.println("Ticket Purchase Date   :" + tickets.getPurchaseDate());
//            System.out.println("Ticket selected number :" + tickets.getTotalTicketsSelected());
            model.addAttribute("event", event);
            model.addAttribute("tickets", tickets);
//            redirectAttributes.addFlashAttribute("event", event);
            System.out.println("Inside updateProfileShoeMethod method.  Model attribute set");
            return "buyTicketForm";
        } else {
            return "redirect:/";
        }
    }

//    @PostMapping("/buyTicket")
//    protected String buyTicket(HttpServletRequest req, @ModelAttribute("event") Event event, @ModelAttribute("tickets") Tickets tickets, Model model) {
//        System.out.println("Inside /buyTicket Post");
//        // retrieve the ID of this session
//        String sessionId = req.getSession(true).getId();
//        // retrieve userEmailId associated to this session.
//        String emailId = (String) req.getSession().getAttribute("emailId");
//        System.out.println("Inside /buyTicket post. Getting email id from session: "+ emailId);
////        if (bindingResult.hasErrors()){
//////            System.out.println("Inside /createEventPost, binding has error. Returning same form");
////            return "createEventForm";
////        }
////        System.out.println("Inside /createEventPost Checking session emailId ");
//        if (emailId != null) {
//            System.out.println("session is not null");
//            // already authed, no need to log in
//            System.out.println("________________________________________________");
//            System.out.println("Ticket Id              :" + tickets.getTicketId());
//            System.out.println("Ticket Event Id        :" + tickets.getEventId());
//            System.out.println("Ticket Owner Id        :" + tickets.getTicketOwnerId());
//            System.out.println("Ticket Purchase Date   :" + tickets.getPurchaseDate());
//            System.out.println("Ticket selected number :" + tickets.getTotalTicketsSelected());
//
//
//            System.out.println("_____________ ______________ ________________ ___________");
//            System.out.println("\nTicket Id            :" + tickets.getTicketId());
//            System.out.println("Ticket Event Id      :" + tickets.getTicketId());
//            System.out.println("Ticket Owner Id      :" + tickets.getTicketId());
//            System.out.println("Ticket Purchase Date :" + tickets.getTicketId());
////            event.setEventOrganizerId(emailId);
////            System.out.println("New Event Name: " + event.getEventName());
////            System.out.println("New Event creation date: " + event.getEventCreatedOn());
////            try {
////                executeInsertIntoEvents(event);
////            } catch (SQLException e) {
////                e.printStackTrace();
////            }
//            return "successfulEventCreated";
//        } else {
//            return "redirect:/";
//        }
//    }

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
            boolean insertSuccess = false;
            tickets.setTicketOwnerId(emailId);
            tickets.setEventId(eventId);
            System.out.println("______________ _______________________ ___________________________ __________________________________");
            System.out.println("Event Id : " +tickets.getEventId() +", OwnerId : " + tickets.getTicketOwnerId() + ", ticket Id : " + tickets.getTicketId() + ", Total tickets :" + tickets.getTotalTicketsSelected());
            System.out.println("______________ _______________________ ___________________________ __________________________________");
            try {
                 insertSuccess = JDBCConnectionPool.executeInsertIntoTickets(tickets);
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
        } else {
            return "redirect:/";
        }
    }


}
