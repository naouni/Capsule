package com.example.nordineaouni.Capsule;

/**
 * Created by nordineaouni on 04/03/17.
 */

public class Capsule {

    private String  text;
    private String senderID;
    private String sentDate;
    private String openingDate;//The date also contains the time information
    //TODO: ArrayList<String> pictureIDs

    public Capsule(){
        //Used by Firebase, do not delete
    }


    public Capsule(String text, String senderID, String sentDate, String openingDate){
        this.text = text;
        this.senderID = senderID;
        this.sentDate = sentDate;
        this.openingDate = openingDate;
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
