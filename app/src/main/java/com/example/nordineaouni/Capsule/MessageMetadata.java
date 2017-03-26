package com.example.nordineaouni.Capsule;

/**
 * Created by nordineaouni on 01/02/17.
 */

public class MessageMetadata {

    public long sent;
    public String interlocutorID;
    public String sentDate;
    public String deliveryDate;


    public MessageMetadata(){
        //Empty constructor used by Firebase. Do not delete
    }

    public MessageMetadata(long sent, String interlocutorID, String sentDate, String deliveryDate){
        this.sent = sent;
        this.interlocutorID = interlocutorID;
        this.sentDate = sentDate;
        this.deliveryDate = deliveryDate;

    }


}
