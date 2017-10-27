package turnout.example.abhinav.turnout.Event;

import android.support.annotation.Keep;

@Keep

public class EventName {
    public String eventID;
    public String eventContact;
    public String eventDes;
    public String image;
    public String eventName;
    public String eventDate;

    public EventName(){
       }


    public EventName(String eventID, String eventContact, String image, String eventDes, String eventName, String eventDate) {
        this.eventID = eventID;
        this.eventContact = eventContact;
        this.image = image;
        this.eventDes = eventDes;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    public String getEventContact() {
        return eventContact;
    }

    public void setEventContact(String eventContact) {
        this.eventContact = eventContact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEventDes() {
        return eventDes;
    }

    public void setEventDes(String eventDes) {
        this.eventDes = eventDes;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }



}