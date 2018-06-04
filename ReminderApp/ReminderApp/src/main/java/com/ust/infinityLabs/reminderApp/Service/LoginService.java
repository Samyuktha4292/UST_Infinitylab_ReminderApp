package com.ust.infinityLabs.reminderApp.Service;



import com.ust.infinityLabs.reminderApp.Controller.HomeController;
import com.ust.infinityLabs.reminderApp.Model.CalanderEventDAO;
import com.ust.infinityLabs.reminderApp.Model.UserDAO;
import com.ust.infinityLabs.reminderApp.Model.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private UserDAO userDAO;

    private Properties properties;



    public void isUserValid(UserDAO userDAO) {
        Map<String, String> credentialsMap = new HashMap<String, String>();
        credentialsMap.put("username",userDAO.getUsername());
        credentialsMap.put("passwrd",userDAO.getPassword());
        String sql="select * from public.user where name=:username and password =:passwrd and active=true ;";
        List<Map<String, Object>>validUser=namedParameterJdbcTemplate.queryForList(sql,credentialsMap);
        if(validUser.size()>0) {
            userDAO.setValidUser(true);
            Utility.setUserDAO(userDAO);
            LOGGER.info("User is valid");
            LOGGER.debug("User is been set as valid user");
            }
            else{
            userDAO.setValidUser(false);
            }
        }



    public boolean updateEvent(CalanderEventDAO calanderEventDAO){
        String sql=null;
        sql="update public.event set eventsummary=:eventsummary, eventstartdate =:eventstartdate, eventenddate=:eventenddate where id=:eventid;";
        Map<String, Object> eventMap = new HashMap<String, Object>();
        eventMap.put("eventstartdate",calanderEventDAO.toEventDateFormat(calanderEventDAO.getEventstartdate()));
        eventMap.put("eventenddate",calanderEventDAO.toEventDateFormat(calanderEventDAO.getEventenddate()));
        eventMap.put("eventsummary", calanderEventDAO.getEventsummary());
        eventMap.put("eventid", calanderEventDAO.getId());
        namedParameterJdbcTemplate.update(sql,eventMap);
        return true;
        }

    public void addEvent(CalanderEventDAO eventdao) {
        Map<String, Object> eventMap = new HashMap<String, Object>();
        String sql=null;
        //LOGGER.info("user name is"+ eventdao.getUserDAO().getUsername());

        LOGGER.info("user name is"+  Utility.getUserDAO().getUsername());
        eventMap.put("eventsummary", eventdao.getEventsummary());
        eventMap.put("eventstartdate",eventdao.toEventDateFormat(eventdao.getEventstartdate()));
        eventMap.put("eventenddate",eventdao.toEventDateFormat(eventdao.getEventenddate()));
        eventMap.put("username",Utility.getUserDAO().getUsername());
        //sql="insert into public.event (eventname,eventdate,eventtime) values(:eventname,:eventdate, :eventtime) where username=:username ";
        sql="insert into public.event (eventsummary,eventstartdate,eventenddate,username) values (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    try {


                        preparedStatement.setString(1, eventdao.getEventsummary());
                        preparedStatement.setString(2, eventdao.getEventstartdate());
                        preparedStatement.setString(3, eventdao.getEventenddate());
                        preparedStatement.setString(4, Utility.getUserDAO().getUsername());
                    }catch(Exception e){
                        LOGGER.error("Caught error While inserting");
                    }
                }
            });
        }catch(Exception e){
            LOGGER.error("Exception occured while inserting the event"+e);
        }
    }

    public List viewEvent() {
        LOGGER.info("View Event Controller hit Started");
        Map<String, String> eventMap = new HashMap<String, String>();
        List<Map<String, Object>> eventrows = null;
        eventMap.put("username",Utility.getUserDAO().getUsername());
        String sql=null;
        try{
            sql="select eventsummary,eventstartdate,eventenddate,id from public.event where username=:username ;";
            eventrows= namedParameterJdbcTemplate.queryForList(sql,eventMap);
            if(eventrows.size()>=1){
               return eventrows;
            }else{
                return null;
            }
        }catch(Exception e){
            LOGGER.error("Exception occurred while retrieving"+e);
        }
        return eventrows;

    }

    public void register(UserDAO userDAO) {
        String sql="insert into public.user (name,password,active) values (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1,userDAO.getUsername());
                    preparedStatement.setObject(2,userDAO.getPassword());
                    preparedStatement.setObject(3,(userDAO.getActive())==null ? true:userDAO.getActive());
                }
            });
        }catch(Exception e){
            LOGGER.error("Exception occured while inserting the event"+e);
        }

    }

}

