package com.example.katri.vsvotingsystem;

/**
 * Created by Katri on 05/01/2018.
 */

public class User {

    private static User instance = null;

    private String UsedID;
    private String Name;
    private String Email;
    private String Password;
    private boolean Admin;

    public User(String UserID, String Name, String Email, String Password, boolean Admin ) {
        this.UsedID = UserID;
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
        this.Admin = Admin;
    }

    public static User getInstance() {
        if(instance == null) {
            throw new AssertionError("You have to call init first");
        }

        return instance;
    }

    public synchronized static User init(String UserID, String Name, String Email, String Password, boolean Admin) {
        if (instance != null)
        {
            // in my opinion this is optional, but for the purists it ensures
            // that you only ever get the same instance when you call getInstance
            throw new AssertionError("You already initialized me");
        }

        instance = new User(UserID, Name, Email,Password,Admin);
        return instance;
    }

    public String GetUserID(){ return UsedID; }
    public String GetUserName(){ return Name; }
    public String GerEmail(){ return Email;}
}