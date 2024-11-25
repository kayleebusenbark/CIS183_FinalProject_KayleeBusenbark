package com.example.cis183_finalproject_kayleebusenbark;

public class SessionData
{
    private static User loggedInUser;

    public static User getLoggedInUser()
    {
        return loggedInUser;
    }
    public static void setLoggedInUser(User u)
    {
        loggedInUser = u;
    }
}
