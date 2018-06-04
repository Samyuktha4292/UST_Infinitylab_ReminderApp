package com.ust.infinityLabs.reminderApp.Controller;

import com.ust.infinityLabs.reminderApp.Model.CalanderEventDAO;
import com.ust.infinityLabs.reminderApp.Model.EventDAO;
import com.ust.infinityLabs.reminderApp.Model.UserDAO;
import com.ust.infinityLabs.reminderApp.Service.LoginService;
import com.ust.infinityLabs.reminderApp.api.GCalender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class HomeController {

    @Autowired
    private LoginService loginService;

    @Autowired
    GCalender googleCalender;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);


    @RequestMapping("/home")
    public String home(){
        LOGGER.info("Reminder App Home page reached");
        return "index";
    }

    @RequestMapping(value="login", method=RequestMethod.POST)
    public ResponseEntity<Boolean> loginUser(@RequestBody  UserDAO userDAO){
        LOGGER.info("Reached the login");
        LOGGER.info("User name logged is"+ userDAO.getUsername());
        loginService.isUserValid(userDAO);
        if(userDAO.isValidUser())

            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        else{
            return new ResponseEntity<Boolean>(true, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value ="addEvent" ,method = RequestMethod.POST)
    public ResponseEntity addEvent(@RequestBody  EventDAO eventdao){
        LOGGER.info("Reached add Event");
        //loginService.addEvent();

        return new ResponseEntity(true, HttpStatus.OK);
    }

    @RequestMapping(value ="viewEvent" ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> viewEvent(){
        LOGGER.info("Reached view Event");
        List<Map<String, Object>> eventsList= loginService.viewEvent();
        return eventsList;
        //return new ResponseEntity(eventsList, HttpStatus.OK);
    }

    @RequestMapping(value ="updateEvent",method = RequestMethod.POST)
    public ResponseEntity updateEvent(@RequestBody CalanderEventDAO calanderEventDAO){
        LOGGER.info("Reached update Event");
        loginService.updateEvent(calanderEventDAO);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        //return new ResponseEntity(eventsList, HttpStatus.OK);
    }


    @RequestMapping(value ="addEventGoogleCalener",method = RequestMethod.POST)
    public ResponseEntity addEventGoogleCalener(@RequestBody CalanderEventDAO calanderEventDAO){
        LOGGER.info("Reached google add event calander");
        googleCalender.getEvent();
        boolean isaddded=googleCalender.createEvent(calanderEventDAO);
        if(true) {
            LOGGER.info("Added to Google Calendar Successfully now adding to ReminderApp DB");
            loginService.addEvent(calanderEventDAO);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        else
            return new ResponseEntity<Boolean>(true, HttpStatus.UNAUTHORIZED);
        //return new ResponseEntity(eventsList, HttpStatus.OK);
    }





}

