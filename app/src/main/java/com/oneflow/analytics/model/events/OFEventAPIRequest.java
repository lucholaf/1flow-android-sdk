package com.oneflow.analytics.model.events;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

import java.util.ArrayList;

public class OFEventAPIRequest extends OFBaseModel {

    @SerializedName("events")
    ArrayList<OFRecordEventsTabToAPI> events;
    @SerializedName("session_id") String sessionId;


    public ArrayList<OFRecordEventsTabToAPI> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<OFRecordEventsTabToAPI> events) {
        this.events = events;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
