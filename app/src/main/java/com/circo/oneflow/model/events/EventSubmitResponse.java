package com.circo.oneflow.model.events;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventSubmitResponse {

    @SerializedName("update")
    private EventUpdate eventUpdate;
    @SerializedName("data")
    private ArrayList<RecordEventsTabToAPI> data;

    public EventUpdate getEventUpdate() {
        return eventUpdate;
    }

    public void setEventUpdate(EventUpdate eventUpdate) {
        this.eventUpdate = eventUpdate;
    }

    public ArrayList<RecordEventsTabToAPI> getData() {
        return data;
    }

    public void setData(ArrayList<RecordEventsTabToAPI> data) {
        this.data = data;
    }
}
