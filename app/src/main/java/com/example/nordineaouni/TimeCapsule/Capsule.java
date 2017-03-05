package com.example.nordineaouni.TimeCapsule;

/**
 * Created by nordineaouni on 04/03/17.
 */

public class Capsule {

    private String  text;
    private String senderID;
    private String sentDate;
    private String openingDate;
    //TODO: ArrayList<String> pictureIDs

    public Capsule(){

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
