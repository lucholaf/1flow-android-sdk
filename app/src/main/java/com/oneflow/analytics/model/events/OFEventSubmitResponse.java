package com.oneflow.analytics.model.events;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OFEventSubmitResponse {

    @SerializedName("update")
    private OFEventUpdate eventUpdate;
    @SerializedName("data")
    private ArrayList<OFRecordEventsTabToAPI> data;

    public OFEventUpdate getEventUpdate() {
        return eventUpdate;
    }

    public void setEventUpdate(OFEventUpdate OFEventUpdate) {
        this.eventUpdate = OFEventUpdate;
    }

    public ArrayList<OFRecordEventsTabToAPI> getData() {
        return data;
    }

    public void setData(ArrayList<OFRecordEventsTabToAPI> data) {
        this.data = data;
    }
}
