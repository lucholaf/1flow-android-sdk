package com.circo.oneflow.model.events;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class EventAPIRequest implements Serializable {

    @SerializedName("events")
    ArrayList<RecordEventsTabToAPI> events;
    @SerializedName("session_id") String sessionId;

    public ArrayList<RecordEventsTabToAPI> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<RecordEventsTabToAPI> events) {
        this.events = events;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
