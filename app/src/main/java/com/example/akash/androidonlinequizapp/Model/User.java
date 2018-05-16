package com.example.akash.androidonlinequizapp.Model;

/**
 * Created by akash on 13/5/18.
 */

public class User {
    private String userName,password,email;
    public User(){

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {

        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public User(String userName, String password, String email) {

        this.userName = userName;
        this.password = password;
        this.email = email;
    }
}
