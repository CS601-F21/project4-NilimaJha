package web.events;

import jdbc.JDBCConnectionPool;
import jdbc.JDBCUserTableOperations;
import model.Event;
import model.EventSearchKeyValue;
import model.User;
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

import static jdbc.JDBCEventTableOperations.*;

/**
 * Controller class that handles request related to events like
 * create event
 * view all events
 * view events
 * view specific event by given event Id.
 * search events by specific category.
 *
 * contains method that handles request with path
 * - /createEvent
 * - /createEventPost
 * - /events
 * - /event/{eventId}
 * - /eventsBy
 *
 * @author nilimajha
 */
@Controller
public class EventController {

    /**
     * handles GET on path /newEventForm
     * validates user then returns from page where
     * user will entered required information of the events to be created.
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping("/createEvent")
    protected String createEventForm(HttpServletRequest req, Model model) {
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
//                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "completeProfile";
            } else {
                Event event = new Event();
                model.addAttribute("event", event);
                System.out.println("Inside updateProfileShoeMethod method.  Model attribute set");
                return "createEventForm";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles POST method on path /createEventPost
     * validates user then if all the information provided in the form is valid
     * new event creation process is performed.
     *
     * @param req
     * @param event
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/createEventPost")
    protected String createEvent(HttpServletRequest req,
                                 @Valid @ModelAttribute("event") Event event,
                                 BindingResult bindingResult,
                                 Model model) {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        // Validating data provided by user in the form.
        if (bindingResult.hasErrors()){
            return "createEventForm";
        }
        if (emailId != null) {
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
                event.setEventOrganizerId(emailId);
                try {
                    executeInsertIntoEvents(event);
                    model.addAttribute("pageHeader", LoginServerConstants.CREATE_EVENT_SUCCESS_PAGE_HEADER);
                    model.addAttribute("message", LoginServerConstants.CREATE_EVENT_SUCCESS_PAGE_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "successfulPage";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET on path /events
     * validates user and then
     * returns a web page containing information of all the upcoming events.
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping("/events")
    protected String allEvents(HttpServletRequest req, Model model) {
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
                List<Event> eventList = null;
                try {
                    eventList = findEventsFromEventsTable();
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
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET on path /event/{eventId}
     * validates user and then returns
     * specific event's details in a html page.
     *
     * @param req
     * @param eventId
     * @param model
     * @return
     */
    @GetMapping("/event/{eventId}")
    protected String eventByEventId(HttpServletRequest req, @PathVariable("eventId") int eventId, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            System.out.println("eventId: "+ eventId);
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                model.addAttribute("user", user);
//                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "completeProfile";
            } else {
                Event event = new Event();
                // already authed, no need to log in
                try {
                    event = findEventByEventIdFromEventsTable(eventId);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                model.addAttribute("event", event);
                System.out.println("Inside events method.  Model attribute set");
                return "viewEvent";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET on path /eventSearchForm
     * validates user, and then
     * returns a html page containing form for find event.
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping("/eventsBy")
    protected String eventSearchForm(HttpServletRequest req, Model model) {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // retrieve userEmailId associated to this session.
        String emailId = (String) req.getSession().getAttribute("emailId");
        if (emailId != null) {
            if (!Utilities.isUserProfileComplete(emailId)) {
                User user = new User();
                try{
                    user = JDBCUserTableOperations.findUserFromUserInfoByEmailId(emailId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                model.addAttribute("user", user);
//                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "completeProfile";
            } else {
                EventSearchKeyValue eventSearchKeyValue = new EventSearchKeyValue();
                model.addAttribute("eventSearchKeyValue", eventSearchKeyValue);
                return "findEventForm";
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * (/eventSearchResult)
     * handles GET request on path /eventsByResult
     * validates user and then returns search result in a html page.
     *
     * @param req
     * @param eventSearchKeyValue
     * @param model
     * @return
     */
    @GetMapping("/eventsByResult")
    protected String findEventBy(HttpServletRequest req, @ModelAttribute("eventSearchKeyValue") EventSearchKeyValue eventSearchKeyValue, Model model) {
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
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                model.addAttribute("user", user);
//                model.addAttribute("upcomingEventList", userUpcomingEventList);
                return "completeProfile";
            } else {
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
            }
        } else {
            return "redirect:/";
        }
    }

    /**
     * handles GET request on path /events/{userId}
     * validates user and then returns that users all events.i.e.
     * the events created by that user.
     * @param req
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/events/{userId}")
    protected String findYourEvents(HttpServletRequest req, @PathVariable("userId") String userId, Model model) {
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
                List<Event> eventList = null;
                try {
                    eventList = findEventsByOrganizerId(userId);
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
            }
        } else {
            return "redirect:/";
        }
    }

}
