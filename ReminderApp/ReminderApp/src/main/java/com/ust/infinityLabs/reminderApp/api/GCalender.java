package com.ust.infinityLabs.reminderApp.api;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.ust.infinityLabs.reminderApp.Controller.HomeController;
import com.ust.infinityLabs.reminderApp.Model.CalanderEventDAO;
import com.ust.infinityLabs.reminderApp.Model.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GCalender {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    Calendar service;

    public boolean createEvent(CalanderEventDAO calanderEventDAO){

        try {
            Event event = new Event();
            event.setSummary(calanderEventDAO.getEventsummary());
            EventDateTime start = new EventDateTime();
            start.setDateTime(new DateTime(calanderEventDAO.toEventDateFormat(calanderEventDAO.getEventstartdate())));
            EventDateTime end = new EventDateTime();
            end.setDateTime(new DateTime(calanderEventDAO.toEventDateFormat(calanderEventDAO.getEventstartdate())));
            event.setStart(start);
            start.setTimeZone("Asia/Calcutta");
            end.setTimeZone("Asia/Calcutta");
            event.setEnd(end);

            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
            event.setRecurrence(Arrays.asList(recurrence));

            EventAttendee[] attendees = new EventAttendee[] {
                    new EventAttendee().setEmail("sjithinkumar@gmail.com")
            };
            event.setAttendees(Arrays.asList(attendees));

            EventReminder[] reminderOverrides = new EventReminder[] {
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(10),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            String calendarId = "primary";
            event = service.events().insert(calendarId, event).execute();
            System.out.printf("Event Added Successfully: %s\n", event.getHtmlLink());
            if(!event.getHtmlLink().isEmpty())
                return true;
            else return false;
        }
        catch (Exception e){
            LOGGER.error("Error Occured while inserting event to Google Calander!"+e);
        }
        return false;
    }


    public void getEvent(){
        try{
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("endTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            if (items.isEmpty()) {
                System.out.println("No upcoming events found !!");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    LOGGER.info("%s (%s)\n", event.getSummary(), start);
                    System.out.printf("%s (%s)\n", event.getSummary(), start);
                }
            }
        }catch(Exception e){
            LOGGER.error("Got an Exception ");
        }
    }


}
