package com.example.nordineaouni.Capsule;

/**
 * Created by nordineaouni on 04/03/17.
 */

public class Conversation {

    private String dateLastSent;//Date last capsule was sent by any member of the conversation
    private String dateClosestOpening;//Closest date on which a capsule can be opened by sender
    private String interlocutorID;//userID of the interlocutor
    private Long numberCapsules;

    public Conversation (){

    }

    public String getDateClosestOpening() {
        return dateClosestOpening;
    }

    public void setDateClosestOpening(String dateClosestOpening) {
        this.dateClosestOpening = dateClosestOpening;
    }

    public String getDateLastSent() {
        return dateLastSent;
    }

    public void setDateLastSent(String dateLastSent) {
        this.dateLastSent = dateLastSent;
    }

    public String getInterlocutorID() { return interlocutorID; }

    public void setInterlocutorID(String interlocutorID) {
        this.interlocutorID = interlocutorID;
    }

    public Long getNumberCapsules() { return numberCapsules; }

    public void setNumberCapsules(Long numberCapsule) { this.numberCapsules = numberCapsule; }

}
