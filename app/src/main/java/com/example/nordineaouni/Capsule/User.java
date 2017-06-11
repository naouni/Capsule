package com.example.nordineaouni.Capsule;

/**
 * Created by nordineaouni on 11/06/17.
 */

public class User {

    public String userId;
    public String userName;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String userName){

    }

    public String getUserName(){
        return userName;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
         return userId;
    }
}
