package com.example.lap.lecture_4.classes;

/**
 * Created by lap on 3/23/2018.
 */

public class Event {
    private String eventName, eventDetails;
    private long id;

    public Event(String name, String details, long i)
    {
        eventName = name;
        eventDetails = details;
        id =i;
    }

    public void setEventName(String name)
    {
        eventName = name;
    }

    public void setEventDetails(String details)
    {
        eventDetails = details;
    }

    public void setEventId(long i)
    {
        id = i;
    }

    public String getEventName()
    {
        return  eventName;
    }

    public  String getEventDetails()
    {
        return eventDetails;
    }

    public long getEventId()
    {
        return id;
    }

    @Override
    public String toString() {
        return "Event : "+eventName + " On "+eventDetails;
    }
}
