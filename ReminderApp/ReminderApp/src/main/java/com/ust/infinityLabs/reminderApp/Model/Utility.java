package com.ust.infinityLabs.reminderApp.Model;

import org.springframework.stereotype.Component;

@Component
public class Utility {

    private static UserDAO userDAO;

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public static void setUserDAO(UserDAO userDAO) {
        Utility.userDAO = userDAO;
    }

}
