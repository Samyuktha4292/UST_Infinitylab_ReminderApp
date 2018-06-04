package com.ust.infinityLabs.reminderApp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@JsonIgnoreProperties
public class CalanderEventDAO {

   public Date toEventDateFormat(String date){
       Date dateTime = null;
       try {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
           dateTime = sdf.parse(date);
           System.out.println(dateTime);
       }
       catch (ParseException e) {
           e.printStackTrace();
       }
       return dateTime;
   }

   public String toTimeStamp(String timestamp) throws ParseException {
       DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
       LocalDateTime localDate = LocalDateTime.parse(timestamp, formatter);
       System.out.println(localDate);
       System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDate));
       return (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDate));
       //format.setTimeZone(TimeZone.getTimeZone("IST"));
   }

    public String getEventsummary() {
        return eventsummary;
    }

    public void setEventsummary(String eventsummary) {
        this.eventsummary = eventsummary;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    String eventsummary;
    String eventenddate;
    String eventstartdate;
    Integer Id;



}
