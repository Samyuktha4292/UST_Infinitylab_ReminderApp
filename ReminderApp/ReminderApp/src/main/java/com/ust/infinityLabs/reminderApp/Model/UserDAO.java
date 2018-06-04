package com.ust.infinityLabs.reminderApp.Model;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Entity
@Table(name = "USER")
@Component
public class UserDAO {


    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    private Boolean active;

    public boolean isValidUser() {
        return isValidUser;
    }

    public void setValidUser(boolean validUser) {
        isValidUser = validUser;
    }

    private boolean isValidUser;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDAO() {

    }

}