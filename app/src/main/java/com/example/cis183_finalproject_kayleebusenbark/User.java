package com.example.cis183_finalproject_kayleebusenbark;

import java.io.Serializable;

public class User implements Serializable
{
    private int userId;
    private String username;
    private String fname;
    private String lname;
    private String password;

    public User()
    {}

    public User(int u_id, String u, String f, String l, String p)
    {
        userId = u_id;
        username = u;
        fname = f;
        lname = l;
        password = p;
    }

    //GETTERS
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    //SETTERS

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
