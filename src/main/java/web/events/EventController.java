package web.events;

import jdbc.JDBCConnectionPool;
import model.Event;
import model.EventSearchKeyValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jdbc.JDBCConnectionPool.executeInsertIntoEvents;
import static jdbc.JDBCConnectionPool.updateUserInfoTable;

@Controller
public class EventController {

    @GetMapping("/createEvent")
    protected String updateProfileShowForm(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            Event event = new Event();
            model.addAttribute("event", event);
            System.out.println("Inside updateProfileShoeMethod method.  Model attribute set");
            return "createEventForm";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/createEventPost")
    protected String updateProfileSubmit(HttpServletRequest req, @Valid @ModelAttribute("event") Event event, BindingResult bindingResult, Model model) {
        System.out.printf("Inside /createEventPost");
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        System.out.println("Inside /createEventPost Getting emailid from session: "+ emailId);
        if (bindingResult.hasErrors()){
            System.out.println("Inside /createEventPost, binding has error. Returning same form");
            return "createEventForm";
        }
        System.out.println("Inside /createEventPost Checking session emailId ");
        if (emailId != null) {
            System.out.println("session is not null");
            // already authed, no need to log in
            event.setEventOrganizerId(emailId);
            System.out.println("New Event Name: " + event.getEventName());
            System.out.println("New Event creation date: " + event.getEventCreatedOn());
            try {
                executeInsertIntoEvents(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "successfulEventCreated";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/events")
    protected String allEvents(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            List<Event> eventList = null;
            try {
                eventList = JDBCConnectionPool.findEventsFromEventsTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String title = "List Of All Active Events...";
            String tableCaption = "Active Events";
            model.addAttribute("title", title);
            model.addAttribute("tableCaption", tableCaption);
            model.addAttribute("eventList", eventList);
            System.out.println("Inside allEvents method.  Model attribute set");
            return "viewAllEvents";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/event/{eventId}")
    protected String eventByEventId(HttpServletRequest req, @PathVariable("eventId") int eventId, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            System.out.println("eventId: "+ eventId);
            Event event = new Event();
            // already authed, no need to log in
            try {
                event = JDBCConnectionPool.findEventByEventIdFromEventsTable(eventId);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            model.addAttribute("event", event);
            System.out.println("Inside events method.  Model attribute set");
            return "viewEvent";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/eventsBy")
    protected String eventBy(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            EventSearchKeyValue eventSearchKeyValue = new EventSearchKeyValue();
            model.addAttribute("eventSearchKeyValue", eventSearchKeyValue);
            return "findEventForm";
//            System.out.println("eventId: "+ eventId);
//            Event event = new Event();
            // already authed, no need to log in
//            try {
//                event = JDBCConnectionPool.findEventByEventIdFromEventsTable(eventId);
//            } catch (SQLException throwable) {
//                throwable.printStackTrace();
//            }

//            System.out.println("Inside events method.  Model attribute set");
//            return "viewEvent";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/eventsByResult")
    protected String findEventBy(HttpServletRequest req, @ModelAttribute("eventSearchKeyValue") EventSearchKeyValue eventSearchKeyValue, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            List<Event> eventList = new ArrayList<>();
            String searchCategory = eventSearchKeyValue.getSearchCategory();
            String searchType = eventSearchKeyValue.getSearchType();
            String searchTerm = eventSearchKeyValue.getSearchTerm();
            try {
                eventList = JDBCConnectionPool.findEventsByPhraseFromEventsTable(searchCategory, searchType, searchTerm);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String title = "List Of Active Events...";
            String tableCaption = "Search Result Where  " + searchCategory + " " + searchType + " " + searchTerm;
            model.addAttribute("title", title);
            model.addAttribute("tableCaption", tableCaption);
            model.addAttribute("eventList", eventList);
            return "viewAllEvents";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/events/{userId}")
    protected String findYourEvents(HttpServletRequest req, @PathVariable("userId") String userId, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            // already authed, no need to log in
            List<Event> eventList = null;
            try {
                eventList = JDBCConnectionPool.findEventsByEventCreatorFromEventsTable(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String title = "List Of Events...";
            String tableCaption = "All Events created by " + userId + "...";
            model.addAttribute("title", title);
            model.addAttribute("tableCaption", tableCaption);
            model.addAttribute("eventList", eventList);
            System.out.println("Inside allEvents method.  Model attribute set");
            return "viewAllEvents";
        } else {
            return "redirect:/";
        }
    }

}
