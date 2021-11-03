package com.oneflow.analytics.model.events;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventUpdate implements Serializable {
    @SerializedName("n")
    private String n;
    @SerializedName("nModified")
    private String nModified;
    @SerializedName("electionId")
    private String electionId;
    @SerializedName("ok")
    private String ok;
    @SerializedName("operationTime")
    private String operationTime;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getnModified() {
        return nModified;
    }

    public void setnModified(String nModified) {
        this.nModified = nModified;
    }

    

    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }
}
