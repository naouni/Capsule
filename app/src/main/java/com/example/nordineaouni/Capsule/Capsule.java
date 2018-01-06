package com.example.nordineaouni.Capsule;

/**
 * Created by nordineaouni on 04/03/17.
 */

public class Capsule {

    private String senderID;
    private String sentDate;
    private String  text;
    private String openingDate;//The date also contains the time information
    private double latitude;
    private double longitude;
    private long radius;

    public Capsule(){
        //Used by Firebase, do not delete
    }


    // constructor by the chats which does not require a location yet.
    public Capsule(String text, String senderID, String sentDate, String openingDate){
        this.text = text;
        this.senderID = senderID;
        this.sentDate = sentDate;
        this.openingDate = openingDate;
        this.latitude = -360; //Impossible value
        this.longitude = -360; //Impossible value
        this.radius = -1; // impossible value
    }

    public Capsule(String text, String senderID, String sentDate, String openingDate, double latitude, double longitude, long radius){
        this.text = text;
        this.senderID = senderID;
        this.sentDate = sentDate;
        this.openingDate = openingDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;

    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
